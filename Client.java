import java.io.*;
import java.net.*;

public class Client {
    // Declare p, g, and Key of client
    static final int PORT = 10000;
    static final int P = 100000000;
    static final int G = 7;
    static final int PRIVATE_KEY = 6;
    
    static String serverName = "localhost";
    static double clientPublicKey, serverPublicKey, sharedKey;
    static DataOutputStream dos;
    static BufferedReader br, kb;
    static Socket s;

    public static void main(String[] args) {
        try {
            System.out.println("Client Connected at port " + PORT);

            s = new Socket(serverName, PORT); // Create client socket
            System.out.println("Just connected to " + s.getRemoteSocketAddress());

            // Secret Key Exchange (Diffie-Hellman)
            OutputStream outToServer = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF(Integer.toString(P)); // Sending p
            out.writeUTF(Integer.toString(G)); // Sending g

            clientPublicKey = (Math.pow(G, PRIVATE_KEY)) % P; // calculation of A
            out.writeUTF(Double.toString(clientPublicKey)); // Sending A

            System.out.println("From Client: Private Key = " + PRIVATE_KEY); // Client's Private Key

            DataInputStream in = new DataInputStream(s.getInputStream()); // Accepts the data

            serverPublicKey = Double.parseDouble(in.readUTF());
            System.out.println("From Server: Public Key = " + serverPublicKey);

            sharedKey = (Math.pow(serverPublicKey, PRIVATE_KEY)) % P; // calculation of shared key

            System.out.println("Secret Key for Symmetric Encryption using Diffie-Hellman = " + sharedKey);

            Blowfish blowfish = new Blowfish(sharedKey);

            System.out.println("\n\nSecret Key was sent; now the conversation can start between the client and the server\n\n");

            // Sending encrypted messages to the server and decrypting the ciphertext
            // received from the server
            dos = new DataOutputStream(s.getOutputStream()); // to send data to the server
            br = new BufferedReader(new InputStreamReader(s.getInputStream())); // to read data coming from the server
            kb = new BufferedReader(new InputStreamReader(System.in));

            String str, str1;

            while (!(str = kb.readLine()).equals("exit")) // repeat as long as exit is not typed at client
            {
                String ciphertext = blowfish.convertPlaintextToCiphertext(str); // str is the plaintext to be sent

                System.out.println("\nPlaintext is: " + str + "\nThe Ciphertext sent is: " + ciphertext + "\n");
                dos.writeBytes(ciphertext + "\n"); // send to the server
                str1 = br.readLine(); // receive from the server str1 is the ciphertext
                String plaintextreceived = blowfish.convertCiphertextToPlaintext(str1);
                System.out.println(
                        "\nCiphertext received is: " + str1 + "\nCorresponds to the Plaintext: " + plaintextreceived
                                + "\n");
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private static void closeResources() {
        try {
            if (dos != null) dos.close();
            if (br != null) br.close();
            if (kb != null) kb.close();
            if (s != null) s.close();
        } catch (IOException e) {
            // Handle exception or log it
            e.printStackTrace();
        }
    }
}

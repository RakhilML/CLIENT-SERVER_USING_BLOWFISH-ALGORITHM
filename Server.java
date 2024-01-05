import java.io.*;
import java.net.*;

public class Server {
    private static int port = 10000;
    private static int serverPrivateKey = 3; // Server Key
    private static double clientP, clientG, clientPublicKey, serverPublicKey, clientSharedKey;
    private static String serverPublicKeyString;
    private static PrintStream ps;
    private static BufferedReader br, kb;
    private static Socket s;
    private static ServerSocket ss;

    public static void main(String args[]) {
        try {
            System.out.println("Server started at port " + port);

            ss = new ServerSocket(port);
            System.out.println("Waiting for the client on port " + ss.getLocalPort() + "...");
            s = ss.accept();
            System.out.println("Connection established");

            // Secret Key Exchange (Diffie-Hellman)
            System.out.println("From Server: Private Key = " + serverPrivateKey);

            DataInputStream in = new DataInputStream(s.getInputStream());

            clientP = Double.parseDouble(in.readUTF());
            System.out.println("From Client: P = " + clientP);

            clientG = Double.parseDouble(in.readUTF());
            System.out.println("From Client: G = " + clientG);

            clientPublicKey = Double.parseDouble(in.readUTF());
            System.out.println("From Client: Public Key = " + clientPublicKey);

            serverPublicKey = (Math.pow(clientG, serverPrivateKey)) % clientP;
            serverPublicKeyString = Double.toString(serverPublicKey);

            // Sends data to the client
            OutputStream outToClient = s.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);

            out.writeUTF(serverPublicKeyString);

            clientSharedKey = (Math.pow(clientPublicKey, serverPrivateKey)) % clientP;

            System.out.println("Shared Key for Symmetric Encryption using Diffie-Hellman = " + clientSharedKey);

            Blowfish blowfish = new Blowfish(clientSharedKey);

            System.out.println("\n\nSecret Key was sent, now the conversation can start between the client and the server\n\n");

            // Sending encrypted messages to the client and decrypting the ciphertext received from the client
            ps = new PrintStream(s.getOutputStream());
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            kb = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                String receivedCiphertext;

                while ((receivedCiphertext = br.readLine()) != null) {
                    String receivedPlaintext = blowfish.convertCiphertextToPlaintext(receivedCiphertext);
                    System.out.println("\nCiphertext received is: " + receivedCiphertext +
                            "\nCorresponds to the Plaintext: " + receivedPlaintext + "\n");

                    String plaintextToSend = kb.readLine();
                    String sentCiphertext = blowfish.convertPlaintextToCiphertext(plaintextToSend);
                    System.out.println("\nPlaintext is: " + plaintextToSend +
                            "\nThe Ciphertext sent is: " + sentCiphertext + "\n");

                    ps.println(sentCiphertext);
                }
            }
        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            System.out.println("Exception " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private static void closeResources() {
        try {
            if (ps != null) ps.close();
            if (br != null) br.close();
            if (kb != null) kb.close();
            if (ss != null) ss.close();
            if (s != null) s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}

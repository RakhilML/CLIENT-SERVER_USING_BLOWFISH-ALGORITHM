# CLIENT-SERVER_USING_BLOWFISH-ALGORITHM
The server generates a private key (serverPrivateKey). It receives the client’s values for p, g, and clientPublicKey over the established connection. The server calculates its public key (serverPublicKey) using the received values and sends it back to the client. It computes the shared secret key (clientSharedKey) using the client’s public key.

Open the CMD in the Saved directory 
## Compile 
```
javac -d . *.java
```
This compiles the .java file and .class file is saved to directory. A Java class file is a file (with the .class filename extension) containing Java bytecode that can be executed on the Java Virtual Machine (JVM).
## Start server
```
java Server.java
```
Open the cmd and run the server and client in different terminals and communication will start.

## Start Client
```
java Client.java
```

##  Server client communication
![image](https://github.com/RakhilML/CLIENT-SERVER_USING_BLOWFISH-ALGORITHM/assets/106943173/6ac15750-4c56-404b-863c-ebb02ab516cb)

## Working of Server client communication
![image](https://github.com/RakhilML/CLIENT-SERVER_USING_BLOWFISH-ALGORITHM/assets/106943173/0b937177-03b7-454a-9008-bfb0c83f62c2)

## TERMINATION
The closeResources method ensures proper closure of resources.

## USER INTERACTION
The client continues the communication loop until the user types "exit" in the
console.



package Server;

import java.net.*;
import java.util.*;
import java.io.*;

public class ChatServer {

    private static final int portNumber = 8080;

    private int serverPort;
    private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"

    public static void main(String[] args){
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber){
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients(){
        return clients;
    }

    private void startServer(){
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e){
            System.err.println("Could not listen on port: "+ serverPort);
            System.exit(1); 
            //exit code 0 means successful termination, however non-zero exit status indicates error
        }
    }

    private void acceptClients(ServerSocket serverSocket){

        System.out.println("server starts port: " + serverSocket.getLocalSocketAddress());
        while(true){
            try{
                Socket socket = serverSocket.accept();
                System.out.println("accepts : " + socket.getRemoteSocketAddress());
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException ex){
                System.out.println("Accept failed on : "+ serverPort);
            }
        }
    }

}

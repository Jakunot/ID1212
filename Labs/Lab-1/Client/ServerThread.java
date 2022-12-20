package Client;

import java.io.*;
import java.net.*;
import java.util.*;


public class ServerThread implements Runnable {

    private Socket socket;
    private String userName;
    private final LinkedList<String> messagesToSend;
    private boolean hasMessages = false;

    public ServerThread(Socket socket, String userName) {

        this.socket = socket;
        this.userName = userName;
        messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String message){
        synchronized (messagesToSend){
            hasMessages = true;
            messagesToSend.push(message);
        }
    }
    /*
     * Socket objects have several properties that are accessible through getter methods
     * public InetAddress getInetAddress(), tells you the remote host
     * public int getPort(), tells you thep  port the socket is connected to
     * public InetAddress getLocalAddress(), tells you the network inteface
     * public int getLocalport(), tells you the port the socket connected from
     * no setter methods since these properties are set as soon as the socket connects and are fixed
     * from there on
     */

    @Override
    public void run() {
        //in the run method is what happens when the thread is on
        System.out.println("Welcome :" + userName);
        System.out.println("Local Port :" + socket.getLocalPort());
        System.out.println("Server " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try{

            PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false); // for writing data from applicatio
            InputStream serverInStream = socket.getInputStream(); //to read bytes from the socket
            Scanner scanner = new Scanner(serverInStream);
            
            //the isClosed() method returns true if the socket is closed, false if it isn't
            //So basically, while socket is not closed, perform ... 
            while(!socket.isClosed()){
                if(serverInStream.available() > 0){
                    if(scanner.hasNextLine()){
                        System.out.println(scanner.nextLine());
                    }
                }
                if(hasMessages){
                    String nextSend = "";
                    synchronized(messagesToSend){
                        nextSend = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    serverOut.println(userName + " > " + nextSend);
                    serverOut.flush();
                }
            }
        }
        catch(IOException e){

            e.printStackTrace();
            
        }

    }
}

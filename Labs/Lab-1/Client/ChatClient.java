package Client;

import java.net.*;
import java.util.*;
import java.io.*;

public class ChatClient {

  //declaring variables for the socket parameter

  private static final String host = "localhost";
  private static final int portNumber = 8080;

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    
    String readUserName = null;
    System.out.println("Enter username: ");
    while(readUserName == null || readUserName.trim().equals("")){
      // null, empty, whitespace(s) not allowed.
      readUserName = scanner.nextLine();
      if(readUserName.trim().equals("")){
        System.out.println("Invalid! No spaces allowed.");
      }
    }

    try{

      //This doesn't just create the object, it actually makes the connection
      //if server isn't listening att given port number throw IOexception
      
      Socket socket = new Socket(host, portNumber);
      Thread.sleep(1000); // waiting for network communication.

      ServerThread serverThread = new ServerThread(socket, readUserName);
      Thread serverAccessThread = new Thread(serverThread);
      serverAccessThread.start();
      while(serverAccessThread.isAlive()){
        if(scanner.hasNextLine()){
          serverThread.addNextMessage(scanner.nextLine());
        }
      }
    }catch(IOException e){

      System.err.println("Fatal Connection error!");
      e.printStackTrace();

    }catch(InterruptedException ex){

      System.out.println("Interrupted");
    
    }finally{

      scanner.close();

    }
  }

}


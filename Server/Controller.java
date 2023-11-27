package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Controller {

  private ServerSocket server;
  private final Object lock;

  public Controller() {
    lock = new Object();
    try {
      server = new ServerSocket(4449);
    } catch(IOException ioe) {
      System.out.println("Erorr " + ioe);
    }
  }
  public void doSomething() {
    System.out.println("The server is running ...");
    while(true) {
      try {
        Socket socket = server.accept();
        int index = PoolSocket.addSocket(socket);
        if(index != -1) {
          synchronized(lock) {
            Service service = new Service(socket, index);
            service.go();
          }
        } else {
            socket.close();
            System.out.println("index " + index);
        }
      } catch(IOException ioe) {
        System.out.println("Erorr " + ioe);
      }

    }
  }
}

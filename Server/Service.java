package Server;

import java.io.*;
import java.net.Socket;


public class Service implements Runnable {

  private final Socket socket;
  private final Thread thread;
  private int index;

  public Service(Socket socket, int index) {
    this.socket = socket;
    this.index = index;
    thread = new Thread(this);
  }

  public void run() {
    System.out.println(socket);

    try {
      InputStream in = socket.getInputStream();
      int data = in.read();
      System.out.println(data);
      char level = (char)data;
      String levelName = "levels" + File.separator + "level" + level + ".sok";
      String message = loadLevel(levelName);
      OutputStream out = socket.getOutputStream();
      out.write((message + "\n").getBytes());
      out.flush();
    } catch (IOException ioe) {
    }
    PoolSocket.removeAtSocket(index);
    try {
        socket.close();
    } catch(IOException ioe) {
        System.out.println("Error " + ioe);
    }


  }

  public void go() {
    thread.start();
  }

  private String loadLevel(String levelFileName) {

    String data = "";

    FileInputStream in = null;
    try {
      in = new FileInputStream(levelFileName);
      int unicode;
      while ((unicode = in.read()) != -1) {
        char symbol = (char)unicode;
        if(('0' <= symbol && symbol <= '4')) {
          data = data + symbol;
        }

        if(symbol == '\n') {
          data = data + 'A';
	}
      }
    } catch (IOException ioe) {
      System.out.println("Error " + ioe);
    } finally {
      try {
        if (in != null) {
          in.close();
        }
      } catch (IOException ioe) {
        System.out.println("Error " + ioe);
      }
    }
    return data;
  }

}

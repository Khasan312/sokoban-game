  package ServerNIO;


  public class Main {
    public static void main(String[] args) {
      NIOServer server = new NIOServer(4449);
      server.start();
    }
  }

package Server;

import java.net.Socket;

public class PoolSocket {
  public static final Socket[] POOL_SOCKET = new Socket[5];

  public static int addSocket(Socket socket) {
    for(int i = 0 ; i < POOL_SOCKET.length; i++) {
      Socket element = POOL_SOCKET[i];
      if(element == null) {
          POOL_SOCKET[i] = socket;
          return i;
      }
    }
    return -1;
  }

  public static void removeAtSocket(int index) {
    POOL_SOCKET[index] = null;
  }
}

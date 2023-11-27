package ServerNIO;

import java.nio.channels.SocketChannel;

public class RequestHandlerFactory {
    public static RequestHandler getHandler(SocketChannel socketChannel, String requestData) {

        if (!requestData.isEmpty()) {

            if (requestData.equals("7") || requestData.equals("8") || requestData.equals("9")) {
                return new LevelRequestHandler(socketChannel, requestData);
            } else {
                return new PlayerRequestHandler(socketChannel, requestData);
            }
        }

        return null;
    }
}

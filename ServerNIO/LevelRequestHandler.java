package ServerNIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class LevelRequestHandler implements RequestHandler {
    private final SocketChannel socketChannel;
    private final String level;

    public LevelRequestHandler(SocketChannel socketChannel, String level) {
        this.socketChannel = socketChannel;
        this.level = level;
    }

    @Override
    public void handleRequest() throws IOException {
        System.out.println("Handling level request for level: " + level);
        String levelName = "levels/level" + level + ".sok";
        String message = loadLevel(levelName);
        System.out.println(message);

        ByteBuffer responseBuffer = ByteBuffer.wrap((message + "\n").getBytes());
        socketChannel.write(responseBuffer);
        socketChannel.close();

        System.out.println("Connection closed for level request");
    }

    private String loadLevel(String levelFileName) {
        StringBuilder data = new StringBuilder();
        try (FileInputStream in = new FileInputStream(levelFileName)) {
            int unicode;
            while ((unicode = in.read()) != -1) {
                char symbol = (char) unicode;
                if ('0' <= symbol && symbol <= '4') {
                    data.append(symbol);
                }

                if (symbol == '\n') {
                    data.append('A');
                }
            }
        } catch (IOException ioe) {
            System.out.println("Error " + ioe);
        }
        return data.toString();
    }
}


package ServerNIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class PlayerRequestHandler implements RequestHandler {
    private final SocketChannel socketChannel;
    private final String data;

    public PlayerRequestHandler(SocketChannel socketChannel, String data) {
        this.socketChannel = socketChannel;
        this.data = data;
    }

    public void handleRequest() {
        if (data.contains("Arr")) {
            String name = parseJsonName(data);
            String moves = parseJsonMoves(data);
            System.out.println();
            System.out.println(moves);
            System.out.println();
            System.out.println(Storage.getPlayers());
            Player player = Storage.getPlayer(name);
            sendMovesToOpponent(player, moves);
        } else if (data.contains("status")) {
            String name = parseJsonName(data);
            boolean status = parseJsonStatus(data);
            Storage.createPlayer(name, status, socketChannel);
            Storage.createSession();
        }
    }

    private String parseJsonName(String json) {
        Map<String, String> keyValueMap = parseJson(json);
        return keyValueMap.get("name");
    }

    private boolean parseJsonStatus(String json) {
        Map<String, String> keyValueMap = parseJson(json);
        return Boolean.parseBoolean(keyValueMap.get("status"));
    }

    private String parseJsonMoves(String json) {
        Map<String, String> keyValueMap = parseJson(json);
        return keyValueMap.get("moves");
    }

    private Map<String, String> parseJson(String json) {
        String[] keyValuePairs = json.split(" ");

        Map<String, String> keyValueMap = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":");
            if (entry.length == 2) {
                keyValueMap.put(entry[0].trim(), entry[1].trim());
            }
        }
        return keyValueMap;
    }

    public void sendMovesToOpponent(Player sender, String moves) {
        if (!Storage.sessions.isEmpty()) {
            Player opponent = Storage.findOpponent(sender);
            if (opponent != null) {
                SocketChannel opponentSocket = opponent.getSocketChannel();
                if (opponentSocket != null && opponentSocket.isOpen() && !opponentSocket.equals(socketChannel)) {
                    ByteBuffer responseBuffer = ByteBuffer.wrap((moves + "\n").getBytes());
                    try {
                        opponentSocket.write(responseBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

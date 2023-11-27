package ServerNIO;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {

    private static int playerId = 1;
    private static int sessionId = 1;
    public static Map<Integer, Player> players = new HashMap<>();
    public static Map<Integer, Session> sessions = new HashMap<>();

    public static void createPlayer(String name, boolean status, SocketChannel socketChannel) {
        Player player = new Player(name, status);
        player.setSocketChannel(socketChannel);
        player.setId(playerId++);
        players.put(playerId, player);
    }

    public static List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public static Player getPlayer(String name) {
        Player foundPlayer = null;
        for (Player player : getPlayers()) {
            if (player.getName().equals(name)) {
                foundPlayer = player;
            }
        }
        return foundPlayer;
    }

    public static void createSession() {
        if (players.size() % 2 != 0) {
            System.out.println("One player");
            return;
        }
        List<Player> playersInSession = new ArrayList<>();

        for (Player player : getPlayers()) {
            if (!player.isInGame()) {
                playersInSession.add(player);
            }
        }

        if (playersInSession.size() == 2) {
            Session session = new Session(playersInSession.get(0), playersInSession.get(1));
            session.setSessionId(sessionId++);
            playersInSession.get(0).setSessionId(sessionId);
            playersInSession.get(1).setSessionId(sessionId);
            sessions.put(session.getSessionId(), session);

            for (Player player : playersInSession) {
                player.setInGame(true);
            }
            playersInSession.clear();
        }
        System.out.println(sessions.get(1).toString());
    }

    public static Player findOpponent(Player player) {
        for (Player potentialOpponent : getPlayers()) {
            if (player.getSessionId() == potentialOpponent.getSessionId() && !player.getName().equals(potentialOpponent.getName())) {
                System.out.println(potentialOpponent.getName());
                return potentialOpponent;
            }
        }
        return null;
    }
}

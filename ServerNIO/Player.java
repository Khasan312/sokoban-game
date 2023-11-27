package ServerNIO;

import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Player {

    private int id;
    private String name;
    private boolean isInGame;
    private int sessionId;
    private SocketChannel socketChannel;


    public Player(String name, boolean isInGame) {
        this.name = name;
        this.isInGame = isInGame;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", isInGame=" + isInGame +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return isInGame == player.isInGame && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isInGame);
    }
}

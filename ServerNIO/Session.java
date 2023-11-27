    package ServerNIO;

    import java.util.Objects;

    public class Session {

        private int sessionId;
        private Player player1;
        private Player player2;

        public Session(Player player1, Player player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public int getSessionId() {
            return sessionId;
        }

        public void setSessionId(int sessionId) {
            this.sessionId = sessionId;
        }

        @Override
        public String toString() {
            return "Session{" +
                    "sessionId=" + sessionId +
                    ", player1=" + player1 +
                    ", player2=" + player2 +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Session session = (Session) o;
            return sessionId == session.sessionId && Objects.equals(player1, session.player1) && Objects.equals(player2, session.player2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sessionId, player1, player2);
        }
    }

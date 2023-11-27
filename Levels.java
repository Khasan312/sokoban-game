import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Levels {

    private int level;

    public Levels() {
        level = 1;
    }

    public int[][] nextLevel() {
        int[][] desktop = null;

        switch (level) {
            case 1:
                desktop = firstLevel();
                break;
            case 2:
                desktop = secondLevel();
                break;
            case 3:
                desktop = thirdLevel();
                break;
            case 4:
                desktop = fourthLevel();
                break;
            case 5:
                desktop = fifthLevel();
                break;
            case 6:
                desktop = sixthLevel();
                break;
            case 7:
                desktop = seventhLevel();
                break;
            case 8:
                desktop = eigththLevel();
                break;
            case 9:
                desktop = ninethLevel();
                break;
            case 10:
                desktop = tenthLevel();
                break;

            default:
                desktop = firstLevel();
                level = 1;
        }

        level = level + 1;
        return desktop;
    }

    private int[][] firstLevel() {
        String levelName = "levels/level1.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] secondLevel() {
        String levelName = "levels/level2.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] thirdLevel() {
        String levelName = "levels/level3.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] fourthLevel() {
        String levelName = "levels/level4.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] fifthLevel() {
        String levelName = "levels/level5.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] sixthLevel() {
        String levelName = "levels/level6.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] seventhLevel() {
        String answer = loadLevelFromServer('7');
        int[][] desktop = parseData(answer, 'A');
        return desktop;
    }

    private int[][] eigththLevel() {
        String answer = loadLevelFromServer('8');
        int[][] desktop = parseData(answer, 'A');
        return desktop;
    }

    private int[][] ninethLevel() {
        String answer = loadLevelFromServer('9');
        int[][] desktop = parseData(answer, 'A');
        return desktop;
    }

    private int[][] tenthLevel() {
        String levelName = "levels/level10.sok";
        String answer = loadLevel(levelName);
        int[][] desktop = parseData(answer);
        return desktop;
    }

    private int[][] parseData(String data) {

        int[][] array = null;

        int row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == '\n') {
                row = row + 1;
            }
        }

        array = new int[row][3];
        int column = 0;
        row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == '\n') {
                array[row] = new int[column];
                row = row + 1;
                column = 0;
                continue;
            }
            column = column + 1;
        }

        column = 0;
        row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == '\n') {
                row = row + 1;
                column = 0;
                continue;
            }
            int element = Integer.parseInt("" + symbol);
            array[row][column] = element;

            column = column + 1;
        }

        return array;
    }

    private String loadLevel(String levelFileName) {

        String data = "";

        FileInputStream in = null;
        try {
            in = new FileInputStream(levelFileName);
            int unicode;
            while ((unicode = in.read()) != -1) {
                char symbol = (char) unicode;
                if (('0' <= symbol && symbol <= '4') || (symbol == '\n')) {
                    data = data + symbol;
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

    public int[][] continueLevel() {
        String levelName = "data_for_continue/playProgress.txt";
        String answer = loadContinueLevel(levelName);
        int[][] desktop = null;
        desktop = parseData(answer);
        return desktop;
    }
    private String loadContinueLevel(String levelFileName) {

        String data = "";
        boolean firstLineSkipped = false;
        FileInputStream in = null;
        try {
            in = new FileInputStream(levelFileName);
            int unicode;
            while ((unicode = in.read()) != -1) {
                char symbol = (char) unicode;

                if (symbol == '\n' && !firstLineSkipped) {
                    firstLineSkipped = true; 
                } else if (firstLineSkipped && (('0' <= symbol && symbol <= '4') || (symbol == '\n'))) {
                    data = data + symbol;
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


    private String loadLevelFromServer(char level) {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(Options.HOST_IP, Options.PORT))) {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte) level);
            buffer.flip();
            socketChannel.write(buffer);

            ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
            int bytesRead = socketChannel.read(responseBuffer);

            socketChannel.close();

            if (bytesRead > 0) {
                return new String(responseBuffer.array(), 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int[][] parseData(String data, char newLineSymbol) {

        int[][] array = null;

        int row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == newLineSymbol) {
                row = row + 1;
            }
        }

        array = new int[row][3];
        int column = 0;
        row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == newLineSymbol) {
                array[row] = new int[column];
                row = row + 1;
                column = 0;
                continue;
            }
            column = column + 1;
        }

        column = 0;
        row = 0;
        for (int i = 0; i < data.length(); i++) {
            char symbol = data.charAt(i);
            if (symbol == newLineSymbol) {
                row = row + 1;
                column = 0;
                continue;
            }
            int element = Integer.parseInt("" + symbol);
            array[row][column] = element;

            column = column + 1;
        }
        return array;
    }

    public void setLevel(int level) {
    this.level = level;
    }

    public int getLevel() {
        return level;
    }

}

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MultiplayerCanvas extends JPanel {

    private Model model;
    private Image backgroundImage;
    private Image imageGamer;
    private Image imageWall;
    private Image imageBox;
    private Image imageGoal;
    private Image imageGround;
    private Image imageError;
    private boolean b = false;
    private boolean isLoaded = false;
    private int[][] desktop;

    public MultiplayerCanvas(Model model) {
        this.model = model;
        setBackground(Color.BLACK);
        setOpaque(true);

        File fileGamer = new File("images/rick/rick1.png");
        File fileWall = new File("images/rick/metalCenter2.png");
        File fileBox = new File("images/rick/box.png");
        File fileGoal = new File("images/rick/goal1.png");
        File fileError = new File("images/error.png");
        File fileGround = new File("images/rick/groundd.png");

        try {
            backgroundImage = ImageIO.read(new File("images/background.png"));
            imageGamer = ImageIO.read(fileGamer);
            imageWall = ImageIO.read(fileWall);
            imageBox = ImageIO.read(fileBox);
            imageGoal = ImageIO.read(fileGoal);
            imageError = ImageIO.read(fileError);
            imageGround = ImageIO.read(fileGround);

            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setOpaque(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
    }

    public void paint(Graphics g) {
        super.paint(g);
        boolean state = model.getState();
        if (state) {
            drawDesktop(g);
            try {
                drawOpponentDesktop(g);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            drawErrorMessage(g);
        }
    }

    private void drawDesktop(Graphics g) {
        int[][] desktop = model.getDesktop();
        int start = 50;
        int x = start;
        int y = 130;
        int width = 50;
        int height = 50;
        int offset = 0;

        for (int i = 0; i < desktop.length; i++) {
            for (int j = 0; j < desktop[i].length; j++) {
                if (desktop[i][j] == Options.GAMER) {
                    g.drawImage(imageGamer, x, y, null);
                } else if (desktop[i][j] == Options.WALL) {
                    g.drawImage(imageWall, x, y, null);
                } else if (desktop[i][j] == Options.BOX) {
                    g.drawImage(imageBox, x, y, null);
                } else if (desktop[i][j] == Options.GOAL) {
                    g.drawImage(imageGoal, x, y, null);
                } else {
                    g.drawImage(imageGround, x, y, null);
                }
                x = x + width + offset;
            }
            x = start;
            y = y + height + offset;
        }
    }

    private void drawOpponentDesktop(Graphics g) throws IOException {
        int start = 650;
        int x = start;
        int y = 130;
        int width = 50;
        int height = 50;
        int offset = 0;
        desktop = model.getSecondDesktop();

        for (int i = 0; i < desktop.length; i++) {
            for (int j = 0; j < desktop[i].length; j++) {
                if (desktop[i][j] == Options.GAMER) {
                    g.drawImage(imageGamer, x, y, null);
                } else if (desktop[i][j] == Options.WALL) {
                    g.drawImage(imageWall, x, y, null);
                } else if (desktop[i][j] == Options.BOX) {
                    g.drawImage(imageBox, x, y, null);
                } else if (desktop[i][j] == Options.GOAL) {
                    g.drawImage(imageGoal, x, y, null);
                } else {
                    g.drawImage(imageGround, x, y, null);
                }
                x = x + width + offset;
            }
            x = start;
            y = y + height + offset;
        }
    }

    private void drawErrorMessage(Graphics g) {
        Font font = new Font("Impact", Font.BOLD, 50);
        g.drawImage(imageError, 200, 200, null);
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Initialization Error!", 250, 100);
    }
}

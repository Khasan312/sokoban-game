import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SkinMenuGameField extends JPanel {

    private Image imageGamer;
    private Image imageWall;
    private Image imageBox;
    private Image imageGoal;
    private Image imageError;
    private Image imageGround;
    private Image imageBackground;
    private Model model;

    public SkinMenuGameField(Model model) {
        this.model = model;

        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.BLACK);
        setOpaque(true);
        if (model.modelSkinStateGetter().equals("morty")) {
            File fileGamer = new File("images/morty/morty.png");
            File fileWall = new File("images/morty/metalCenter7.png");
            File fileBox = new File("images/morty/box3.png");
            File fileGoal = new File("images/morty/goal.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/morty/groundd.png");
            File fileBackground = new File("images/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
        if (model.modelSkinStateGetter().equals("rick")) {
            File fileGamer = new File("images/rick/rick1.png");
            File fileWall = new File("images/rick/metalCenter2.png");
            File fileBox = new File("images/rick/box.png");
            File fileGoal = new File("images/rick/goal1.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/rick/groundd.png");
            File fileBackground = new File("images/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }if (model.modelSkinStateGetter().equals("aang")) {
            File fileGamer = new File("images/avatar/skins/aang/aang.png");
            File fileWall = new File("images/avatar/skins/aang/wall.png");
            File fileBox = new File("images/rick/box.png");
            File fileGoal = new File("images/rick/goal1.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/avatar/skins/aang/ground.png");
            File fileBackground = new File("images/avatar/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawDesktop(g);
    }

    private void drawDesktop(Graphics g) {
        int[][] desktop = getDemoLevel();
        int start = 150;
        int x = start;
        int y = start - 50;
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

    public SkinMenuGameField getCanvas() {
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageBackground, 0, 0, null);
    }

    private int[][] getDemoLevel() {
        int[][] demoLevel = {
                { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 0, 0, 1, 3, 0, 0, 4, 0, 2 },
                { 2, 0, 0, 0, 3, 0, 0, 4, 0, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
                { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 }
        };
        return demoLevel;
    }

    public void drawSkins() {
        if (model.modelSkinStateGetter().equals("morty")) {
            File fileGamer = new File("images/morty/morty.png");
            File fileWall = new File("images/morty/metalCenter7.png");
            File fileBox = new File("images/morty/box3.png");
            File fileGoal = new File("images/morty/goal.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/morty/groundd.png");
            File fileBackground = new File("images/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
        if (model.modelSkinStateGetter().equals("rick")) {
            File fileGamer = new File("images/rick/rick1.png");
            File fileWall = new File("images/rick/metalCenter2.png");
            File fileBox = new File("images/rick/box.png");
            File fileGoal = new File("images/rick/goal1.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/rick/groundd.png");
            File fileBackground = new File("images/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
        if (model.modelSkinStateGetter().equals("aang")) {
            File fileGamer = new File("images/avatar/skins/aang/aang.png");
            File fileWall = new File("images/avatar/skins/aang/wall.png");
            File fileBox = new File("images/avatar/skins/aang/box.png");
            File fileGoal = new File("images/rick/goal1.png");
            File fileError = new File("images/error.png");
            File fileGround = new File("images/avatar/skins/aang/ground.png");
            File fileBackground = new File("images/avatar/background.png");

            try {
                imageGamer = ImageIO.read(fileGamer);
                imageWall = ImageIO.read(fileWall);
                imageBox = ImageIO.read(fileBox);
                imageGoal = ImageIO.read(fileGoal);
                imageError = ImageIO.read(fileError);
                imageGround = ImageIO.read(fileGround);
                imageBackground = ImageIO.read(fileBackground);

                setLayout(new BorderLayout());
                setBorder(new EmptyBorder(0, 0, 0, 0));
                setOpaque(false);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
        repaint();
    }
}

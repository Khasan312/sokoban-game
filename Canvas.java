import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class Canvas extends JPanel {
  private Model model;
  private Image imageGamer;
  private Image imageWall;
  private Image imageBox;
  private Image imageGoal;
  private Image imageError;
  private Image imageGround;
  private Image imageBackground;

  public Canvas(Model model) {
    this.model = model;
    setBackground(Color.BLACK);
    setOpaque(true);

    String skinState = model.modelSkinStateGetter();
    File fileGamer, fileWall, fileBox, fileGoal, fileError, fileGround, fileBackground;

    if (skinState.equals("morty")) {
      fileGamer = new File("images/morty/morty.png");
      fileWall = new File("images/morty/metalCenter7.png");
      fileBox = new File("images/morty/box3.png");
      fileGoal = new File("images/morty/goal.png");
      fileError = new File("images/error.png");
      fileGround = new File("images/morty/groundd.png");
      fileBackground = new File("images/background.png");
    } else if (skinState.equals("rick")) {
      fileGamer = new File("images/rick/rick1.png");
      fileWall = new File("images/rick/metalCenter2.png");
      fileBox = new File("images/rick/box.png");
      fileGoal = new File("images/rick/goal1.png");
      fileError = new File("images/error.png");
      fileGround = new File("images/rick/groundd.png");
      fileBackground = new File("images/background.png");
    }  else if(skinState.equals("aang")) {
      fileGamer = new File("images/avatar/skins/aang/aang.png");
      fileWall = new File("images/avatar/skins/aang/wall.png");
      fileBox = new File("images/avatar/skins/aang/box.png");
      fileGoal = new File("images/avatar/skins/aang/goal.png");
      fileError = new File("images/error.png");
      fileGround = new File("images/avatar/skins/aang/ground.png");
      fileBackground = new File("images/background.png");
    } else {
      return;
    }

    try {
      imageGamer = loadImage(fileGamer);
      imageWall = loadImage(fileWall);
      imageBox = loadImage(fileBox);
      imageGoal = loadImage(fileGoal);
      imageError = loadImage(fileError);
      imageGround = loadImage(fileGround);
      imageBackground = loadImage(fileBackground);

      setLayout(new BorderLayout());
      setBorder(new EmptyBorder(0, 0, 0, 0));
      setOpaque(false);
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  private BufferedImage loadImage(File file) throws IOException {
    return ImageIO.read(file);
  }

  public void paint(Graphics g) {
    super.paint(g);
    boolean state = model.getState();
    if (state) {
      drawSkins();
      drawDesktop(g);
    } else {
      drawErrorMessage(g);
    }
  }

  public int getMovesCount() {
    return model.getMovesCount();
  }

  public int getBoxMovesCount() {
    return model.getBoxMovesCount();
  }

  private void drawDesktop(Graphics g) {
    int[][] desktop = model.getDesktop();
    int start = 100;
    int x = start + 220;
    int y = start;
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
      x = start + 220;
      y = y + height + offset;
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(imageBackground, 0, 0, null);
  }

  private void drawErrorMessage(Graphics g) {
    Font font = new Font("Impact", Font.BOLD, 50);
    g.drawImage(imageError, 200, 200, null);
    g.setFont(font);
    g.setColor(Color.RED);
    g.drawString("Initialization Error!", 250, 100);
  }

  private void drawSkins() {
    String skinState = model.modelSkinStateGetter();
    String directory = skinState.equals("morty") ? "morty" : "rick";

    String[] imageFiles = {
            "morty.png", "metalCenter7.png", "box3.png", "goal.png", "error.png", "groundd.png", "background.png"
    };

    try {
      File[] files = new File[imageFiles.length];
      for (int i = 0; i < imageFiles.length; i++) {
        files[i] = new File("images/" + directory + "/" + imageFiles[i]);
      }

      imageGamer = ImageIO.read(files[0]);
      imageWall = ImageIO.read(files[1]);
      imageBox = ImageIO.read(files[2]);
      imageGoal = ImageIO.read(files[3]);
      imageError = ImageIO.read(files[4]);
      imageGround = ImageIO.read(files[5]);
      imageBackground = ImageIO.read(files[6]);

      setLayout(new BorderLayout());
      setBorder(new EmptyBorder(0, 0, 0, 0));
      setOpaque(false);
    } catch (IOException e) {
      System.out.println("Error: " + e);
    }
  }

  public Canvas getCanvas() {
    return this;
  }
}

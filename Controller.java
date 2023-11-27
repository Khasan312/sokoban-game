import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.List;

import javax.swing.JButton;

public class Controller implements KeyListener, MouseListener, ActionListener {

  private Model model;
  private Canvas canvas;
  private Music music;
  private List<JButton> buttons;

  private int elapsedTime = 0;

  public Controller(Viewer viewer) {
    model = new Model(viewer);
    canvas = new Canvas(model);
    canvas.addMouseListener(this);
    music = new Music();
  }

  public Model getModel() {
    return model;
  }

  public void keyPressed(KeyEvent event) {
    int keyCode = event.getKeyCode();
    String direction = "";

    switch (keyCode) {
      case KeyEvent.VK_LEFT:
        direction = "Left";
        break;
      case KeyEvent.VK_UP:
        direction = "Up";
        break;
      case KeyEvent.VK_RIGHT:
        direction = "Right";
        break;
      case KeyEvent.VK_DOWN:
        direction = "Down";
        break;
      case KeyEvent.VK_ESCAPE:
        direction = "EscapeToMenu";
        break;
      default:
        return;
    }

    model.move(direction);
    if (model.getUndoOrNot()) {
      model.clearRedo();
      if (model.isGamerAndBoxPositionEquals()) {
        model.getUndoMoves().push(getOppositeDirection(direction) + "Box");
        model.setNullPositions();
      } else {
        model.getUndoMoves().push(getOppositeDirection(direction));
      }
    }
  }

  private String getOppositeDirection(String direction) {
    switch (direction) {
      case "Left":
        return "Right";
      case "Right":
        return "Left";
      case "Up":
        return "Down";
      case "Down":
        return "Up";
      default:
        return "";
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    int mouseX = e.getX();
    int mouseY = e.getY();
    int width = 50;
    int height = 50;
    int startX = 320;
    int startY = 100;
    int offset = 0;

    int cellX = (mouseX - startX) / (width + offset);
    int cellY = (mouseY - startY) / (height + offset);

    if (model.isCellAccessible(cellY, cellX)) {
      model.moveCharacter(cellY, cellX);
    }
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    String command = event.getActionCommand();
    switch (command) {
      case "New_Game":
      case "skinChanger":
      case "Continue":
      case "Levels":
      case "Play_Online":
        model.initializeStateGame(command);
        break;
      case "Settings":
        model.showSettingsDialog();
      case "Resume_Game":
      case "Hints_Game":
      case "Settings_Game":
      case "Main_Menu_Game":
        model.pauseStateGame(command);
        break;
      case "Rick_Skin":
      case "Morty_Skin":
      case "Aang_Skin":
        model.changeSkinStateGame(command);
        break;
      case "level1":
      case "level2":
      case "level3":
      case "level4":
      case "level5":
      case "level6":
      case "level7":
      case "level8":
      case "level9":
      case "level10":
      case "Back":
        model.levelStateGame(command);
        break;
      case "undo":
        model.undo();
        break;
      case "redo":
        model.redo();
        break;
      case "Reset":
        model.clearStacks();
        model.resetDesktop();
        break;
      case "EscapeToMenu":
        model.openMenu();
        break;
      case "Exit":
        System.exit(0);
    }

  }

  public void keyTyped(KeyEvent event) {
  }

  public void keyReleased(KeyEvent event) {
  }

  @Override
  public void mouseEntered(MouseEvent arg0) {
//    JButton enteredButton = (JButton) arg0.getSource();
//    Color color = new Color(0, 102, 255);
//    enteredButton.setForeground(color);
//    enteredButton.setSize(new Dimension(265, 55));
  }

  @Override
  public void mouseExited(MouseEvent arg0) {
//    JButton enteredButton = (JButton) arg0.getSource();
//    enteredButton.setForeground(Color.BLACK);
//    enteredButton.setSize(new Dimension(250, 50));
  }

  @Override
  public void mousePressed(MouseEvent arg0) {
  }

  @Override
  public void mouseReleased(MouseEvent arg0) {
  }
}

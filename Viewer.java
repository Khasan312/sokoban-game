import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class Viewer {

  private GameMenu gameMenu;
  private SkinMenu skinMenu;
  private LevelMenu levelMenu;
  private SkinMenuGameField skinMenuGameField;
  private Canvas canvas;
  private Timer gameTimer;
  private int elapsedTime = 0;
  private int movesCount = 0;
  private JLabel label;
  private Controller controller;
  private JFrame frame;
  private MainMenu mainMenu;
  private Model model;
  private JButton undoButton;
  private JButton redoButton;
  private JButton resetButton;
  private JCheckBox autoSaveCheckbox;
  private JButton saveAutoButton;
  private JButton saveButton;
  private JLabel moves;
  private JButton pauseButton;

  private MultiplayerCanvas multiplayerCanvas;
  private JFrame multiplayerFrame;
  private String playerName;
  private JFrame enterYourName;

  public Viewer() {
    initializeYEnterYourName();
    initializeGameViewer();
    initializeMainMenu();
    initializeMultiplayerViewer();
  }

  public void initializeGameViewer() {
    controller = new Controller(this);
    model = controller.getModel();
    canvas = new Canvas(model);


    label = new JLabel(getTimeAndMoves());
    label.setHorizontalAlignment(SwingConstants.CENTER);
    Font labelFont = new Font("Arial", Font.BOLD, 22);
    label.setFont(labelFont);

    moves = new JLabel();
    Image image = Toolkit.getDefaultToolkit().createImage("images/ButtonIcons/moves.png");
    ImageIcon movesIcon = new ImageIcon(image);
    moves.setIcon(movesIcon);
    moves.setBounds(350, 1100, 70, 70);

    pauseButton = createMenuButton();
    undoButton = createUndoButton();
    redoButton = createRedoButton();
    resetButton = createResetButton();
    autoSaveCheckbox = createAutoSaveCheckbox();
    saveAutoButton = createAutoSaveButton();
    saveButton = createSaveButton();
    int delay = 10;
    gameTimer = new Timer(delay, new TimerListener());
    initializeFrame();

  }
  public void initializeYEnterYourName() {
    String name = JOptionPane.showInputDialog(enterYourName, "Enter your name:");
    if (name != null && !name.isEmpty()) {
      playerName = name;
      System.out.println("Entered name: " + name);
    } else {
      System.out.println("Name not entered or entered empty");
    }

  }

  public void initializeMultiplayerViewer() {
    multiplayerCanvas = new MultiplayerCanvas(controller.getModel());
    multiplayerFrame = new JFrame();
    multiplayerFrame.setTitle("Online game");
    multiplayerFrame.setSize(1200, 800);
    multiplayerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    multiplayerFrame.add("Center", multiplayerCanvas);
    multiplayerFrame.setSize(1200, 800);
    multiplayerFrame.addKeyListener(controller);
    multiplayerFrame.setVisible(false);
  }

  public void initializeFrame() {
    frame = new JFrame("Sokoban");
    frame.setSize(1200, 800);
    frame.add("South", label);
    frame.setLocation(200, 100);
    frame.add(undoButton);
    frame.add(redoButton);
    frame.add(resetButton);
    frame.add(autoSaveCheckbox);
    frame.add(saveAutoButton);
    frame.add(saveButton);
    frame.add(moves);
    frame.add(pauseButton);
    frame.add("Center", canvas);
    frame.setVisible(false);
    canvas.addKeyListener(controller);
    canvas.addMouseListener(controller);
    canvas.setFocusable(true);

    frame.setDefaultCloseOperation(3);
  }

  private JButton createUndoButton() {
    JButton undoButton = new JButton(new ImageIcon("images/ButtonIcons/undo.png"));
    undoButton.setBounds(530, 30, 70, 70);
    undoButton.setContentAreaFilled(false);
    undoButton.setBorderPainted(false);
    undoButton.setFocusable(false);
    undoButton.setActionCommand("undo");
    undoButton.addActionListener(controller);
    return undoButton;
  }

  private JButton createMenuButton() {
    JButton pauseButton = new JButton(new ImageIcon("images/ButtonIcons/pause.png"));
    pauseButton.setBounds(50, 30, 70, 70);
    pauseButton.setContentAreaFilled(false);
    pauseButton.setBorderPainted(false);
    pauseButton.setFocusable(false);
    pauseButton.setActionCommand("EscapeToMenu");
    pauseButton.addActionListener(controller);
    return pauseButton;
  }

  private JButton createRedoButton() {
    JButton redoButton = new JButton(new ImageIcon("images/ButtonIcons/redo.png"));
    redoButton.setBounds(620, 30, 70, 70);
    redoButton.setContentAreaFilled(false);
    redoButton.setBorderPainted(false);
    redoButton.setFocusable(false);
    redoButton.setActionCommand("redo");
    redoButton.addActionListener(controller);
    return redoButton;
  }

  private JButton createResetButton() {
    JButton resetButton = new JButton(new ImageIcon("images/ButtonIcons/reset2.png"));
    resetButton.setBounds(710, 30, 70, 70);
    resetButton.setContentAreaFilled(false);
    resetButton.setBorderPainted(false);
    resetButton.setFocusable(false);
    resetButton.setActionCommand("Reset");
    resetButton.addActionListener(controller);
    return resetButton;
  }

  private JCheckBox createAutoSaveCheckbox() {
    JCheckBox autoSaveCheckbox = new JCheckBox("Auto Save");
    autoSaveCheckbox.setBounds(350, 85, 85, 15);
    autoSaveCheckbox.setForeground(Color.WHITE);
    autoSaveCheckbox.setContentAreaFilled(false);
    autoSaveCheckbox.setBorderPainted(false);
    autoSaveCheckbox.setFocusable(false);

    Timer timer = new Timer(5000, new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        model.saveProgress();
      }
    });

    autoSaveCheckbox.addActionListener(e -> {
      if (autoSaveCheckbox.isSelected()) {
        timer.start();
      } else {
        timer.stop();
      }
    });

    return autoSaveCheckbox;
  }

  private JButton createAutoSaveButton() {
    JButton saveAutoButton = new JButton(new ImageIcon("images/ButtonIcons/autosave1.png"));
    saveAutoButton.setBounds(350, 30, 85, 55);
    saveAutoButton.setContentAreaFilled(false);
    saveAutoButton.setBorderPainted(false);
    saveAutoButton.setFocusable(false);
    saveAutoButton.addActionListener(e -> {
      System.out.println("Button saveAuto clicked");
      autoSaveCheckbox.doClick();
    });
    return saveAutoButton;
  }

  private JButton createSaveButton() {
    JButton saveButton = new JButton(new ImageIcon("images/ButtonIcons/save1.png"));
    saveButton.setBounds(440, 30, 70, 70);
    saveButton.setContentAreaFilled(false);
    saveButton.setBorderPainted(false);
    saveButton.setFocusable(false);
    saveButton.addActionListener(e -> {
      System.out.println("Button Save clicked");
      model.saveProgress();
    });
    return saveButton;
  }

  public void initializeMainMenu() {
    mainMenu = new MainMenu();
    mainMenu.setActionListener(controller);
    mainMenu.setMouseListener(controller);
    mainMenu.getFrame().addWindowListener(new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        model.getSettings().getMusicCheckBox().setSelected(true);
        model.getSettings().getSoundCheckBox().setSelected(true);
      }
    });
  }

  public GameMenu initializeResumeMenu() {
    gameMenu = new GameMenu(model);
    gameMenu.setActionListener(controller);
    return gameMenu;
  }

  public SkinMenu initializeSkinMenu() {
    skinMenu = new SkinMenu(model);
    skinMenu.setActionListener(controller);
    skinMenuGameField = skinMenu.getGameField();
    return skinMenu;
  }

  public LevelMenu initializeLevelMenu() {
    levelMenu = new LevelMenu(model);
    levelMenu.setActionListener(controller);
    return levelMenu;
  }

  public SkinMenuGameField mainSkinMenu() {
    skinMenuGameField.drawSkins();
    return skinMenuGameField;

  }

  public void startTimer() {
    if (!gameTimer.isRunning()) {
      gameTimer.start();
    }
  }

  private class TimerListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      elapsedTime++;
      label.setText(getTimeAndMoves());
      update();
    }
  }

  public String formatTime(int milliseconds) {
    int seconds = (int) (milliseconds / 60);
    int minutes = seconds / 60;
    int remainingSeconds = seconds % 60;
    int remainingMilliseconds = milliseconds % 60;
    return String.format("%02d:%02d.%02d", minutes, remainingSeconds, remainingMilliseconds);
  }

  public void stopTimer() {
    if (gameTimer.isRunning()) {
      gameTimer.stop();
    }
  }

  public void resetTimer() {
    elapsedTime = 0;
    movesCount = 0;
    gameTimer.start();
    label.setText(getTimeAndMoves());
    gameTimer.stop();
    canvas.repaint();
  }

  public String getTimeAndMoves() {
    return "Time : " + formatTime(elapsedTime) + "   Moves : " + canvas.getMovesCount() + "   Moving boxes: "
        + canvas.getBoxMovesCount();
  }

  public void update() {
    canvas.repaint();
  }

  public JLabel getLabel() {
    return label;
  }

  public int getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(int elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public JFrame getGameFrame() {
    return frame;
  }

  public MainMenu getMainMenu() {
    return mainMenu;
  }

  public GameMenu getGameMenu() {
    return gameMenu;
  }

  public LevelMenu getLevelMenu() {
    return levelMenu;
  }

  public Canvas getGameField() {
    return canvas;
  }

  public void updateSkin() {
    skinMenuGameField.drawSkins();
    skinMenuGameField.repaint();
  }

  public void removeMouseAndKeyListener() {
    canvas.removeMouseListener(controller);
    canvas.removeKeyListener(controller);
    undoButton.setEnabled(false);
    redoButton.setEnabled(false);
    resetButton.setEnabled(false);
  }

  public void addMouseAndKeyListener() {
    canvas.addMouseListener(controller);
    canvas.addKeyListener(controller);
    undoButton.setEnabled(true);
    redoButton.setEnabled(true);
    resetButton.setEnabled(true);
  }

  public void updateMultiplayerCanvas() {
    multiplayerCanvas.repaint();
  }

  public JFrame getMultiplayerFrame() {
    return multiplayerFrame;
  }

  public String getPlayerName() {
    return playerName;
  }
}

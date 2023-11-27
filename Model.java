import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.List;
import java.util.Queue;


public class Model {
  private Viewer viewer;
  private GameMenu gameMenu;
  private LevelMenu levelMenu;
  private int[][] desktop;
  private int[][] resetDesktop;
  private int indexX;
  private int indexY;
  private int[][] arrayOfGoals;
  private Levels repository;
  private boolean stateGame;
  private int movesCount;
  private Music music;
  private Timer timer;
  private int boxMovesCount;
  private String skinsState;
  private int countOfUndos;
  private boolean addGamer;
  private boolean inMainMenu;
  private int levelStatusForAutoGoal = -1;
  private final Stack<String> redoMoves;
  private final Stack<String> undoMoves;
  private int[] boxPosition;
  private int[] gamerPosition;
  private final Timer autoPlayTimer;
  private int currentAutoGoalStep;
  private long[] autoGoalDirections;
  private JCheckBox soundCheckBox;
  private JCheckBox musicCheckBox;
  private Settings settings;
  private String playerDirection;
  private boolean moved;
  private boolean undoOrNot;
  private boolean inOnlineGame = false;
  private int[][] secondDesktop;
  private Timer serverDataTimer;
  private SocketChannel socketChannelRequest;
  private final String playerName;


  public Model(Viewer viewer) {
    moved = false;
    playerDirection = "Down";
    skinsState = "rick";
    this.viewer = viewer;
    repository = new Levels();
    inMainMenu = true;
    music = new Music();
    undoMoves = new Stack<>();
    redoMoves = new Stack<>();
    autoPlayTimer = new Timer(50, e -> autoPlayStep());
    autoPlayTimer.setRepeats(true);
    playerName = viewer.getPlayerName();
    //playMusic();
    initialization();
    settings = new Settings();
    if (inOnlineGame) {
      serverDataTimer = new Timer(16, e -> {
        try {
          getDataFromServer();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      });
    }
  }

  private void initialization() {
    if(levelStatusForAutoGoal == 10) {
      levelStatusForAutoGoal = 0;
    }
    levelStatusForAutoGoal = levelStatusForAutoGoal + 1;

    stateGame = true;
    desktop = repository.nextLevel();
    resetDesktop = new int[desktop.length][];
    int counterOne = 0;
    int counterThree = 0;
    int counterFour = 0;
    undoOrNot = true;
    for (int i = 0; i < desktop.length; i++) {
      resetDesktop[i] = new int[desktop[i].length];
      for (int j = 0; j < desktop[i].length; j++) {
        if (desktop[i][j] == Options.GAMER) {
          counterOne = counterOne + 1;
          indexX = i;
          indexY = j;
        }
        if (desktop[i][j] == Options.BOX) {
          counterThree = counterThree + 1;
        }

        if (desktop[i][j] == Options.GOAL) {
          counterFour = counterFour + 1;
        }
        resetDesktop[i][j] = desktop[i][j];
      }
    }

    if ((counterOne != 1) || (counterThree == 0 && counterFour == 0) || (counterThree != counterFour)) {
      stateGame = false;
      desktop = null;
      return;
    }

    arrayOfGoals = new int[2][counterFour];

    int column = 0;
    for (int i = 0; i < desktop.length; i++) {
      for (int j = 0; j < desktop[i].length; j++) {
        if (desktop[i][j] == Options.GOAL) {
          arrayOfGoals[0][column] = i;
          arrayOfGoals[1][column] = j;
          column = column + 1;
        }
      }
    }
  }

  public void move(String direction) {
    int x = indexX;
    int y = indexY;
    if (!inMainMenu) {
      if (direction.equals("Left")) {
        moveLeft();
        if (inOnlineGame) {
          sendMovesToServer(desktop);
        }
        playerDirection = direction;
        moved = true;
        viewer.startTimer();
      } else if (direction.equals("Up")) {
        moveUp();
        if (inOnlineGame) {
          sendMovesToServer(desktop);
        }
        playerDirection = direction;
        moved = true;
        viewer.startTimer();
      } else if (direction.equals("Right")) {
        moveRight();
        if (inOnlineGame) {
          sendMovesToServer(desktop);
        }
        playerDirection = direction;
        moved = true;
        viewer.startTimer();
      } else if (direction.equals("Down")) {
        moveDown();
        if (inOnlineGame) {
          sendMovesToServer(desktop);
        }
        playerDirection = direction;
        moved = true;
        viewer.startTimer();
      } else if (direction.equals("EscapeToMenu")) {
        openMenu();
        viewer.stopTimer();
      } else {
        return;
      }
      check();
      addGamer = true;
      countOfUndos = countOfUndos + 1;
      viewer.update();
      won();
      if(x == indexX && y == indexY) {
        undoOrNot = false;
      } else {
        undoOrNot = true;
      }
    }
  }

  public void undo() {
    if (!undoMoves.isEmpty()) {
      String command = undoMoves.pop();
      if (command.equals("LeftBox")) {
        handleUndoGamerAndBoxPosition("Left");
        redoMoves.push("RightBox");
      } else if (command.equals("RightBox")) {
        handleUndoGamerAndBoxPosition("Right");
        redoMoves.push("LeftBox");
      } else if (command.equals("DownBox")) {
        handleUndoGamerAndBoxPosition("Down");
        redoMoves.push("UpBox");
      } else if (command.equals("UpBox")) {
        handleUndoGamerAndBoxPosition("Up");
        redoMoves.push("DownBox");
      } else if (command.equals("Left")) {
        redoMoves.add("Right");
      } else if (command.equals("Right")) {
        redoMoves.add("Left");
      } else if (command.equals("Up")) {
        redoMoves.add("Down");
      } else if (command.equals("Down")) {
        redoMoves.add("Up");
      }
      movesCount-=2;
      move(command);
      movesCount-=2;
    }
  }

  public void redo() {
    if (!redoMoves.isEmpty()) {
      String command = redoMoves.pop();
      if (command.equals("LeftBox")) {
        handleRedoGamerAndBoxPosition("Left");
        undoMoves.push("RightBox");
      } else if (command.equals("RightBox")) {
        handleRedoGamerAndBoxPosition("Right");
        undoMoves.push("LeftBox");
      } else if (command.equals("DownBox")) {
        handleRedoGamerAndBoxPosition("Down");
        undoMoves.push("UpBox");
      } else if (command.equals("UpBox")) {
        handleRedoGamerAndBoxPosition("Up");
        undoMoves.push("DownBox");
      } else if (command.equals("Left")) {
        undoMoves.add("Right");
      } else if (command.equals("Right")) {
        undoMoves.push("Left");
      } else if (command.equals("Up")) {
        undoMoves.push("Down");
      } else if (command.equals("Down")) {
        undoMoves.push("Up");
      }
      move(command);
    }
  }

  private void handleUndoGamerAndBoxPosition(String direction) {
    if (direction.equals("Right")) {
      desktop[indexX][indexY - 1] = Options.FLOOR;
      move(direction);
      desktop[indexX][indexY - 1] = Options.BOX;
    } else if (direction.equals("Left")) {
      desktop[indexX][indexY + 1] = Options.FLOOR;
      move(direction);
      desktop[indexX][indexY + 1] = Options.BOX;
    } else if (direction.equals("Down")) {
      desktop[indexX - 1][indexY] = Options.FLOOR;
      move(direction);
      desktop[indexX - 1][indexY] = Options.BOX;
    } else if (direction.equals("Up")) {
      desktop[indexX + 1][indexY] = Options.FLOOR;
      move(direction);
      desktop[indexX + 1][indexY] = Options.BOX;
    }
  }

  private void handleRedoGamerAndBoxPosition(String direction) {
    if (direction.equals("Right")) {
      desktop[indexX][indexY + 1] = Options.FLOOR;
      move(direction);
      desktop[indexX][indexY + 1] = Options.BOX;
    } else if (direction.equals("Left")) {
      desktop[indexX][indexY - 1] = Options.FLOOR;
      move(direction);
      desktop[indexX][indexY - 1] = Options.BOX;
    } else if (direction.equals("Down")) {
      desktop[indexX + 1][indexY] = Options.FLOOR;
      move(direction);
      desktop[indexX + 1][indexY] = Options.BOX;
    } else if (direction.equals("Up")) {
      desktop[indexX - 1][indexY] = Options.FLOOR;
      move(direction);
      desktop[indexX - 1][indexY] = Options.BOX;
    }
  }

  public void clearStacks() {
    undoMoves.clear();
    redoMoves.clear();
    resetMovesCount();
    resetBoxMovesCount();
  }

  public void clearRedo() {
    redoMoves.clear();
  }

  public boolean isGamerAndBoxPositionEquals() {
    if (gamerPosition == null || boxPosition == null) {
      return false;
    }
    return Arrays.equals(gamerPosition, boxPosition);
  }
  public Stack<String> getUndoMoves() {
    return undoMoves;
  }
  public void setNullPositions() {
    gamerPosition = null;
    boxPosition = null;
  }
  public void resetDesktop() {
    for (int i = 0; i < resetDesktop.length; i++) {
      for (int j = 0; j < resetDesktop[i].length; j++) {
        if (resetDesktop[i][j] == Options.GAMER) {
          indexX = i;
          indexY = j;
        }
        desktop[i][j] = resetDesktop[i][j];
      }
    }
    playerDirection = "Down";
  }

  public void resetMovesCount() {
    movesCount = 0;
  }

  public void resetBoxMovesCount() {
    boxMovesCount = 0;
  }

  public int getMovesCount() {
    return movesCount;
  }

  public int getBoxMovesCount() {
    return boxMovesCount;
  }

  public void setSoundCheckBox(JCheckBox soundCheckBox) {
    this.soundCheckBox = soundCheckBox;
    soundCheckBox.addActionListener(e -> toggleSound(soundCheckBox.isSelected()));
  }

  public void setMusicCheckBox(JCheckBox musicCheckBox) {
    this.musicCheckBox = musicCheckBox;
    musicCheckBox.addActionListener(e -> toggleMusic(musicCheckBox.isSelected()));
  }


  private void toggleSound(boolean enableSound) {
    if (enableSound) {
        music.setVolume(0.7f);
        music.play(music.load("/songs/win1.wav"));
    } else {
        music.stop();
        }
    }

  private void toggleMusic(boolean enableMusic) {
    if (enableMusic) {
        music.play(music.load("/songs/soundtrack.wav"));
    } else {
        music.stop();
    }
  }

  public void showSettingsDialog() {
    settings.showSettingsDialog();
  }

  private void won() {
    boolean isWon = true;
    for (int a = 0; a < arrayOfGoals[0].length; a++) {
      int i = arrayOfGoals[0][a];
      int j = arrayOfGoals[1][a];
      if (desktop[i][j] != Options.BOX) {
        isWon = false;
        break;
      }
    }

    if (isWon) {
      viewer.stopTimer();
      String message = "  Time : " + viewer.formatTime(viewer.getElapsedTime())
          + "  Moves : " + movesCount + "   Moving boxes: " + boxMovesCount;

      if (settings.getSoundCheckBox().isSelected()) {
        int volumeValue = settings.getVolumeSoundSlider().getValue();
        float normalizedVolume = volumeValue / 100.0f;
        music.play(music.load("/songs/win1.wav"));
        music.setVolume(normalizedVolume);
      }
      viewer.getLabel().setText(message);
      javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "You won!!!");

      viewer.getLabel().setText("  Time : " + viewer.formatTime(viewer.getElapsedTime()) + "  Moves : " + movesCount
          + "   Moving boxes: " + boxMovesCount);

      playerDirection = "Down";
      resetMovesCount();
      resetBoxMovesCount();
      initialization();
      viewer.removeMouseAndKeyListener();
      viewer.addMouseAndKeyListener();
      clearStacks();
      viewer.resetTimer();
      viewer.stopTimer();
      viewer.update();

    }
  }

  private void check() {
    for (int a = 0; a < arrayOfGoals[0].length; a++) {
      int i = arrayOfGoals[0][a];
      int j = arrayOfGoals[1][a];
      if (desktop[i][j] == Options.FLOOR) {
        desktop[i][j] = Options.GOAL;
        break;
      }
    }
  }
  public boolean isCellAccessible(int targetX, int targetY) {
    boolean[][] visited = new boolean[desktop.length][desktop[0].length];
    Queue<int[]> queue = new LinkedList<>();
    int[] start = { indexX, indexY };
    queue.add(start);
    visited[indexX][indexY] = true;

    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    while (!queue.isEmpty()) {
      int[] current = queue.poll();
      int x = current[0];
      int y = current[1];

      if (x == targetX && y == targetY) {
        return true;
      }

      for (int[] dir : directions) {
        int newX = x + dir[0];
        int newY = y + dir[1];

        if (isValidCoordinate(newX, newY) && !visited[newX][newY] && desktop[newX][newY] != Options.WALL
            && desktop[newX][newY] != Options.GOAL && desktop[newX][newY] != Options.BOX) {
          queue.add(new int[] { newX, newY });
          visited[newX][newY] = true;
        }
      }
    }
    return false;
  }

  private boolean isValidCoordinate(int x, int y) {
    return x >= 0 && x < desktop.length && y >= 0 && y < desktop[0].length;
  }

  public void openMenu() {
    if (gameMenu == null || !gameMenu.isVisible()) {
      gameMenu = viewer.initializeResumeMenu();
      gameMenu.setVisible(true);
    }
  }

  public void moveCharacter(int targetX, int targetY) {
    desktop[indexX][indexY] = Options.FLOOR;
    indexX = targetX;
    indexY = targetY;
    desktop[indexX][indexY] = Options.GAMER;
    viewer.update();
  }
  public int[][] getDesktop() {
    return desktop;
  }

  public boolean getState() {
    return stateGame;
  }

  public void initializeStateGame(String command) {
    if (command.equals("New_Game")) {
      repository.setLevel(1);
      initialization();
      LoadingWindow loadingWindow = new LoadingWindow(viewer);
      viewer.getMainMenu().getFrame().setVisible(false);
      inMainMenu = false;
      try {
        File file = new File("data_for_continue/playProgress.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.close();
      } catch (IOException ioe) {
        System.out.println(ioe + " in initializeStateGame method - New_Game");
      }

    } else if (command.equals("Play_Online")) {
      viewer.getMainMenu().getFrame().setVisible(false);
      viewer.getMultiplayerFrame().setVisible(true);
      secondDesktop = new int[][]{{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 0, 0, 1, 3, 0, 0, 4, 0, 2},
              {2, 0, 0, 0, 3, 0, 0, 4, 0, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 0, 0, 0, 0, 0, 0, 0, 0, 2},
              {2, 2, 2, 2, 2, 2, 2, 2, 2, 2}};

      requestOnlineGame();
      inMainMenu = false;
      inOnlineGame = true;
    } else if (command.equals("Continue")) {
      try {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("data_for_continue/playProgress.txt"));
        repository.setLevel(Integer.parseInt(bufferedReader.readLine()));
        LoadingWindow loadingWindow = new LoadingWindow(viewer);
        viewer.getMainMenu().getFrame().setVisible(false);
        inMainMenu = false;


        BufferedReader bufferedReader2 = new BufferedReader(new FileReader("data_for_continue/data.txt"));
        viewer.setElapsedTime(Integer.parseInt(bufferedReader2.readLine()));
        int elapsedTime = viewer.getElapsedTime();
        movesCount = Integer.parseInt(bufferedReader2.readLine());
        boxMovesCount = Integer.parseInt(bufferedReader2.readLine());
        viewer.getLabel().setText(viewer.getTimeAndMoves());
      }

      catch (IOException ioe) {
        System.out.println(ioe + " in initializeStateGame method - Continue");
      }

      stateGame = true;
      desktop = repository.continueLevel();
      resetDesktop = repository.nextLevel();
      int counterOne = 0;
      int counterThree = 0;
      int counterFour = 0;
      for (int i = 0; i < desktop.length; i++) {
        for (int j = 0; j < desktop[i].length; j++) {
          if (desktop[i][j] == Options.GAMER) {
            counterOne = counterOne + 1;
            indexX = i;
            indexY = j;
          }
          if (desktop[i][j] == Options.BOX) {
            counterThree = counterThree + 1;
          }

          if (resetDesktop[i][j] == Options.GOAL) {
            counterFour = counterFour + 1;
          }
        }
      }

      if (counterOne != 1) {
        stateGame = false;
        desktop = null;
        return;
      }
      arrayOfGoals = new int[2][counterFour];

      int column = 0;
      for (int i = 0; i < resetDesktop.length; i++) {
        for (int j = 0; j < resetDesktop[i].length; j++) {
          if (resetDesktop[i][j] == Options.GOAL) {
            arrayOfGoals[0][column] = i;
            arrayOfGoals[1][column] = j;
            column = column + 1;
          }
        }
      }
    }
    if (command.equals("skinChanger")) {
      viewer.initializeSkinMenu();
    }
    if (command.equals("Levels")) {
      viewer.getMainMenu().getFrame().setVisible(false);
      viewer.initializeLevelMenu();
    }
    if(command.equals("Exit")) {
      /*Window window = SwingUtilities.getWindowAncestor(viewer.getMainMenu());
      window.dispose();*/
    }
  }

  public void pauseStateGame(String command) {
    if (command.equals("Resume_Game")) {
      if (gameMenu.isVisible() || viewer.getGameMenu() != null) {
        gameMenu.setVisible(false);
      }
    } else if (command.equals("Hints_Game")) {
      level1(levelStatusForAutoGoal);
    } else if (command.equals("Settings_Game")) {
        showSettingsDialog();
    } else if (command.equals("Main_Menu_Game")) {
      viewer.getMainMenu().getFrame().setVisible(true);
      Window w = SwingUtilities.getWindowAncestor(viewer.getGameField().getCanvas());
      w.setVisible(false);
      gameMenu.setVisible(false);
      inMainMenu = true;
      resetCounters();
      playerDirection = "Down";
      viewer.getMainMenu().getFrame().dispose();
      viewer.initializeMainMenu();

    }
  }
  private void resetCounters() {
    resetMovesCount();
    resetBoxMovesCount();
    viewer.resetTimer();
  }

  public void changeSkinStateGame(String command) {
    if (command.equals("Rick_Skin")) {
      skinsStateRick();
    } else if (command.equals("Morty_Skin")) {
      skinsStateMorty();
    } else if(command.equals("Aang_Skin")){
      skinsStateAang();
    }
  }

  public void levelStateGame(String command) {
    if (command.equals("Back")) {
      viewer.getLevelMenu().getFrame().setVisible(false);
      viewer.getMainMenu().getFrame().setVisible(true);
    } else if (command.substring(0, 5).equals("level")) {
      repository.setLevel(Integer.parseInt(command.substring(5)));
      initialization();
      levelStatusForAutoGoal = Integer.parseInt(command.substring(5));
      viewer.getLevelMenu().getFrame().setVisible(false);
      LoadingWindow loadingWindow = new LoadingWindow(viewer);
      inMainMenu = false;
      try {
        File file = new File("data_for_continue/playProgress.txt");
        FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.close();
      } catch (IOException ioe) {
        System.out.println(ioe + " in levelStateGame method - level");
      }
    }
  }


  public void saveProgress() {
    try {
      FileWriter file = new FileWriter("data_for_continue/playProgress.txt");
      BufferedWriter bufferedWriter = new BufferedWriter(file);
      bufferedWriter.write(String.valueOf(repository.getLevel() - 1));
      bufferedWriter.newLine();
      for (int i = 0; i < desktop.length; i++) {
        for (int j = 0; j < desktop[i].length; j++) {
          bufferedWriter.write(String.valueOf(desktop[i][j]));
        }
        bufferedWriter.newLine();
      }
      bufferedWriter.flush();
      bufferedWriter.close();

      FileWriter file2 = new FileWriter("data_for_continue/data.txt");
      BufferedWriter bufferedWriter2 = new BufferedWriter(file2);
      bufferedWriter2.write(viewer.getElapsedTime() + "\n" + getMovesCount() + "\n" + getBoxMovesCount());
      bufferedWriter2.flush();
      bufferedWriter2.close();
    }
    catch (IOException ioe) {
        System.out.println(ioe + " in saveProgress method");
    }
  }

  public void skinsStateRick() {
    skinsState = "rick";
    viewer.updateSkin();
  }

  public void skinsStateMorty() {
    skinsState = "morty";
    viewer.updateSkin();
  }

  public void skinsStateAang() {
    skinsState = "aang";
    viewer.updateSkin();
  }

  public String modelSkinStateGetter() {
    return skinsState;
  }

  private long[] readAutoGoal(int AutoGoalLevel) {
    String filePath = "AutoGoal/level" + AutoGoalLevel + ".txt";
    List<Long> longList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numberStrings = line.split(",");
                for (String numberString : numberStrings) {
                    longList.add(Long.parseLong(numberString.trim()));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long[] longArray = new long[longList.size()];
        for (int i = 0; i < longList.size(); i++) {
            longArray[i] = longList.get(i);
        }
        return longArray;
  }

  private void level1(int level) {
    autoPlayTimer.stop();
    System.out.println(level);
    long[] level1 = readAutoGoal(level);
    currentAutoGoalStep = 0;
    autoGoalDirections = level1;

    gameMenu.setVisible(false);
    viewer.removeMouseAndKeyListener();
    resetDesktop();

    autoPlayTimer.start();
  }

  private void autoPlayStep() {
    if (currentAutoGoalStep < autoGoalDirections.length) {
      long direction = autoGoalDirections[currentAutoGoalStep];
      if (direction >= 1 && direction <= 4) {
        playDirection((int) direction);
      }
      currentAutoGoalStep++;
    } else {
      autoPlayTimer.stop();
    }
  }

  private void playDirection(int direction) {
    switch (direction) {
      case 1:
        moveLeft();
        break;
      case 2:
        moveRight();
        break;
      case 3:
        moveUp();
        break;
      case 4:
        moveDown();
        break;
    }
    viewer.update();
    won();
  }
  private void moveLeft() {
    if (desktop[indexX][indexY - 1] == Options.BOX) {
      if (desktop[indexX][indexY - 2] == Options.FLOOR || desktop[indexX][indexY - 2] == Options.GOAL) {
        desktop[indexX][indexY - 1] = Options.FLOOR;
        desktop[indexX][indexY - 2] = Options.BOX;
        boxPosition = new int[]{indexX, indexY + 1};
        gamerPosition = new int[]{indexX, indexY + 1};
        boxMovesCount++;
      }
    }

    if (desktop[indexX][indexY - 1] == Options.FLOOR || desktop[indexX][indexY - 1] == Options.GOAL) {
      if (resetDesktop[indexX][indexY] == 4) {
        desktop[indexX][indexY] = Options.GOAL;
        indexY = indexY - 1;
        desktop[indexX][indexY] = Options.GAMER;
      } else {
        desktop[indexX][indexY] = Options.FLOOR;
        indexY = indexY - 1;
        desktop[indexX][indexY] = Options.GAMER;
      }
      playSound();
      movesCount++;
    }
  }

  private void moveUp() {
    if (desktop[indexX - 1][indexY] == Options.BOX) {
      if (desktop[indexX - 2][indexY] == 0 || desktop[indexX - 2][indexY] == Options.GOAL) {
        desktop[indexX - 1][indexY] = Options.FLOOR;
        desktop[indexX - 2][indexY] = Options.BOX;
        boxPosition = new int[]{indexX, indexY + 1};
        gamerPosition = new int[]{indexX, indexY + 1};
        boxMovesCount++;
      }
    }

    if (desktop[indexX - 1][indexY] == 0 || desktop[indexX - 1][indexY] == Options.GOAL) {
      if (resetDesktop[indexX][indexY] == 4) {
        desktop[indexX][indexY] = Options.GOAL;
        indexX = indexX - 1;
        desktop[indexX][indexY] = Options.GAMER;
      } else {
        desktop[indexX][indexY] = Options.FLOOR;
        indexX = indexX - 1;
        desktop[indexX][indexY] = Options.GAMER;
      }
      playSound();
      movesCount++;
    }
  }

  private void moveRight() {

    if (desktop[indexX][indexY + 1] == Options.BOX) {
      if (desktop[indexX][indexY + 2] == 0 || desktop[indexX][indexY + 2] == Options.GOAL) {
        desktop[indexX][indexY + 1] = Options.FLOOR;
        desktop[indexX][indexY + 2] = Options.BOX;
        boxPosition = new int[]{indexX, indexY - 1};
        gamerPosition = new int[]{indexX, indexY - 1};
        boxMovesCount++;
      }
    }

    if (desktop[indexX][indexY + 1] == 0 || desktop[indexX][indexY + 1] == Options.GOAL) {
      if (resetDesktop[indexX][indexY] == 4) {
        desktop[indexX][indexY] = Options.GOAL;
        indexY = indexY + 1;
        desktop[indexX][indexY] = Options.GAMER;
      } else {
        desktop[indexX][indexY] = Options.FLOOR;
        indexY = indexY + 1;
        desktop[indexX][indexY] = Options.GAMER;
      }
      playSound();
      movesCount++;
    }
  }

  private void moveDown() {
    if (desktop[indexX + 1][indexY] == Options.BOX) {
      if (desktop[indexX + 2][indexY] == 0 || desktop[indexX + 2][indexY] == Options.GOAL) {
        desktop[indexX + 1][indexY] = Options.FLOOR;
        desktop[indexX + 2][indexY] = Options.BOX;
        boxPosition = new int[]{indexX, indexY - 1};
        gamerPosition = new int[]{indexX, indexY - 1};
        boxMovesCount++;
      }
    }

    if (desktop[indexX + 1][indexY] == Options.FLOOR || desktop[indexX + 1][indexY] == Options.GOAL) {
      if (resetDesktop[indexX][indexY] == 4) {
        desktop[indexX][indexY] = Options.GOAL;
        indexX = indexX + 1;
        desktop[indexX][indexY] = Options.GAMER;
      } else {
        desktop[indexX][indexY] = Options.FLOOR;
        indexX = indexX + 1;
        desktop[indexX][indexY] = Options.GAMER;
      }
      playSound();
      movesCount++;
    }
  }

  public Settings getSettings() {
    return settings;
  }

  public void playSound() {
    if (settings.getSoundCheckBox().isSelected()) {
        int volumeValue = settings.getVolumeSoundSlider().getValue();
        float normalizedVolume = volumeValue / 100.0f;
        music.play(music.load("/songs/movetowall.wav"));
        music.setVolume(normalizedVolume);
      }
  }
  public String getPlayerDirection() {
    return playerDirection;
  }

  public void setMovedBoolean(boolean moved) {
    this.moved = moved;
  }

  public boolean getMovedBoolean() {
    return moved;
  }

  public boolean getUndoOrNot() {
    return undoOrNot;
  }

  public int getDesktopWidth() {
    if (desktop == null || desktop.length == 0) {
      return 0;
    }
    return desktop[0].length; // Assuming all rows have the same length (width)
  }

  public int getDesktopHeight() {
    return desktop.length; // Height corresponds to the number of rows in the grid
  }

  public void sendMovesToServer(int[][] moves) {

    String movesString = "name:" + playerName + " " + "moves:" + arrayToString(moves);

    ByteBuffer buffer = ByteBuffer.wrap(movesString.getBytes());

    while (buffer.hasRemaining()) {
      try {
        socketChannelRequest.write(buffer);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private String arrayToString(int[][] array) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int[] row : array) {
      for (int value : row) {
        stringBuilder.append(value);
      }
      stringBuilder.append("Arr");
    }
    return stringBuilder.toString();
  }

  public void getDataFromServer() throws IOException {
    Selector selector = Selector.open();
    socketChannelRequest.configureBlocking(false);
    socketChannelRequest.register(selector, SelectionKey.OP_WRITE);

    int readyChannels = selector.selectNow();
    if (readyChannels > 0) {
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

      while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();
        if (key.isReadable()) {
          SocketChannel channel = (SocketChannel) key.channel();
          ByteBuffer readBuffer = ByteBuffer.allocate(1024);
          int bytesRead = channel.read(readBuffer);

          if (bytesRead > 0) {
            readBuffer.flip();
            byte[] data = new byte[bytesRead];
            readBuffer.get(data);

            String dataString = new String(data);
            System.out.println(dataString);
            if (!dataString.isEmpty()) {
              System.out.println();
              secondDesktop = convertStringToArray(dataString);
              viewer.updateMultiplayerCanvas();
            }
          } else if (bytesRead == -1) {
            key.cancel();
            channel.close();
          }
        }

        keyIterator.remove();
      }
    }
    selector.close();
  }


  public int[][] convertStringToArray(String input) {
    String[] parts = input.split("Arr");
    System.out.println(Arrays.toString(parts));
    int[][] resultArray = new int[parts.length - 1][];

    for (int i = 1; i < parts.length; i++) {
      String[] values = parts[i].split("");
      resultArray[i - 1] = new int[values.length];
      for (int j = 0; j < values.length; j++) {
        resultArray[i - 1][j] = Integer.parseInt(values[j]);
        System.out.println(Arrays.deepToString(resultArray));
      }
    }
    return resultArray;
  }

  public void requestOnlineGame() {
    try {
      socketChannelRequest = SocketChannel.open();
      socketChannelRequest.connect(new InetSocketAddress(Options.HOST_IP, Options.PORT));

      String request = "name:" + playerName + " " + "status:false";
      ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
      socketChannelRequest.write(buffer);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int[][] getSecondDesktop() {
    return secondDesktop;
  }
}

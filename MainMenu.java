import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class MainMenu {
    private JFrame frame;
    private MenuCanvas menuCanvas;
    private List<JButton> buttons = new ArrayList<>();
    private SkinsMenu skinsMenu;

    public MainMenu() {

        frame = new JFrame("Sokoban");
        menuCanvas = new MenuCanvas();
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        File file = new File("data_for_continue/playProgress.txt");
        if (file.length() != 0) {
            JButton continueGame = createButton("Continue", "Continue", 180, 290, 250, 50, true);
        }

        JButton newGame = createButton("New Game", "New_Game", 100, 350, 250, 50, true);
        JButton playOnline = createButton("Play Online", "Play_Online", 80, 410, 250, 50, true);
        JButton settings = createButton("Settings", "Settings", 60, 470, 250, 50, true);
        JButton levels = createButton("Levels", "Levels", 40, 530, 250, 50, true);
        JButton exit = createButton("Exit", "Exit", 20, 590, 250, 50, true);

        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // initialzeSkinsMenu();

            }
        });

        JButton hanger = createButton("", "skinChanger", 20, 680, 80, 80, false);
        hanger.setIcon(new ImageIcon("images/ButtonIcons/hanger2.png"));

        frame.add(menuCanvas, "Center");
        frame.setVisible(true);

        animateButton(newGame, 180);
        animateButton(playOnline, 180);
        animateButton(settings, 180);
        animateButton(levels, 180);
        animateButton(exit, 180);
    }

    private JButton createButton(String label, String actionCommand, int coordinateX, int coordinateY, int width,
            int height, boolean backColor) {
        JButton button = new JButton(label);
        Font buttonFont = new Font("Rockwell Condensed", Font.PLAIN, 25);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setFont(buttonFont);
        button.setSize(width, height);
        button.setFocusable(false);
        button.setActionCommand(actionCommand);
        button.setLocation(coordinateX, coordinateY);
        buttons.add(button);

        if (backColor) {
            int red = 0x61;
            int green = 0xfd;
            int blue = 0x77;
            Color customColor = new Color(red, green, blue);
            button.setBackground(customColor);
        } else {
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
        }

        frame.add(button);
        return button;
    }

    private void animateButton(JButton button, int targetX) {
        Timer timer = new Timer(10, new ActionListener() {
            int currentX = button.getX();

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentX < targetX) {
                    currentX += 5;
                    button.setLocation(currentX, button.getY());
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    public void setActionListener(ActionListener actionListener) {
        for (JButton button : buttons) {
            button.addActionListener(actionListener);
        }
    }

    public void setMouseListener(MouseListener mouseListener) {
        for (JButton button : buttons) {
            button.addMouseListener(mouseListener);
        }
    }

    public List<JButton> getButtons() {
        return buttons;
    }

    // public void initialzeSkinsMenu() {
    // skinsMenu = new SkinsMenu();
    // }

    public JFrame getFrame() {
        return frame;
    }

    public void addMouseListener(Controller controller) {
    }
}

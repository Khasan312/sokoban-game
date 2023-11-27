import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class GameMenu extends JFrame {
    private SkinsMenu skinsMenu;
    private Model model;

    private List<JButton> buttons = new ArrayList<>();

    public GameMenu(Model model) {
        super("Game Menu");
        this.model = model;
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        //120 170 220 270
        JButton resumeButton = createButton("Resume", 120, "Resume_Game");
        JButton hintsButton = createButton("Auto go level", 170, "Hints_Game");
        JButton settingsButton = createButton("Settings", 220, "Settings_Game");
//        settingsButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                initialzeSkinsMenu();
//            }
//        });
        JButton mainMenuButton = createButton("Main menu", 270, "Main_Menu_Game");

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("images/pausebg.png");
        JLabel background = new JLabel(backgroundImage);
        panel.add(background);
        add(panel);
    }

    private JButton createButton(String label, int coordinateY, String actionCommand) {

        JButton button = new JButton(label);
        Font buttonFont = new Font("Rockwell Condensed", Font.PLAIN, 25);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setFont(buttonFont);
        button.setSize(200, 40);
        button.setFocusable(false);
        button.setLocation(190, coordinateY);
        button.setActionCommand(actionCommand);
        buttons.add(button);

        int red = 0x61;
        int green = 0xfd;
        int blue = 0x77;
        Color customColor = new Color(red, green, blue);

        button.setBackground(customColor);

        add(button);
        return button;
    }

    public void setActionListener(ActionListener actionListener) {
        for (JButton button : buttons) {
            button.addActionListener(actionListener);
        }
    }

    public void initialzeSkinsMenu() {
        skinsMenu = new SkinsMenu(model);
    }

}

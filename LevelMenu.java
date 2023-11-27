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
import java.awt.Dimension;
import java.awt.BorderLayout;

public class LevelMenu extends JFrame {
    private SkinsMenu skinsMenu;
    private Model model;
    private Color customColor;
    private List<JButton> buttons = new ArrayList<>();

    public LevelMenu(Model model) {
        super("Level Menu");
        this.model = model;
        setSize(1080, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.BLACK);

        JButton level1 = createButton(panel, "Level1", 50, 20, "Level1");
        JButton level2 = createButton(panel, "level2", 300, 20, "level2");
        JButton level3 = createButton(panel, "level3", 550, 20, "level3");
        JButton level4 = createButton(panel, "level4", 800, 20, "level4");
        JButton level5 = createButton(panel, "level5", 50, 270, "level5");
        JButton level6 = createButton(panel, "level6", 300, 270, "level6");
        JButton level7 = createButton(panel, "level7", 550, 270, "level7");
        JButton level8 = createButton(panel, "level8", 800, 270, "level8");
        JButton level9 = createButton(panel, "level9", 550, 520, "level9");
        JButton level10 = createButton(panel, "level10", 800, 520, "level10");

        JButton buttonBack = new JButton("Back");
        Font buttonFont = new Font("Rockwell Condensed", Font.PLAIN, 25);
        buttonBack.setVerticalAlignment(SwingConstants.BOTTOM);
        buttonBack.setFont(buttonFont);
        buttonBack.setSize(250, 50);
        buttonBack.setFocusable(false);
        buttonBack.setActionCommand("Back");
        buttonBack.setLocation(50, 670);
        buttons.add(buttonBack);
        buttonBack.setBackground(customColor);

        for (JButton button : buttons) {
            panel.add(button);
        }

        add(panel);

        ImageIcon backgroundImage = new ImageIcon("images/levelbg.jpeg");
        JLabel background = new JLabel(backgroundImage);
        background.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        panel.add(background);
    }

    private JButton createButton(JPanel panel, String label,int coordinateX, int coordinateY, String actionCommand) {
        JButton button = new JButton();
        int levelNumber = Integer.parseInt(label.substring(5));
        ImageIcon buttonImage = loadImageForButton(levelNumber);
        button.setIcon(buttonImage);
        button.setPreferredSize(new Dimension(buttonImage.getIconWidth(), buttonImage.getIconHeight()));
        button.setBounds(coordinateX, coordinateY, buttonImage.getIconWidth(), buttonImage.getIconHeight());
        button.setFocusable(false);
        button.setLocation(coordinateX, coordinateY);
        button.setActionCommand(actionCommand);
        buttons.add(button);

        JLabel textLabel = new JLabel("Level " + levelNumber, SwingConstants.CENTER);
        textLabel.setForeground(Color.WHITE);
        textLabel.setBounds(coordinateX, coordinateY + buttonImage.getIconHeight() + 5, 100, 20);

        panel.add(textLabel);

        int red = 0x61;
        int green = 0xfd;
        int blue = 0x77;
        customColor = new Color(red, green, blue);

        button.setBackground(customColor);
        button.setOpaque(true);

        return button;
    }

    private ImageIcon loadImageForButton(int levelNumber) {
        String imagePath = "images/LevelImages/" + levelNumber + ".png";
        ImageIcon imageIcon = new ImageIcon(imagePath);
        return imageIcon;
    }

    public void setActionListener(ActionListener actionListener) {
        for (JButton button : buttons) {
            button.addActionListener(actionListener);
        }
    }

    public void initialzeSkinsMenu() {
        skinsMenu = new SkinsMenu(model);
    }
    public JFrame getFrame() {
        return this;
    }
}

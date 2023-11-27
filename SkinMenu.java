import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SkinMenu extends JFrame {

    private List<JButton> buttons = new ArrayList<>();
    private Model model;
    private SkinMenuGameField gameField;

    public SkinMenu(Model model) {
        super("Change Skin");
        this.model = model;
        setSize(1200, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setBackground(Color.GREEN);
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        gameField = new SkinMenuGameField(model); // Your existing game field panel

        JPanel skinChangePanel = new JPanel(new GridBagLayout());
        skinChangePanel.setPreferredSize(new Dimension(400, 800));
        skinChangePanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;

        JButton rickButton = createButton("Rick's skin", 200, "Rick_Skin");
        JButton mortyButton = createButton("Morty's skin", 270, "Morty_Skin");
/*        JButton spongeButton = createButton("Aang's skin", 200, "Aang_Skin");
        JButton patrickButton = createButton("Patric's skin", 270, "Patric_skin");
        JButton sonicButton = createButton("Sonic's skin", 200, "Sonic_Skin");*/

        mainPanel.add(gameField, BorderLayout.CENTER);
        mainPanel.add(skinChangePanel, BorderLayout.EAST);
        skinChangePanel.add(rickButton, gbc);
        skinChangePanel.add(mortyButton, gbc);
/*        skinChangePanel.add(spongeButton);
        skinChangePanel.add(patrickButton);
        skinChangePanel.add(sonicButton);*/
        getContentPane().add(mainPanel);
    }

    private JButton createButton(String label, int coordinateY, String actionCommand) {
        JButton button = new JButton(label);
        Font buttonFont = new Font("Broken Console", Font.PLAIN, 15);
        button.setFont(buttonFont);
        button.setForeground(Color.WHITE);
        button.setSize(160, 35);
        button.setFocusable(false);
        button.setLocation(900, coordinateY);
        button.setBackground(Color.BLUE);
        button.setActionCommand(actionCommand);

        buttons.add(button);
        return button;
    }

    public void setActionListener(ActionListener actionListener) {
        for (JButton button : buttons) {
            button.addActionListener(actionListener);
        }
    }

    public SkinMenuGameField getGameField() {
        return gameField;
    }

}

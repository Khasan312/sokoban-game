import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkinsMenu extends JFrame {
    private Model model;

    public SkinsMenu(Model model) {
        this.model = model;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setVisible(true);
        
        
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        JButton rick = createButton("images/rick/rick.png", 180, 200);
        rick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.skinsStateRick();
                System.out.println("SkinsMenuRick");

            }
        });
        add(rick);
        JButton morty = createButton("images/morty/morty.png", 320, 200);
        morty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.skinsStateMorty();
                System.out.println("SkinsMenuMorty");
            }
        });
        add(morty);

        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("images/skin.png");
        JLabel background = new JLabel(backgroundImage);
        panel.add(background);
        add(panel);
    }

    private JButton createButton(String imagepath, int coordinateX, int coordinateY) {
        JButton button = new JButton();
        button.setBounds(coordinateX, coordinateY, 60, 60);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        ImageIcon icon = new ImageIcon(imagepath);
        button.setIcon(icon);

        add(button);
        return button;
    }
}

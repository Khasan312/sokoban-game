import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomJOptionPane extends JFrame {

    CustomJOptionPane(){
        setSize(600, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);



    }

    private void initComponents() {
        ImageIcon image = new ImageIcon("images/MenuImages/win.png");
        JLabel icon = new JLabel(image);

        JPanel panel = new JPanel();
        panel.add(icon);

        JButton button = new JButton("Next level");
        Font buttonFont = new Font("Rockwell Condensed", Font.PLAIN, 25);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setFont(buttonFont);
        button.setSize(200, 40);
        button.setFocusable(false);
        button.setLocation(190, 320);

        int red = 0x61;
        int green = 0xfd;
        int blue = 0x77;
        Color customColor = new Color(red, green, blue);

        button.setBackground(customColor);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        add(button);

        add(panel);
    }
}

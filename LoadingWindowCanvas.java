import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class LoadingWindowCanvas extends JPanel {
    private Image backgroundImage;

    public LoadingWindowCanvas() {

        setLayout(new FlowLayout());
        try {
            backgroundImage = ImageIO.read(new File("images/loadingbg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton loading = new JButton("Loading...");
        loading.setFont(new Font("Rockwell Condensed", Font.PLAIN, 26));
        loading.setLocation(500, 550);
        loading.setFocusable(false);
        loading.setSize(230, 100);
        loading.setVerticalAlignment(SwingConstants.BOTTOM);
        loading.setBackground(Color.GREEN);
        loading.setOpaque(false);
        loading.setBorder(BorderFactory.createEmptyBorder());
        loading.setForeground(Color.WHITE);
        add(loading);

        JLabel portal = new JLabel();
        Image image = Toolkit.getDefaultToolkit().createImage("images/portal2.gif");
        ImageIcon portalIcon = new ImageIcon(image);
        portalIcon.setImageObserver(portal);
        portal.setIcon(portalIcon);
        portal.setBounds(270, 100, portalIcon.getIconWidth(), portalIcon.getIconHeight());
        setLayout(null);
        add(portal);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

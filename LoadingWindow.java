import javax.swing.*;
import java.awt.*;

public class LoadingWindow {
    private JFrame frame;
    public LoadingWindow(Viewer viewer) {
        LoadingWindowCanvas loadingWindowCanvas = new LoadingWindowCanvas();
        frame = new JFrame("Sokoban");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        Timer timer = new Timer(5000, e -> {
            frame.setVisible(false);
            viewer.getGameFrame().setVisible(true);
        });

        timer.setRepeats(false);
        timer.start();

        frame.add(loadingWindowCanvas, "Center");
    }

    public JFrame getFrame() {
        return frame;
    }

}

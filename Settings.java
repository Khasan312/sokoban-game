import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class Settings {
    private Preferences preferences;
    private JDialog dialog;
    private JCheckBox soundCheckBox;
    private JCheckBox musicCheckBox;
    private Music musicPlayer;
    private Music soundPlayer;
    private JSlider volumeMusicSlider;
    private JSlider volumeSoundSlider;
    private boolean isDialogVisible = false;
    private Model model;
    private Color customColor;
    private Timer timer;
    private int volumeValue;
    private float normalizedVolume;
    private int startVolume;

    public Settings() {
        int red = 0x61;
        int green = 0xfd;
        int blue = 0x77;
        startVolume = 98;
        customColor = new Color(red, green, blue);
        preferences = Preferences.userNodeForPackage(Settings.class);
        dialog = new JDialog();
        dialog.setTitle("Settings");
        dialog.setUndecorated(true);

        ImageIcon backgroundIcon = new ImageIcon("images/MenuImages/settings.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(null);

        musicPlayer = new Music();
        soundPlayer = new Music();
        soundCheckBox = new JCheckBox("Sound on", preferences.getBoolean("sound", true));
        musicCheckBox = new JCheckBox("Music on", preferences.getBoolean("music", true));

        JLabel musicLabel = createLabel("Music:");
        JLabel soundsLabel = createLabel("Sounds:");

        volumeMusicSlider = createSliders();
        volumeSoundSlider = createSliders();
        volumeMusicSlider = new JSlider(JSlider.HORIZONTAL, 20, 100, startVolume);
        volumeMusicSlider.setMajorTickSpacing(2);
        volumeMusicSlider.setMinorTickSpacing(1);
        volumeMusicSlider.setPaintTicks(false);
        volumeMusicSlider.setPaintLabels(false);
        volumeMusicSlider.setBackground(customColor);

        volumeSoundSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 98);
        volumeSoundSlider.setMajorTickSpacing(10);
        volumeSoundSlider.setMinorTickSpacing(1);
        volumeSoundSlider.setPaintTicks(false);
        volumeSoundSlider.setPaintLabels(false);
        volumeSoundSlider.setBackground(customColor);

        //soundCheckBox.setBounds(200, 50, 80, 30);
        //musicCheckBox.setBounds(200, 80, 80, 30);
        musicLabel.setBounds(250, 80, 130, 40);
        volumeMusicSlider.setBounds(160, 120, 250, 30);
        soundsLabel.setBounds(250, 160, 130, 40);
        volumeSoundSlider.setBounds(160, 200, 250, 30);
        JButton authorsButton = createButton("Authors", 190,250, 200, "authors");
        JButton okButton = createButton("OK", 130, 310, 100, "ok");
        JButton cancelButton = createButton("Cancel", 340, 310, 100, "cancel");

        volumeMusicSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                volumeValue = volumeMusicSlider.getValue();
                normalizedVolume = volumeValue / 100.0f;
                musicPlayer.setVolume(normalizedVolume);
            }
        });

        volumeSoundSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int volumeValue = volumeSoundSlider.getValue();
                float normalizedVolume = volumeValue / 100.0f;
                soundPlayer.setVolume(normalizedVolume);
            }
        });


        backgroundLabel.add(musicLabel);
        backgroundLabel.add(soundsLabel);
        soundCheckBox.setBackground(customColor);
        musicCheckBox.setBackground(customColor);
        backgroundLabel.add(soundCheckBox);
        backgroundLabel.add(musicCheckBox);
        backgroundLabel.add(volumeMusicSlider);
        backgroundLabel.add(volumeSoundSlider);
        backgroundLabel.add(authorsButton);
        backgroundLabel.add(okButton);
        backgroundLabel.add(cancelButton);
        dialog.setContentPane(backgroundLabel);

        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        authorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAuthorsDialog();
            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volumeValue = volumeMusicSlider.getValue();
                normalizedVolume = volumeValue / 100.0f;
                musicPlayer.setVolume(normalizedVolume);

                int volumeValue = volumeSoundSlider.getValue();
                float normalizedVolume = volumeValue / 100.0f;
                soundPlayer.setVolume(normalizedVolume);
                dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        soundCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                    boolean enableSound = soundCheckBox.isSelected();
                    preferences.putBoolean("sound", enableSound);
                }
            }
        });

        musicCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.DESELECTED) {
                    boolean enableMusic = musicCheckBox.isSelected();
                    preferences.putBoolean("music", enableMusic);
                    toggleMusic(enableMusic);
                }
            }
        });

        if (musicCheckBox.isSelected()) {
            toggleMusic(true);
        }
    }
    public JSlider getVolumeMusicSlider() {
        return volumeMusicSlider;
    }

    public JSlider getVolumeSoundSlider() {
        return volumeSoundSlider;
    }

    public void setModel(Model model) {
        this.model = model;
        model.setSoundCheckBox(soundCheckBox);
        model.setMusicCheckBox(musicCheckBox);
    }

    public void showSettingsDialog() {
        if (!isDialogVisible) {
            dialog.setVisible(true);
            isDialogVisible = true;
            dialog.addWindowListener(new WindowAdapter() {
              @Override
              public void windowActivated(WindowEvent e) {
                  isDialogVisible = true;
              }

              @Override
              public void windowDeactivated(WindowEvent e) {
                  isDialogVisible = false;
              }
          });
        }
    }

    public JCheckBox getSoundCheckBox() {
        return soundCheckBox;
    }

    public JCheckBox getMusicCheckBox() {
        return musicCheckBox;
    }

    public void toggleMusic(boolean enableMusic) {
        if (enableMusic) {
            playMusic();
        } else {
            musicPlayer.stop();

        }
    }

    private void toggleSound(boolean enableSound) {
        if (enableSound) {
            soundPlayer.load("/songs/win1.wav");
            soundPlayer.play();
        } else {
            soundPlayer.stop();
        }
    }
    private void playMusic() {
      musicPlayer.play(musicPlayer.load("/songs/soundtrack.wav"));
      int audioLength = musicPlayer.getAudioLength("/songs/soundtrack.wav");
      timer = new Timer(audioLength, e -> {
        musicPlayer.play(musicPlayer.load("/songs/soundtrack.wav"));
        musicPlayer.setVolume(normalizedVolume);
      });

      timer.start();
    }

    private void showAuthorsDialog() {
        ImageIcon gifIcon = new ImageIcon(getClass().getResource("/images/Authors.gif"));
        JLabel gifLabel = new JLabel(gifIcon);

        JPanel authorsPanel = new JPanel();
        authorsPanel.setLayout(new BorderLayout());
        authorsPanel.setBackground(customColor);
        authorsPanel.add(gifLabel, BorderLayout.CENTER);

        JDialog authorsDialog = new JDialog(dialog, "", true);
        authorsDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        authorsDialog.setContentPane(authorsPanel);
        authorsDialog.setSize(800, 400);
        authorsDialog.setLocationRelativeTo(dialog);
        authorsDialog.setVisible(true);
    }

    private JSlider createSliders() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 70);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        slider.setBackground(customColor);

        return slider;
    }
    private JLabel createLabel(String text){
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        Font labelsFont = new Font("Agency FB", Font.BOLD, 30);
        label.setFont(labelsFont);
        return label;
    }

    private JButton createButton(String label, int coordinateX, int coordinateY, int width, String actionCommand) {

        JButton button = new JButton(label);
        Font buttonFont = new Font("Rockwell Condensed", Font.PLAIN, 25);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
        button.setFont(buttonFont);
        button.setSize(width, 40);
        button.setFocusable(false);
        button.setLocation(coordinateX, coordinateY);
        button.setActionCommand(actionCommand);


        int red = 0x61;
        int green = 0xfd;
        int blue = 0x77;
        Color customColor = new Color(red, green, blue);

        button.setBackground(customColor);
        return button;
    }
}

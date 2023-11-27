import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Music {
    private Clip clip;
    public Clip load(String file) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource(file));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            System.out.println(e);
        }
        return clip;
    }

    public void play(Clip clip) {
        if (clip != null) {
            clip.start();
        }
    }
    public void play() {
       play(clip);
   }

   public void setClip(Clip clip) {
        this.clip = clip;
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

   public void stop() {
       if (clip != null) {
           clip.stop();
           clip.close();
       }
   }

   public void stop(Clip clip) {
       if (clip != null) {
           clip.stop();
           clip.setFramePosition(0);
           clip.close();
       }
   }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public int getAudioLength(String file) {
        double durationInSeconds = 0.0f;
      try {
          AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(file));
          long frames = audioInputStream.getFrameLength();
          durationInSeconds = (frames + 0.0) / audioInputStream.getFormat().getFrameRate();

      } catch (Exception e) {
          System.out.println(e);
      }
      return (int) Math.round(durationInSeconds * 1000.0);
    }
}

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music {
    File file = new File("C:\\Users\\Prabal Kuinkel\\Desktop\\4TH SEM BIT\\JAVA GUI\\Bouncy_Motor[JAVA]\\src\\Start.wav");
    Clip clip = AudioSystem.getClip();

    public Music() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        AudioInputStream audiostream = AudioSystem.getAudioInputStream(file);
        clip.open(audiostream);
    }

    void sound(boolean carf) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        if(carf==true) {
            if (!clip.isRunning()) {
                clip.setMicrosecondPosition(4*1000000); // Reset the playback position to the beginning
                clip.start();
            }
        }
        else
        {
            clip.stop();
        }
    }
}

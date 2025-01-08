/*Saya [Shidiq Arifin Sudrajat] mengerjakan evaluasi Tugas Masa Depan dalam mata kuliah
Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya
tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.
*/
package viewmodel;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private static Clip backgroundClip;
    private static Clip soundEffectClip;
    private static FloatControl backgroundVolumeControl;
    private static FloatControl soundEffectVolumeControl;

    public static void playBackgroundMusic(String filePath) {
        try {
            stopBackgroundMusic(); // Stop any currently playing background music
            backgroundClip = AudioSystem.getClip();
            URL audioUrl = SoundPlayer.class.getClassLoader().getResource(filePath);
            if (audioUrl != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
                backgroundClip.open(audioInputStream);
                backgroundVolumeControl = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(backgroundVolumeControl, 0.3f);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Could not find file: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    public static void playSoundEffect(String filePath) {
        try {
            stopSoundEffect(); // Stop any currently playing sound effect
            soundEffectClip = AudioSystem.getClip();
            URL audioUrl = SoundPlayer.class.getClassLoader().getResource(filePath);
            if (audioUrl != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioUrl);
                soundEffectClip.open(audioInputStream);
                soundEffectVolumeControl = (FloatControl) soundEffectClip.getControl(FloatControl.Type.MASTER_GAIN);
                setVolume(soundEffectVolumeControl, 0.2f);
                soundEffectClip.start();
            } else {
                System.err.println("Could not find file: " + filePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopSoundEffect() {
        if (soundEffectClip != null && soundEffectClip.isRunning()) {
            soundEffectClip.stop();
            soundEffectClip.close();
        }
    }

    private static void setVolume(FloatControl control, float volume) {
        if (control != null) {
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            control.setValue(dB);
        }
    }
}

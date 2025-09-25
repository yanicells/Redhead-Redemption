/**
The Sound class handles loading and playing audio files for game effects and music.
It provides methods to play, loop, and stop sounds.

@author Edrian Miguel E. Capistrano (240939)
@author Sofia Dion Y. Torres (244566)
@version May 20, 2025

I have not discussed the Java language code in my program
with anyone other than my instructor or the teaching assistants
assigned to this course.
I have not used Java language code obtained from another student,
or any other unauthorized source, either modified or unmodified.
If any Java language code or documentation used in my program
was obtained from another source, such as a textbook or website,
that has been clearly noted with a proper citation in the comments
of my program.
**/

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound {
    private File file;
    private Clip clip;
    private String fileName;

    /**
     * Constructs a Sound object with the specified file name.
     * Initializes the sound with the given file name.
     *
     * @param fileName the name of the audio file
     */
    public Sound(String fileName) {
        this.fileName = fileName;
        setUpFile();
    }

    /**
     * Sets up the audio file for playback.
     * This method loads the audio file and prepares it for playback.
     */
    public void setUpFile() {
        file = new File(fileName);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays the sound from the beginning.
     */
    public void play() {
        play(0);  
    }

    /**
     * Plays the sound from the specified start position.
     *
     * @param start the start position in microseconds
     */
    public void play(long start) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setMicrosecondPosition(start);
        clip.start();
    }

    /**
     * Loops the sound continuously.
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stops the sound playback.
     */
    public void stop() {
        clip.stop();
    }
}

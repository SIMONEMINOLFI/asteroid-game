package asteroid.services;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * Service to manage audio playback in the game.
 * Handles loading and playing sound effects for various game events.
 * Implemented as a singleton to ensure a single instance manages audio settings.
 */
public class AudioService {
    // Singleton instance
    private static final AudioService INSTANCE = new AudioService();

    // Audio file paths
    private static final String SHOOT_SOUND = "/sounds/shoot.wav";
    private static final String EXPLOSION_SOUND = "/sounds/explosion.wav";
    private static final String LOSE_LIFE_SOUND = "/sounds/lose_life.wav";
    private static final String GAME_OVER_SOUND = "/sounds/game_over.wav";
    private static final String POWERUP_SOUND = "/sounds/powerup.wav";

    private boolean audioEnabled = true; // Flag to enable/disable audio

    /**
     * Private constructor to prevent external instantiation.
     */
    private AudioService() {
        // Private constructor for singleton
    }

    /**
     * Gets the singleton instance of the AudioService.
     * @return The single instance of AudioService
     */
    public static AudioService getInstance() {
        return INSTANCE;
    }

    /**
     * Plays the shooting sound effect.
     */
    public void playShootSound() {
        if (audioEnabled) {
            playSound(SHOOT_SOUND);
        }
    }

    /**
     * Plays the asteroid explosion sound effect.
     */
    public void playExplosionSound() {
        if (audioEnabled) {
            playSound(EXPLOSION_SOUND);
        }
    }

    /**
     * Plays the lose life sound effect.
     */
    public void playLoseLifeSound() {
        if (audioEnabled) {
            playSound(LOSE_LIFE_SOUND);
        }
    }

    /**
     * Plays the game over sound effect.
     */
    public void playGameOverSound() {
        if (audioEnabled) {
            playSound(GAME_OVER_SOUND);
        }
    }

    /**
     * Plays the power-up activation sound effect.
     */
    public void playPowerupSound() {
        if (audioEnabled) {
            playSound(POWERUP_SOUND);
        }
    }

    /**
     * Generic method to play a sound from a resource path.
     * Uses Java Sound API to load and play audio clips.
     * @param soundFile The resource path of the sound file
     */
    private void playSound(String soundFile) {
        try {
            // Get sound resource
            URL soundURL = getClass().getResource(soundFile);
            if (soundURL == null) {
                System.err.println("Sound file not found: " + soundFile);
                return;
            }

            // Get and open an audio input stream
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundURL);

            // Get a clip resource
            Clip clip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enables or disables audio.
     * @param enabled true to enable audio, false to disable
     */
    public void setAudioEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }

    /**
     * Checks if audio is currently enabled.
     * @return true if audio is enabled, false otherwise
     */
    public boolean isAudioEnabled() {
        return audioEnabled;
    }
}

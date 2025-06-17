package com.kurabiye.kutd.view;

import com.kurabiye.kutd.model.Player.UserPreference;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicManager {
    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer combatPlayer;
    private MediaPlayer currentPlayer;
    private double volume = 0.5; // Default volume (50%)
    
    public MusicManager() {
         // Initialize volume from user preferences
        UserPreference prefs = UserPreference.getInstance();
        this.volume = prefs.getMusicVolume();
        loadMusic();
    }
    
    private void loadMusic() {
        try {
            // Load background music for gameplay
            Media backgroundMusic = new Media(getClass().getClassLoader()
                .getResource("assets/audio/background_music.mp3").toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
            backgroundMusicPlayer.setVolume(volume);
            
            // Load menu music
            Media combat = new Media(getClass().getClassLoader()
                .getResource("assets/audio/combat.mp3").toExternalForm());
            combatPlayer = new MediaPlayer(combat);
            combatPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            combatPlayer.setVolume(volume);
            
        } catch (Exception e) {
            System.err.println("Failed to load music files: " + e.getMessage());
        }
    }
    
    public void playBackgroundMusic() {
        stopCurrentMusic();
        if (backgroundMusicPlayer != null) {
            currentPlayer = backgroundMusicPlayer;
            backgroundMusicPlayer.play();
        }
    }
    
    public void playcombat() {
        stopCurrentMusic();
        if (combatPlayer != null) {
            currentPlayer = combatPlayer;
            combatPlayer.play();
        }
    }
    
    public void pauseMusic() {
        if (currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            currentPlayer.pause();
        }
    }
    
    public void resumeMusic() {
        if (currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            currentPlayer.play();
        }
    }
    
    public void stopCurrentMusic() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }
    
    public void setVolume(double volume) {
        this.volume = Math.max(0.0, Math.min(1.0, volume)); // Clamp between 0 and 1
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setVolume(this.volume);
        }
        if (combatPlayer != null) {
            combatPlayer.setVolume(this.volume);
        }

        // Update user preferences to persist the volume setting
        UserPreference current = UserPreference.getInstance();
        UserPreference.Builder builder = new UserPreference.Builder(current)
            .setMusicVolume((float) this.volume);
        UserPreference.applySettings(builder);
    }
    
    public double getVolume() {
        return volume;
    }
    
    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
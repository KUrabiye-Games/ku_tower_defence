package com.kurabiye.kutd.view;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private static final String GOLD_SOUND_PATH = "/assets/audio/gold.mp3";
    private static AudioClip goldSound;

    static {
        try {
            goldSound = new AudioClip(SoundManager.class.getResource(GOLD_SOUND_PATH).toExternalForm());
        } catch (Exception e) {
            System.err.println("Failed to load gold sound: " + e.getMessage());
        }
    }

    public static void playGoldSound() {
        if (goldSound != null) {
            goldSound.play();
        }
    }
}

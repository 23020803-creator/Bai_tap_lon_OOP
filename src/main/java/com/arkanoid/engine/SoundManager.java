package com.arkanoid.engine;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp quản lý toàn bộ âm thanh (nhạc nền & hiệu ứng) trong game Arkanoid.
 * Sử dụng JavaFX Media API để phát nhạc và hiệu ứng.
 */
public class SoundManager {
    private static final Map<String, MediaPlayer> bgmMap = new HashMap<>();
    private static final Map<String, AudioClip> sfxMap = new HashMap<>();

    /**
     *  Phát nhạc nền (BGM). Nếu loop = true thì phát lặp vô hạn.
     */
    public static void playBGM(String fileName, boolean loop) {
        stopBGM(fileName); // Dừng nếu đang phát lại file đó

        URL url = SoundManager.class.getResource("/sound/" + fileName);
        if (url == null) {
            System.out.println("[WARN] Không tìm thấy BGM: " + fileName);
            return;
        }

        Media media = new Media(url.toString());
        MediaPlayer player = new MediaPlayer(media);
        if (loop) player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.7);
        player.play();

        bgmMap.put(fileName, player);
    }

    /**
     *  Phát âm thanh hiệu ứng ngắn (SFX).
     */
    public static void playSFX(String fileName) {
        URL url = SoundManager.class.getResource("/sound/" + fileName);
        if (url == null) {
            System.out.println("[WARN] Không tìm thấy SFX: " + fileName);
            return;
        }

        AudioClip clip = new AudioClip(url.toString());
        clip.play();
        sfxMap.put(fileName, clip);
    }

    /**
     *  Dừng phát âm nhạc.
     */
    public static void stopBGM(String fileName) {
        MediaPlayer player = bgmMap.remove(fileName);
        if (player != null) player.stop();
    }

    /**
     *  Dừng toàn bộ âm nhạc đang phát.
     */
    public static void stopAllBGM() {
        for (MediaPlayer player : bgmMap.values()) {
            if (player != null) player.stop();
        }
        bgmMap.clear();
    }

    public static void setBGMVolume(double volume) {
        for (MediaPlayer player : bgmMap.values()) {
            player.setVolume(volume);
        }
    }

    public static void setSFXVolume(double volume) {
        for (AudioClip sfx : sfxMap.values()) {
            sfx.setVolume(volume);
        }
    }
}
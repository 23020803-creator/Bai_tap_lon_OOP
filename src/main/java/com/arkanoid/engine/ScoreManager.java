package com.arkanoid.engine;

import java.io.*;
import java.util.*;

public final class ScoreManager {
    private static final String SCORE_FILE =
            System.getProperty("user.home") + File.separator + "arkanoid_scores.txt";
    private static final int MAX_SCORES = 10;

    private static int currentScore = 0;

    /** Reset điểm khi bắt đầu game mới */
    public static void resetScore() {
        currentScore = 0;
    }

    /** Cộng điểm */
    public static void add(int points) {
        currentScore += points;
    }

    public static int getCurrentScore() {
        return currentScore;
    }

    /** Lưu điểm nếu nằm trong top */
    public static void saveScoreIfHigh() {
        try {
            List<Integer> scores = loadScores();
            scores.add(currentScore);
            scores.sort(Collections.reverseOrder());
            if (scores.size() > MAX_SCORES)
                scores = scores.subList(0, MAX_SCORES);

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE))) {
                for (int s : scores) {
                    bw.write(String.valueOf(s));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi lưu điểm: " + e.getMessage());
        }
    }

    /** Đọc danh sách top điểm */
    public static List<Integer> loadScores() {
        List<Integer> scores = new ArrayList<>();
        File f = new File(SCORE_FILE);
        if (!f.exists()) return scores;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    scores.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException ignored) {}
        scores.sort(Collections.reverseOrder());
        return scores;
    }
}

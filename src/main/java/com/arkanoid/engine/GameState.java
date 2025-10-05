package com.arkanoid.engine;

/**
 * Biểu diễn các trạng thái của trò chơi.
 */
public enum GameState {
    MENU,       // Màn hình menu chính, chờ người chơi bắt đầu
    RUNNING,    // Game đang chạy bình thường
    PAUSED,     // Game đang tạm dừng
    GAME_OVER,  // Người chơi thua (hết mạng)
    WIN         // Người chơi thắng (phá hết gạch)
}
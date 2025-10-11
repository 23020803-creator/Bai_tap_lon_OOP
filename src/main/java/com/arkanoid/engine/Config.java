package com.arkanoid.engine;

/**
 * Lớp chứa các hằng số cấu hình của game.
 * <p>
 * Mọi thông số về kích thước, tốc độ, số mạng... đều được định nghĩa ở đây
 * để dễ dàng thay đổi và quản lý.
 */
public final class Config {
    private Config() {
    }

    // Kích thước cửa sổ hiển thị
    public static final int VIEW_WIDTH = 800;
    public static final int VIEW_HEIGHT = 600;

    // Cấu hình thanh trượt (Paddle)
    public static final int PADDLE_WIDTH = 120;
    public static final int PADDLE_HEIGHT = 16;
    public static final int PADDLE_SPEED = 5;

    // Cấu hình quả bóng (Ball)
    public static final int BALL_SIZE = 12;
    public static final int BALL_SPEED = 3;

    // Cấu hình khối gạch (Brick)
    public static final int BRICK_ROWS = 6;
    public static final int BRICK_COLS = 10;
    public static final int BRICK_WIDTH = 70;
    public static final int BRICK_HEIGHT = 24;
    public static final int BRICK_GAP = 4;

    // Thời gian hiệu lực của powerup
    public static final int DURATION_PER_POWERUP = 60 * 4;

    // Số mạng ban đầu của người chơi
    public static final int START_LIVES = 3;

    // Số mạng tối đa
    public static final int MAX_LIVES = 5;
}
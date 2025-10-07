package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp tăng tốc bóng tạm thời (Fast Ball).
 * - Khi người chơi ăn được, tốc độ bóng tăng thêm +2.
 * - Hiệu ứng kéo dài trong 6 giây (ở 60 FPS).
 * - Khi hết thời gian, tốc độ bóng sẽ trở lại như ban đầu.
 */
public final class FastBallPowerUp extends PowerUp {
    private static final int DURATION = 60 * 6; // Thời gian hiệu lực: 6 giây
    private double originalSpeed; // Lưu tốc độ gốc của bóng

    /**
     * Khởi tạo PowerUp tăng tốc bóng.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng hiển thị của vật phẩm
     * @param h chiều cao hiển thị của vật phẩm
     */
    public FastBallPowerUp(double x, double y, double w, double h) {
        super(x, y, w, h, PowerUpType.FAST_BALL, DURATION);
    }

    /**
     * Áp dụng hiệu ứng: tăng tốc độ bóng lên +2.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (ball == null) return; // Nếu chưa có bóng thì bỏ qua
        originalSpeed =  ball.getSpeed();   // Lưu lại tốc độ gốc
        ball.setSpeed(originalSpeed + 2);  // Tăng tốc độ bóng
        if (paddle != null) paddle.applyPowerUp(this);
    }

    /**
     * Gỡ hiệu ứng khi hết thời gian: đặt lại tốc độ bóng về giá trị ban đầu.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (ball != null && originalSpeed > 0) {
            ball.setSpeed(originalSpeed);
        }
    }
}

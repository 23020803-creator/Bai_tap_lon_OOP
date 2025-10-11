package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp tăng tốc bóng tạm thời (Fast Ball).
 * - Khi người chơi ăn được, tốc độ bóng tăng thêm +2.
 * - Hiệu ứng kéo dài trong 4 giây (ở 60 FPS).
 * - Khi hết thời gian, tốc độ bóng sẽ trở lại như ban đầu.
 * - Có thể cộng dồn hiệu ứng (max = basespeed + 4) và thời gian (vô hạn)
 */
public final class FastBallPowerUp extends PowerUp {

    /**
     * Khởi tạo PowerUp tăng tốc bóng.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng hiển thị của vật phẩm
     * @param h chiều cao hiển thị của vật phẩm
     */
    public FastBallPowerUp(double x, double y, double w, double h) {
        super(x, y, w, h, PowerUpType.FAST_BALL);
    }

    /**
     * Áp dụng hiệu ứng: tăng tốc độ bóng lên +2.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (ball != null) {
            ball.activateFastBallEffect();
        }
    }
}

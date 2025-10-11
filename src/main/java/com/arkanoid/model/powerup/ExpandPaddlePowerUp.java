package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp mở rộng Paddle (Expand Paddle).
 * - Khi người chơi ăn được, thanh Paddle sẽ dài ra (x1.5 lần).
 * - Hiệu ứng chỉ tồn tại trong một khoảng thời gian giới hạn (4 giây ở 60 FPS).
 * - Khi hết hạn, Paddle trở lại chiều rộng ban đầu.
 * - Có thể cộng dồn hiệu ứng (max = paddle x2.25 lần) và thời gian (vô hạn).
 */
public final class ExpandPaddlePowerUp extends PowerUp {

    /**
     * Khởi tạo PowerUp mở rộng Paddle.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng của vật phẩm hiển thị
     * @param h chiều cao của vật phẩm hiển thị
     */
    public ExpandPaddlePowerUp(double x, double y, double w, double h) {
        super( x, y, w, h, PowerUpType.EXPAND_PADDLE);
    }

    /**
     * Áp dụng hiệu ứng: tăng chiều rộng Paddle lên 1.5 lần, tối đa 2.25 lần.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (ball != null) {
            paddle.activateExpandEffect();
        }
    }
}

package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp mở rộng Paddle (Expand Paddle).
 * - Khi người chơi ăn được, thanh Paddle sẽ dài ra (x1.5 lần).
 * - Hiệu ứng chỉ tồn tại trong một khoảng thời gian giới hạn (8 giây ở 60 FPS).
 * - Khi hết hạn, Paddle trở lại chiều rộng ban đầu.
 */
public final class ExpandPaddlePowerUp extends PowerUp {
    private static final int DURATION = 60 * 8; // Thời gian hiệu lực: 8 giây
    private double originalWidth; // Lưu lại chiều rộng gốc của Paddle

    /**
     * Khởi tạo PowerUp mở rộng Paddle.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng của vật phẩm hiển thị
     * @param h chiều cao của vật phẩm hiển thị
     */
    public ExpandPaddlePowerUp(double x, double y, double w, double h) {
        super( x, y, w, h, PowerUpType.EXPAND_PADDLE, DURATION);
    }

    /**
     * Áp dụng hiệu ứng: tăng chiều rộng Paddle lên 1.5 lần.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        if (originalWidth == 0) { // chỉ lưu lần đầu
            originalWidth = paddle.getWidth();
        }
        paddle.setWidth((int) (originalWidth * 1.5));
        paddle.applyPowerUp(this);
    }

    /**
     * Gỡ hiệu ứng khi hết thời gian: đặt lại Paddle về chiều rộng gốc.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {
        if (paddle != null && originalWidth > 0) {
            paddle.setWidth(originalWidth);
        }
    }
}

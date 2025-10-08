package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp giúp người chơi nhận thêm 1 mạng khi ăn.
 * Không có hiệu ứng tạm thời, chỉ tác động 1 lần khi chạm Paddle.
 */
public final class ExtraLifePowerUp extends PowerUp {

    /**
     * Khởi tạo PowerUp cộng thêm mạng.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng hiển thị của vật phẩm
     * @param h chiều cao hiển thị của vật phẩm
     */
    public ExtraLifePowerUp(double x, double y, double w, double h) {
        // Truyền type = EXTRA_LIFE và thời gian hiệu lực = 0 (vì không cần duy trì)
        super(x, y, w, h, PowerUpType.EXTRA_LIFE, 0);
    }

    /**
     * Áp dụng hiệu ứng: cộng thêm 1 mạng cho người chơi khi Paddle chạm.
     * (Hiệu ứng xảy ra tức thời, không có thời gian duy trì)
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {
        markApplied(); // Đánh dấu hiệu ứng đã áp dụng
    }

    /**
     * Không cần gỡ hiệu ứng vì đây là PowerUp tức thời.
     */
    @Override
    public void removeEffect(Paddle paddle, Ball ball) {}
}

package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

/**
 * PowerUp giúp người chơi nhận thêm 1 mạng khi ăn.
 * Không có hiệu ứng tạm thời, chỉ tác động 1 lần khi chạm Paddle.
 * Tối đa 5 mạng.
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
        super(x, y, w, h, PowerUpType.EXTRA_LIFE);
    }

    /**
     * Áp dụng hiệu ứng: cộng thêm 1 mạng cho người chơi khi Paddle chạm vật phẩm.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball) {}
}

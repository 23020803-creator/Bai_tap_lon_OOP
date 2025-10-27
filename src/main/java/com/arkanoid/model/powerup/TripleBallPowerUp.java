package com.arkanoid.model.powerup;

import com.arkanoid.engine.GameManager;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;

public class TripleBallPowerUp extends PowerUp {
    public TripleBallPowerUp(double x, double y, double w, double h) {
        super(x, y, w, h, PowerUpType.TRIPLE_BALL);
    }

    // Không dùng hàm cũ (Paddle, Ball) cho case này — ta sẽ gọi hàm đặc thù từ GameManager
    @Override
    public void applyEffect(Paddle paddle, Ball mainBall) {
        // no-op: ta sẽ xử lý ở GameManager bằng hàm đặc thù có tham chiếu GameManager.
    }

    // Hàm đặc thù cho TripleBall để GameManager gọi
    public void apply(GameManager gm, Ball reference) {
        gm.spawnClonedBalls(reference, 2); // nhân thêm 2 bóng từ bóng tham chiếu
    }
}

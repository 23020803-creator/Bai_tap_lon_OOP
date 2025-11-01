package com.arkanoid.model.powerup;

import com.arkanoid.engine.GameManager;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import java.util.ArrayList;
import java.util.List;

public class TripleBallPowerUp extends PowerUp {
    public TripleBallPowerUp(double x, double y, double w, double h) {
        super(x, y, w, h, PowerUpType.TRIPLE_BALL);
    }

    @Override
    public void applyEffect(Paddle paddle, Ball mainBall) {}

    public void apply(GameManager gm, Ball reference) {
        if (gm == null || reference == null) {
            return;
        }

        List<Ball> existingBalls = new ArrayList<>(gm.getBalls());
        for (Ball refBall : existingBalls) {
            gm.spawnClonedBalls(refBall, 2);  // Mỗi bóng hiện tại nhân ra thành 3 bóng
        }

        // Tất cả bóng sinh ra đều như nhau
        double speed = reference.getSpeed();
        for (Ball b : gm.getBalls()) {
            b.setSpeed(speed);
        }
    }
}

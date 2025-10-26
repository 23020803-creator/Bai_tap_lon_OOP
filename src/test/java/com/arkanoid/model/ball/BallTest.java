package com.arkanoid.model.ball;

import com.arkanoid.engine.Config;
import com.arkanoid.model.paddle.Paddle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Bộ kiểm thử lớp Ball:
 * - Kiểm tra reset vị trí trên paddle sau khi mất mạng.
 * - Kiểm tra phản xạ ngang, dọc và khi va chạm paddle.
 * - Kiểm tra va chạm bằng AABB.
 * - Kiểm tra update khi có hiệu ứng tăng tốc.
 * - Kiểm tra kích hoạt / reset hiệu ứng tăng tốc bóng.
 */
class BallTest {

    /**
     * Kiểm tra resetOnPaddle(): bóng nằm giữa paddle và phía trên nó.
     */
    @Test
    void resetOnPaddle() {
        Paddle paddle = new Paddle(100, 500, 80, 14, 6);
        Ball ball = new Ball(0, 0, 10, 10, 5);
        ball.resetOnPaddle(paddle);
        // x: giữa paddle
        assertEquals(paddle.getCenterX() - ball.getWidth() / 2, ball.getX(), 1e-6);
        // y: ngay trên paddle
        assertEquals(paddle.getY() - ball.getHeight() - 2, ball.getY(), 1e-6);
    }

    /**
     * Kiểm tra bounceHorizontal(): đảo hướng ngang.
     */
    @Test
    void bounceHorizontal() {
        Ball ball = new Ball(0, 0, 10, 10, 5);
        double beforeDx = ball.dx;
        ball.bounceHorizontal();
        assertEquals(-beforeDx, ball.dx, 1e-6);
    }

    /**
     * Kiểm tra bounceVertical(): đảo hướng dọc.
     */
    @Test
    void bounceVertical() {
        Ball ball = new Ball(0, 0, 10, 10, 5);
        double beforeDy = ball.dy;
        ball.bounceVertical();
        assertEquals(-beforeDy, ball.dy, 1e-6);
    }

    /**
     * Kiểm tra phản xạ theo vị trí va chạm trên paddle.
     * - Nếu bóng chạm giữa paddle, đi lên.
     * - Nếu chạm trái, dx âm. Nếu chạm phải, dx dương.
     */
    @Test
    void bounceOff() {
        Paddle paddle = new Paddle(100, 500, 80, 14, 6);
        Ball ball = new Ball(0, 0, 10, 10, 5);
        // Đặt bóng ngay giữa paddle
        ball.setX(paddle.getCenterX() - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() - 2);
        ball.bounceOff(paddle);
        assertTrue(ball.dy < 0);

        // Chạm bên trái paddle
        ball.setX(paddle.getX() - 10);
        ball.bounceOff(paddle);
        assertTrue(ball.dx < 0);

        // Chạm bên phải paddle
        ball.setX(paddle.getX() + paddle.getWidth() + 10);
        ball.bounceOff(paddle);
        assertTrue(ball.dx > 0);
    }

    /**
     * Kiểm tra checkCollision(): giữa Ball và Paddle.
     */
    @Test
    void checkCollision() {
        Paddle paddle = new Paddle(100, 500, 80, 14, 6);
        Ball ball = new Ball(120, 491, 10, 10, 5);
        assertTrue(ball.checkCollision(paddle));

        ball.setX(300);
        ball.setY(50);
        assertFalse(ball.checkCollision(paddle));
    }

    /**
     * Kiểm tra update(): đếm ngược thời gian hiệu ứng tăng tốc và reset lại khi hết thời gian.
     */
    @Test
    void update() {
        Ball ball = new Ball(0, 0, 10, 10, 5);
        double baseSpeed = ball.getSpeed();

        ball.activateFastBallEffect();
        ball.activateFastBallEffect(); // cấp 2: +4

        assertTrue(ball.getSpeed() > baseSpeed);

        int total = 2 * Config.DURATION_PER_POWERUP;
        for (int i = 0; i < total; i++) {
            ball.update();
        }

        assertEquals(baseSpeed, ball.getSpeed(), 1e-6);
    }

    /**
     * Kiểm tra activateFastBallEffect():
     * cộng dồn cấp độ tốc độ tối đa 2.
     * mỗi cấp tăng thêm 2 đơn vị tốc độ.
     */
    @Test
    void activateFastBallEffect() {
        Ball ball = new Ball(0, 0, 10, 10, 5);
        ball.activateFastBallEffect();
        assertEquals(7.0, ball.getSpeed(), 1e-6);

        ball.activateFastBallEffect();
        assertEquals(9.0, ball.getSpeed(), 1e-6);

        ball.activateFastBallEffect();
        assertEquals(9.0, ball.getSpeed(), 1e-6);
    }

    /**
     * Kiểm tra resetSpeed(): rrset về tốc độ ban đầu và xóa hiệu ứng.
     */
    @Test
    void resetSpeed() {
        Ball ball = new Ball(0, 0, 10, 10, 5);
        ball.activateFastBallEffect();
        ball.activateFastBallEffect();
        assertTrue(ball.getSpeed() > 5.0);

        ball.resetSpeed();
        assertEquals(5.0, ball.getSpeed(), 1e-6);
    }
}

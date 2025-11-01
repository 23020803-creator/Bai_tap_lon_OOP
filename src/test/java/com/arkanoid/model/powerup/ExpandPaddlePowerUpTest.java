package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dùng Junit test chức năng của ExpandPaddlePowerUp.
 */
public class ExpandPaddlePowerUpTest {
    private Paddle paddle;
    private Ball ball;

    /**
     * setUp cho đối tượng ball và paddle.
     */
    @BeforeEach
    public void setUp() {
        paddle = new Paddle(200, 500, 120, 16, 5);
        ball = new Ball(100, 100, 12, 12, 3);
    }

    /**
     * Test chức năng phóng to khi ăn powerup.
     */
    @Test
    public void testApplyEffect_ExpandsWidth() {
        double oldWidth = paddle.getWidth();
        ExpandPaddlePowerUp powerUp = new ExpandPaddlePowerUp(0, 0, 10, 10);
        powerUp.applyEffect(paddle, ball);
        assertTrue(paddle.getWidth() > oldWidth,
                "ExpandPaddlePowerUp làm tăng chiều rộng của Paddle.");
    }

    /**
     * Test chương trình khi gặp giá trị null.
     */
    @Test
    public void testApplyEffect_NoExceptionOnNullBall() {
        ExpandPaddlePowerUp powerUp = new ExpandPaddlePowerUp(0, 0, 10, 10);
        assertDoesNotThrow(() -> powerUp.applyEffect(paddle, null),
                "ExpandPaddlePowerUp không được ném lỗi khi ball bằng null.");
    }

    /**
     * Test áp dụng powerup nhiều lần.
     */
    @Test
    public void testExpandEffect_MaxLimit() {
        ExpandPaddlePowerUp powerUp = new ExpandPaddlePowerUp(0, 0, 10, 10);
        for (int i = 0; i < 5; i++) {
            powerUp.applyEffect(paddle, ball);
        }
        double maxWidth = 120 * 2.25;
        assertTrue(paddle.getWidth() <= maxWidth + 0.001,
                "Chiều rộng của thanh Paddle không được vượt quá 2.25 lần kích thước ban đầu.");
    }

    /**
     * Test hệ số nhân khi áp dụng powerup.
     */
    @Test
    public void testExpandEffect_IncreaseByCorrectFactor() {
        ExpandPaddlePowerUp powerUp = new ExpandPaddlePowerUp(0, 0, 10, 10);
        double oldWidth = paddle.getWidth();
        powerUp.applyEffect(paddle, ball);
        assertEquals(oldWidth * 1.5, paddle.getWidth(), 0.001,
                "Chiều rộng của thanh Paddle tăng lên 1.5 lần sau khi áp dụng PowerUp.");
    }

    /**
     * Test đảm bảo đúng loại powerup.
     */
    @Test
    public void testPowerUpType_IsExpandPaddle() {
        ExpandPaddlePowerUp powerUp = new ExpandPaddlePowerUp(0, 0, 10, 10);
        assertEquals(PowerUpType.EXPAND_PADDLE, powerUp.getType(),
                "Loại PowerUp là EXPAND_PADDLE");
    }

    /**
     * Test constructor gán đúng giá trị x,y.
     */
    @Test
    public void testConstructor_SetsCorrectSizeAndPosition() {
        ExpandPaddlePowerUp p = new ExpandPaddlePowerUp(50, 60, 20, 20);

        assertEquals(50, p.getX(), "X phải khớp với hàm tạo");
        assertEquals(60, p.getY(), "Y phải khớp với hàm tạo");
        assertEquals(40, p.getWidth(), "Chiều rộng tăng gấp đôi");
        assertEquals(40, p.getHeight(), "Chiều cao tăng gấp đôi");
    }
}

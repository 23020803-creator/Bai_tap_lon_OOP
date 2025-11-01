package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dùng Junit test chức năng của FastBallPowerUp.
 */
public class FastBallPowerUpTest {

    private Paddle paddle;
    private Ball ball;
    private FastBallPowerUp powerUp;

    /**
     * Khởi tạo dữ liệu mô phỏng trước mỗi test.
     */
    @BeforeEach
    public void setUp() {
        paddle = new Paddle(100, 100, 120, 16, 5);
        ball = new Ball(100, 80, 12, 12, 3);
        powerUp = new FastBallPowerUp(0, 0, 10, 10);
    }

    /**
     * Kiểm tra việc tăng tốc độ bóng khi áp dụng PowerUp.
     */
    @Test
    public void testApplyEffect_IncreasesSpeed() {
        double before = ball.getSpeed();
        powerUp.applyEffect(paddle, ball);

        assertTrue(ball.getSpeed() > before,
                "FastBallPowerUp phải làm tăng tốc độ của bóng.");
    }

    /**
     * Đảm bảo không ném lỗi khi ball = null.
     */
    @Test
    public void testApplyEffect_DoesNotCrashOnNullBall() {
        assertDoesNotThrow(() -> powerUp.applyEffect(paddle, null),
                "FastBallPowerUp không được ném lỗi khi ball = null.");
    }

    /**
     * Kiểm tra khởi tạo đúng vị trí và kích thước nhân đôi.
     */
    @Test
    public void testConstructor_SetsCorrectValues() {
        FastBallPowerUp p = new FastBallPowerUp(50, 60, 20, 20);

        assertEquals(50, p.getX(), "Giá trị X phải trùng với constructor.");
        assertEquals(60, p.getY(), "Giá trị Y phải trùng với constructor.");
        assertEquals(40, p.getWidth(), "Chiều rộng phải được nhân đôi theo quy định lớp PowerUp.");
        assertEquals(40, p.getHeight(), "Chiều cao phải được nhân đôi theo quy định lớp PowerUp.");
    }

    /**
     * Kiểm tra loại PowerUp là FAST_BALL.
     */
    @Test
    public void testType_IsFastBall() {
        assertEquals(PowerUpType.FAST_BALL, powerUp.getType(),
                "PowerUp type phải là FAST_BALL.");
    }

    /**
     * Kiểm tra tốc độ tối đa khi tăng tốc nhiều lần.
     */
    @Test
    public void testApplyEffect_MaxSpeedLimit() {
        double baseSpeed = ball.getSpeed();
        for (int i = 0; i < 5; i++) {
            powerUp.applyEffect(paddle, ball);
        }
        double maxExpectedSpeed = baseSpeed + 4;
        assertTrue(ball.getSpeed() <= maxExpectedSpeed + 0.001,
                "Tốc độ bóng không được vượt quá giới hạn hợp lý.");
    }

    /**
     * Đảm bảo getIcon() hoạt động an toàn.
     */
    @Test
    public void testGetIcon_SafeAccess() {
        assertDoesNotThrow(() -> powerUp.getIcon(),
                "Phương thức getIcon() phải an toàn và không ném lỗi.");
    }
}

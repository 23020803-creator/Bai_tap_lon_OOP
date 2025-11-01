package com.arkanoid.model.powerup;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dùng Junit test chức năng của ExtraLifePowerUp.
 */
public class ExtraLifePowerUpTest {

    private ExtraLifePowerUp powerUp;
    private Paddle paddle;
    private Ball ball;

    /**
     * setUp cho đối tượng paddle, ball, powerUp.
     */
    @BeforeEach
    public void setUp() {
        powerUp = new ExtraLifePowerUp(0, 0, 10, 10);
        paddle = new Paddle(100, 100, 120, 16, 5);
        ball = new Ball(100, 80, 12, 12, 3);
    }

    /**
     * Kiểm tra không ném ngoại lệ khi được gọi.
     */
    @Test
    public void testApplyEffect_NoException() {
        assertDoesNotThrow(() -> powerUp.applyEffect(paddle, ball),
                "ExtraLifePowerUp không được ném lỗi khi thực thi applyEffect().");
    }

    /**
     * Đảm bảo constructor gán đúng giá trị X, Y và nhân đôi kích thước (theo PowerUp cha).
     */
    @Test
    public void testConstructor_SetsCorrectValues() {
        ExtraLifePowerUp p = new ExtraLifePowerUp(50, 60, 20, 20);

        assertEquals(50, p.getX(), "Giá trị X phải trùng với constructor.");
        assertEquals(60, p.getY(), "Giá trị Y phải trùng với constructor.");
        assertEquals(40, p.getWidth(), "Chiều rộng phải được nhân đôi theo quy định lớp PowerUp.");
        assertEquals(40, p.getHeight(), "Chiều cao phải được nhân đôi theo quy định lớp PowerUp.");
    }

    /**
     * Xác nhận loại PowerUp trả về.
     */
    @Test
    public void testType_IsExtraLife() {
        assertEquals(PowerUpType.EXTRA_LIFE, powerUp.getType(),
                "PowerUp type phải là EXTRA_LIFE.");
    }

    /**
     * Đảm bảo không ném lỗi khi ball = null.
     */
    @Test
    public void testApplyEffect_NoExceptionOnNullBall() {
        assertDoesNotThrow(() -> powerUp.applyEffect(paddle, null),
                "ExtraLifePowerUp không được ném lỗi khi ball là null.");
    }

    /**
     * Đảm bảo PowerUp trả về icon hợp lệ (hoặc null nếu file chưa tồn tại).
     */
    @Test
    public void testGetIcon_SafeAccess() {
        assertDoesNotThrow(() -> {powerUp.getIcon();},
                "Phương thức getIcon() phải an toàn và không ném lỗi.");
    }
}
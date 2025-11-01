package com.arkanoid.model.powerup;

import com.arkanoid.engine.GameManager;
import com.arkanoid.model.ball.Ball;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Dùng Junit test chức năng của TripleBallPowerUp.
 */
public class TripleBallPowerUpTest {

    private GameManager gm;
    private Ball ball;
    private TripleBallPowerUp powerUp;

    /**
     * Khởi tạo dữ liệu mô phỏng trước mỗi test.
     */
    @BeforeEach
    public void setUp() {
        gm = new GameManager(null);
        ball = new Ball(100, 100, 12, 12, 3);
        gm.getBalls().add(ball);
        powerUp = new TripleBallPowerUp(0, 0, 10, 10);
    }

    /**
     * Kiểm tra rằng khi áp dụng PowerUp, số lượng bóng trong game tăng lên 3 bóng.
     */
    @Test
    public void testApply_TripleBallAddsTwoBalls() {
        int before = gm.getBalls().size();
        powerUp.apply(gm, ball);
        int after = gm.getBalls().size();

        assertEquals(before * 3, after,
                "TripleBallPowerUp phải nhân ba số lượng bóng trong game.");
    }

    /**
     * Kiểm tra rằng tất cả các bóng mới đều có cùng tốc độ với bóng tham chiếu.
     */
    @Test
    public void testApply_AllBallsHaveSameSpeed() {
        powerUp.apply(gm, ball);

        double expected = ball.getSpeed();
        for (Ball b : gm.getBalls()) {
            assertEquals(expected, b.getSpeed(), 0.001,
                    "Tất cả bóng nhân bản phải có cùng tốc độ với bóng gốc.");
        }
    }

    /**
     * Đảm bảo không ném lỗi nếu tham số ball = null.
     */
    @Test
    public void testApply_DoesNotCrashOnNullBall() {
        assertDoesNotThrow(() -> powerUp.apply(gm, null),
                "TripleBallPowerUp không được ném lỗi khi ball là null.");
    }

    /**
     * Xác nhận loại PowerUp là TRIPLE_BALL.
     */
    @Test
    public void testType_IsTripleBall() {
        assertEquals(PowerUpType.TRIPLE_BALL, powerUp.getType(),
                "PowerUp type phải là TRIPLE_BALL.");
    }

    /**
     * Đảm bảo tọa độ và kích thước nhân đôi theo quy định lớp PowerUp.
     */
    @Test
    public void testConstructor_SetsCorrectValues() {
        TripleBallPowerUp p = new TripleBallPowerUp(50, 60, 20, 20);

        assertEquals(50, p.getX(), "Giá trị X phải trùng với constructor.");
        assertEquals(60, p.getY(), "Giá trị Y phải trùng với constructor.");
        assertEquals(40, p.getWidth(), "Chiều rộng phải được nhân đôi theo quy định lớp PowerUp.");
        assertEquals(40, p.getHeight(), "Chiều cao phải được nhân đôi theo quy định lớp PowerUp.");
    }

    /**
     * Đảm bảo getIcon() an toàn.
     */
    @Test
    public void testGetIcon_SafeAccess() {
        assertDoesNotThrow(() -> powerUp.getIcon(),
                "Phương thức getIcon() phải an toàn và không ném lỗi.");
    }
}

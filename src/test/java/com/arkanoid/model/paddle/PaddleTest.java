package com.arkanoid.model.paddle;

import com.arkanoid.engine.Config;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bộ test Paddle:
 * - Khởi tạo, mở rộng theo level, đếm thời gian hiệu ứng và reset.
 * - Tính toán tọa độ trung tâm theo chiều ngang.
 * - Cập nhật theo khung hình để tự hết hiệu ứng.
 * - Tốc độ và ràng buộc tốc độ tối thiểu.
 * - Hiệu ứng nháy khi va chạm.
 */
class PaddleTest {

    /**
     * Kiểm tra việc mở rộng paddle x1.5 lần khi ăn vật phẩm.
     */
    @Test
    void activateExpandEffect() {
        Paddle p = new Paddle(0, 0, 80, 12, 5);
        p.activateExpandEffect();
        assertEquals(120.0, p.getWidth(), 1e-6);
    }

    /**
     * Kiểm tra cập nhật chiều rộng theo cấp level.
     */
    @Test
    void updateWidthByLevel() {
        Paddle p = new Paddle(0, 0, 100, 12, 5);

        p.activateExpandEffect(); // level 1: 1.5x
        assertEquals(150.0, p.getWidth(), 1e-6);

        p.activateExpandEffect(); // level 2: 2.25x
        assertEquals(225.0, p.getWidth(), 1e-6);

        p.activateExpandEffect(); // không vượt quá 2.25x
        assertEquals(225.0, p.getWidth(), 1e-6);
    }

    /**
     * Kiểm tra resetSize(): khôi phục về baseWidth và xóa hiệu ứng.
     */
    @Test
    void resetSize() {
        Paddle p = new Paddle(0, 0, 90, 12, 5);
        p.activateExpandEffect();
        assertTrue(p.getWidth() > 90.0);

        p.resetSize();
        assertEquals(90.0, p.getWidth(), 1e-6);
    }

    /**
     * Kiểm tra getCenterX(): trả về tạo độ Õ của điểm chính giữa paddle.
     */
    @Test
    void getCenterX() {
        Paddle p = new Paddle(10, 100, 80, 12, 5);
        assertEquals(10.0 + 80.0 / 2.0, p.getCenterX(), 1e-6);
    }

    /**
     * Kiểm tra update():thời gian hiệu ứng giảm dần theo frame.
     */
    @Test
    void update() {
        Paddle p = new Paddle(0, 0, 80, 12, 5);
        double base = p.getWidth();

        p.activateExpandEffect(); // level 1
        p.activateExpandEffect(); // level 2

        int total = 2 * Config.DURATION_PER_POWERUP;
        for (int i = 0; i < total; i++) {
            p.update();
        }
        assertEquals(base, p.getWidth(), 1e-6);
    }

    /**
     * Kiểm tra getter tốc độ.
     */
    @Test
    void getSpeed() {
        Paddle p = new Paddle(0, 0, 80, 12, 6.75);
        assertEquals(6.75, p.getSpeed(), 1e-6);
    }

    /**
     * Kiểm tra setSpeed(): bị chặn dưới ở 1.
     */
    @Test
    void setSpeed() {
        Paddle p = new Paddle(0, 0, 80, 12, 5);

        p.setSpeed(0.4);
        assertEquals(1.0, p.getSpeed(), 1e-6);

        p.setSpeed(7.25);
        assertEquals(7.25, p.getSpeed(), 1e-6);
    }
}

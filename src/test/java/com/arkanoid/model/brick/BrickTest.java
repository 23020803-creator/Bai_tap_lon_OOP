package com.arkanoid.model.brick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bộ test Brick:
 * - Khởi tạo giá trị ban đầu đúng.
 * - Giảm hitPoints khi bị takeHit().
 * - Không cho phép hitPoints âm.
 * - Kiểm tra isDestroyed() hoạt động đúng.
 * - Render không ném lỗi khi vẽ.
 */
class BrickTest {

    /** Lớp con giả để test lớp trừu tượng Brick. */
    private static class DummyBrick extends Brick {
        public DummyBrick(double x, double y, double w, double h, int hitPoints, String type) {
            super(x, y, w, h, hitPoints, type);
        }
    }

    private DummyBrick brick;

    @BeforeEach
    void setUp() {
        brick = new DummyBrick(50, 100, 60, 20, 3, "NORMAL");
    }

    /**
     * Kiểm tra khởi tạo ban đầu: toạ độ, kích thước, hitPoints, type.
     */
    @Test
    void initialValues() {
        assertEquals(50, brick.getX());
        assertEquals(100, brick.getY());
        assertEquals(60, brick.getWidth());
        assertEquals(20, brick.getHeight());
        assertEquals(3, brick.getHitPoints());
        assertEquals("NORMAL", brick.getType());
        assertFalse(brick.isDestroyed());
    }

    /**
     * Kiểm tra takeHit(): giảm hitPoints đúng 1 mỗi lần gọi.
     */
    @Test
    void takeHitDecreasesHitPoints() {
        brick.takeHit();
        assertEquals(2, brick.getHitPoints());
        assertFalse(brick.isDestroyed());

        brick.takeHit();
        assertEquals(1, brick.getHitPoints());
        assertFalse(brick.isDestroyed());
    }

    /**
     * Kiểm tra gạch bị phá hủy khi hitPoints = 0.
     */
    @Test
    void destroyedAfterEnoughHits() {
        brick.takeHit();
        brick.takeHit();
        brick.takeHit();
        assertEquals(0, brick.getHitPoints());
        assertTrue(brick.isDestroyed());
    }

    /**
     * Kiểm tra hitPoints không bị giảm âm khi takeHit() nhiều hơn số lần chịu đòn.
     */
    @Test
    void hitPointsNeverNegative() {
        for (int i = 0; i < 10; i++) {
            brick.takeHit();
        }
        assertEquals(0, brick.getHitPoints());
        assertTrue(brick.isDestroyed());
    }
}


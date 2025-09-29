package com.arkanoid.model.brick;

/**
 * Lớp gạch thường (Normal Brick).
 * - Đây là loại gạch cơ bản nhất trong game.
 * - Bị phá hủy chỉ sau **1 lần** va chạm với bóng.
 */
public final class NormalBrick extends Brick {

    /**
     * Khởi tạo gạch thường với vị trí và kích thước cho trước.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng
     * @param h chiều cao
     */
    public NormalBrick(double x, double y, double w, double h) {
        // hitPoints = 1 (bị phá ngay sau 1 lần va chạm)
        super(x, y, w, h, /*hitPoints=*/1, "NORMAL");
    }
}

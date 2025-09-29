package com.arkanoid.model.brick;

/**
 * Lớp gạch cứng (Strong Brick).
 * - Đây là loại gạch đặc biệt, khó phá hơn gạch thường.
 * - Cần **nhiều hơn một lần va chạm** với bóng mới bị phá hủy.
 * - Trong phiên bản này: cần 3 lần va chạm (hitPoints = 3).
 */
public final class StrongBrick extends Brick {

    /**
     * Khởi tạo gạch cứng với vị trí và kích thước cho trước.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng
     * @param h chiều cao
     */
    public StrongBrick(double x, double y, double w, double h) {
        // hitPoints = 3 (cần 3 lần bóng chạm để phá hủy)
        super(x, y, w, h, /*hitPoints=*/3, "STRONG");
    }
}


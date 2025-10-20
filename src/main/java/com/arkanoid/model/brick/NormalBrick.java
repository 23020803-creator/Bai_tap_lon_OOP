package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp gạch thường (Normal Brick).
 * - Đây là loại gạch cơ bản nhất trong game.
 * - Bị phá hủy chỉ sau 1 lần va chạm với bóng.
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
        super(x, y, w, h, 1, "NORMAL");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(100, 180, 255)); // xanh lam nhạt
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
    }
}

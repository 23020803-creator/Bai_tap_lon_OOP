package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp gạch cứng (Strong Brick).
 * - Đây là loại gạch đặc biệt, khó phá hơn gạch thường.
 * - Cần nhiều hơn một lần va chạm với bóng mới bị phá hủy.
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
        super(x, y, w, h, 3, "STRONG");
    }

    @Override
    public void render(GraphicsContext gc) {
        int hit = getHitPoints();
        double ratio = (hit - 1) / 2.0; // 3→1.0, 2→0.5, 1→0.0
        Color baseColor = Color.rgb(255, 80, 80);
        Color finalColor = Color.rgb(100, 180, 255); // màu normal
        Color blend = baseColor.interpolate(finalColor, 1 - ratio);

        gc.setFill(blend);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        // hiển thị số lần hit còn lại
        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(hit), getX() + getWidth() / 2 - 4, getY() + getHeight() / 2 + 4);
    }
}


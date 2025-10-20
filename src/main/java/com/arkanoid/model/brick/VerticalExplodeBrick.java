package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Gạch nổ dọc: khi bị phá sẽ phá toàn bộ gạch cùng cột.
 */
public final class VerticalExplodeBrick extends Brick {

    public VerticalExplodeBrick(double x, double y, double w, double h) {
        // 1 hit để phá
        super(x, y, w, h, 1, "EXPLODE_VERTICAL");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(255, 220, 80)); // vàng
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        // ký hiệu “|”
        gc.setStroke(Color.WHITE);
        double cx = getX() + getWidth() / 2;
        double cy = getY() + getHeight() / 2;
        gc.strokeLine(cx, cy - 10, cx, cy + 10);
    }
}

package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Gạch nổ ngang: khi bị phá sẽ phá toàn bộ gạch cùng hàng.
 */
public final class HorizontalExplodeBrick extends Brick {

    public HorizontalExplodeBrick(double x, double y, double w, double h) {
        // 1 hit để phá
        super(x, y, w, h, 1, "EXPLODE_HORIZONTAL");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(255, 150, 50)); // cam
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        // ký hiệu “—”
        gc.setStroke(Color.WHITE);
        double cx = getX() + getWidth() / 2;
        double cy = getY() + getHeight() / 2;
        gc.strokeLine(cx - 10, cy, cx + 10, cy);
    }
}

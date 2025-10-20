package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Gạch không thể phá: không giảm hp, bóng sẽ nảy lại.
 */
public final class UnbreakableBrick extends Brick {

    public UnbreakableBrick(double x, double y, double w, double h) {
        // Đặt hitPoints = Integer.MAX_VALUE (không bao giờ trở về <=0)
        super(x, y, w, h, Integer.MAX_VALUE, "UNBREAKABLE");
    }

    @Override
    public void takeHit() {
        // Không giảm hitpoint
    }

    @Override
    public boolean isDestroyed() {
        return false; // không bao giờ bị phá
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(100, 100, 100)); // xám
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        // vẽ biểu tượng ổ khóa đơn giản
        gc.setStroke(Color.WHITE);
        double cx = getX() + getWidth() / 2;
        double cy = getY() + getHeight() / 2;
        gc.strokeRect(cx - 6, cy - 4, 12, 8); // thân khóa
        gc.strokeOval(cx - 6, cy - 10, 12, 8); // vòng khóa
    }
}

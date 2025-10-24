package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Gạch không thể phá: không giảm hp, bóng sẽ nảy lại.
 * Kí hiệu: ổ khóa.
 */
public final class UnbreakableBrick extends Brick {

    private final Image lockImage;

    public UnbreakableBrick(double x, double y, double w, double h) {
        super(x, y, w, h, Integer.MAX_VALUE, "UNBREAKABLE");

        lockImage = new Image(getClass().getResource("/images_brick/LockSymbol.png").toExternalForm());
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

        double imgW = getWidth() * 0.5;
        double imgH = getHeight() ;
        double cx = getX() + (getWidth() - imgW) / 2;
        double cy = getY() + (getHeight() - imgH) / 2;

        gc.drawImage(lockImage, cx, cy, imgW, imgH);
    }
}

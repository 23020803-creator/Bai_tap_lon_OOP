package com.arkanoid.model.brick;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Gạch nổ ngang: khi bị phá sẽ phá toàn bộ gạch cùng hàng.
 * Ký hiệu: Quả bom.
 */
public final class HorizontalExplodeBrick extends Brick {

    private static final Image BOMB_IMAGE = new Image(
            HorizontalExplodeBrick.class.getResource("/images/brick/BombSymbol.gif").toExternalForm()
    );

    public HorizontalExplodeBrick(double x, double y, double w, double h) {
        super(x, y, w, h, 1, "EXPLODE_HORIZONTAL");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(255, 220, 80));
        gc.fillRect(getX(), getY(), getWidth(), getHeight());
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());

        double imgW = getWidth() * 0.5;
        double imgH = getHeight();
        double cx = getX() + (getWidth() - imgW) / 2;
        double cy = getY() + (getHeight() - imgH) / 2;

        gc.drawImage(BOMB_IMAGE, cx, cy, imgW, imgH);
    }
}

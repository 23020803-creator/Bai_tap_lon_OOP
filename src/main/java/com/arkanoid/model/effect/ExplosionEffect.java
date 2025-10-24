package com.arkanoid.model.effect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Hiệu ứng nổ tạm thời vẽ trực tiếp bằng GraphicsContext.
 */
public final class  ExplosionEffect {
    private final double x;
    private final double y;
    private int timer; // frame còn lại
    private final double maxRadius;

    public ExplosionEffect(double x, double y, int durationFrames, double maxRadius) {
        this.x = x;
        this.y = y;
        this.timer = Math.max(1, durationFrames);
        this.maxRadius = maxRadius;
    }

    public boolean isAlive() {
        return timer > 0;
    }

    public void update() {
        if (timer > 0) {
            timer--;
        }
    }

    /**
     * Vẽ hiệu ứng nổ 3 lớp
     */
    public void render(GraphicsContext gc) {
        if (timer <= 0) {
            return;
        }
        double lifeFraction = (double) timer / 60;
        double radius = maxRadius * (1.0 - lifeFraction);
        if (radius < 0) {
            radius = maxRadius;
        }

        double alpha = Math.max(0, Math.min(0.9, timer / 20));
        gc.save();
        // lớp ngoài màu vàng
        gc.setGlobalAlpha(alpha * 0.4);
        gc.setFill(Color.rgb(255, 200, 80));
        gc.fillOval(x - radius * 1.3, y - radius * 1.3, radius * 2.6, radius * 2.6);

        // lớp giữa màu trắng
        gc.setGlobalAlpha(alpha * 0.9);
        gc.setFill(Color.rgb(255, 255, 230));
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // lớp trong viền lửa màu vàng cam
        gc.setGlobalAlpha(alpha * 0.8);
        gc.setStroke(Color.rgb(255, 160, 50));
        gc.setLineWidth(2.5);
        double r2 = radius * 0.6;
        gc.strokeOval(x - r2, y - r2, r2 * 2, r2 * 2);
        gc.restore();
    }
}

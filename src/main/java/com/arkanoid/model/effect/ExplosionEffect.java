package com.arkanoid.model.effect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Hiệu ứng nổ tạm thời vẽ trực tiếp bằng GraphicsContext.
 * Lưu ý: đơn giản, frame-based.
 */
public final class ExplosionEffect {
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
        if (timer > 0) timer--;
    }

    public void render(GraphicsContext gc) {
        if (timer <= 0) return;
        double progress = 1.0 - ((double) timer / (double) Math.max(1, timer + 0)); // relative progress not exact but okay
        // safer compute current progress as fraction of initial duration:
        // (We don't store initialDuration here, but we can derive visual effect using timer and maxRadius)
        double lifeFraction = (double) timer / 20.0; // if timer started at ~20
        double radius = maxRadius * (1.0 - lifeFraction); // expands then fades
        // clamp
        if (radius < 0) radius = maxRadius;

        // alpha depends on remaining timer
        double alpha = Math.max(0, Math.min(0.9, timer / 20.0));
        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setFill(Color.rgb(255, 200, 120, alpha));
        gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // inner flash
        gc.setGlobalAlpha(alpha * 0.6);
        gc.setStroke(Color.rgb(255, 220, 180, alpha));
        gc.strokeOval(x - radius * 0.6, y - radius * 0.6, radius * 1.2, radius * 1.2);
        gc.restore();
    }
}

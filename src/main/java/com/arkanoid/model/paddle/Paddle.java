package com.arkanoid.model.paddle;

import com.arkanoid.engine.Config;
import com.arkanoid.model.object.MovableObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * Lớp Paddle (thanh trượt) do người chơi điều khiển.
 * - Paddle chỉ di chuyển ngang trong màn chơi.
 * - Có thể nhận hiệu ứng từ PowerUp (ví dụ: kéo dài, tăng tốc...).
 */
public final class Paddle extends MovableObject {
    private double speed;               // Tốc độ hiện tại
    private final double baseWidth;
    private int expandLevel;
    private int expandTimer;
    private int flashTimer = 0;   // giảm dần trong update()

    /**
     * Khởi tạo Paddle.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param width chiều rộng
     * @param height chiều cao
     * @param speed tốc độ di chuyển ngang
     */
    public Paddle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.baseWidth = width;
        this.expandLevel = 0;
        this.expandTimer = 0;
        this.speed = speed;
    }

    /**
     * Áp dụng hiệu ứng PowerUp mới cho Paddle.
     */
    public void activateExpandEffect() {
        if (expandLevel < 2) {
            expandLevel++;
        }
        this.expandTimer += Config.DURATION_PER_POWERUP;
        updateWidthByLevel();
    }

    /**
     * Cập nhật chiều rộng theo cấp độ paddle.
     */
    public void updateWidthByLevel() {
        if (expandLevel == 2) {
            setWidth(baseWidth * 2.25);
        }  else if (expandLevel == 1) {
            setWidth(baseWidth * 1.5);
        } else  {
            setWidth(baseWidth);
        }
    }

    /**
     * Reset kích thước paddle về trạng thái ban đầu.
     */
    public void resetSize() {
        expandLevel = 0;
        expandTimer = 0;
        updateWidthByLevel();
    }

    /**
     * Trả về toạ độ X của tâm Paddle.
     */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    /**
     * Đếm ngược và kiêmr tra hiệu ứng.
     */
    @Override
    public void update() {
        if (expandTimer > 0) {
            expandTimer--;
            if (expandTimer == 0) {
                resetSize();
            }
        }
        if (flashTimer > 0) {
            flashTimer--;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        double x = getX();
        double y = getY();
        double w = getWidth();
        double h = getHeight();
        double r = h * 0.8; // bo góc

        // Đổ bóng
        gc.setGlobalAlpha(0.35);
        gc.setFill(Color.web("#0A0F0F"));
        gc.fillRoundRect(x, y + 4, w, h, r, r);
        gc.setGlobalAlpha(1.0);

        // Màu theo cấp độ paddle
        String top, bot;
        switch (expandLevel) {
            case 2: // Đỏ
                top = "#FF6B6B"; // đỏ sáng
                bot = "#C62828"; // đỏ đậm
                break;
            case 1: // Cam
                top = "#FFD166"; // cam sáng
                bot = "#FF8C42"; // cam đậm
                break;
            default: // Vàng
                top = "#FFF685"; // vàng sáng
                bot = "#FFD54F"; // vàng đậm
                break;
        }

        // Tạo dải màu từ sáng sang đậm
        LinearGradient body = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(top)),
                new Stop(1, Color.web(bot))
        );
        gc.setFill(body);
        gc.fillRoundRect(x, y, w, h, r, r);

        // Viền tối để tách nền
        gc.setStroke(Color.web("#2E0A00"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + 0.5, y + 0.5, w - 1, h - 1, r, r);

        // Đánh bóng paddle
        gc.setGlobalAlpha(0.18);
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x + 4, y + 3, w - 8, h * 0.45, r, r);
        gc.setGlobalAlpha(1.0);

        // Flash khi va chạm bóng
        if (flashTimer > 0) {
            double a = Math.min(1.0, flashTimer / 8.0);
            gc.setGlobalAlpha(0.25 * a);
            gc.setFill(Color.WHITE);
            gc.fillRoundRect(x - 4, y - 4, w + 4, h + 4, r, r);
            gc.setGlobalAlpha(1.0);
        }
    }

    // Getter & Setter tốc độ
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(1.0, speed);
    }

    public void hitFlash() { this.flashTimer = 10; }
}



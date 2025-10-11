package com.arkanoid.model.paddle;

import com.arkanoid.engine.Config;
import com.arkanoid.model.object.MovableObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
    }

    @Override
    public void render(GraphicsContext gc) {
        // Vẽ Paddle hình chữ nhật bo góc màu xanh nhạt
        gc.setFill(Color.rgb(120, 220, 160));
        gc.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 10, 10);
    }

    // Getter & Setter tốc độ
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(1.0, speed);
    }
}



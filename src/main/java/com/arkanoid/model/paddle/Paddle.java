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
    private final double baseSpeed;      // Tốc độ mặc định ban đầu
    private double speed;                // Tốc độ hiện tại (có thể thay đổi do PowerUp)

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
        this.baseSpeed = speed;
        this.speed = speed;
    }

    /** Di chuyển sang trái (không vượt ra ngoài biên trái màn hình). */
    public void moveLeft() {
        setX(Math.max(0, getX() - speed));
    }

    /** Di chuyển sang phải (không vượt ra ngoài biên phải màn hình). */
    public void moveRight() {
        setX(Math.min(Config.VIEW_WIDTH - getWidth(), getX() + speed));
    }

    /** Đặt lại tốc độ và trạng thái ban đầu (dùng khi PowerUp hết hiệu lực). */
    public void resetStats() {
        speed = baseSpeed;
    }

    /** Trả về toạ độ X của tâm Paddle (hữu ích để đặt bóng). */
    public double getCenterX() {
        return getX() + getWidth() / 2.0;
    }

    @Override
    public void update() {
        // Paddle không tự di chuyển, chỉ cập nhật thông qua input từ người chơi.
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

    public void setSpeed(double s) {
        this.speed = Math.max(1, s);
    }
}



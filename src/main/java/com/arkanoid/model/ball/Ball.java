package com.arkanoid.model.ball;

import com.arkanoid.engine.Config;
import com.arkanoid.model.object.GameObject;
import com.arkanoid.model.object.MovableObject;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Quả bóng trong game Arkanoid.
 * - Bóng có thể di chuyển, va chạm với tường, paddle và gạch.
 * - Bóng nảy lại khi chạm vật thể.
 */
public final class Ball extends MovableObject {
    private double speed;           // Tốc độ bóng hiện tại
    private final double baseSpeed;    // Tốc độ bóng cơ bản
    private int directionX = 1;    // Hướng ngang: +1 sang phải, -1 sang trái
    private int directionY = -1;   // Hướng dọc: -1 đi lên, +1 đi xuống
    private int speedBoostLevel;  // cấp độ của tốc độ bóng
    private int speedBoostTimer; // Thời gian hiệu lực

    /**
     * Khởi tạo bóng với vị trí, kích thước và tốc độ ban đầu.
     */
    public Ball(double x, double y, double w, double h, double speed) {
        super(x, y, w, h);
        this.speed = speed;
        this.baseSpeed = speed;
        this.speedBoostTimer = 0;
        this.speedBoostLevel = 0;
        setSpeed(speed);
    }

    /**
     * Đặt lại bóng nằm trên paddle (sau khi mất mạng).
     * Vị trí: ở giữa paddle, ngay phía trên.
     */
    public void resetOnPaddle(Paddle paddle) {
        setX(paddle.getCenterX() - getWidth() / 2);
        setY(paddle.getY() - getHeight() - 2);
        directionX = 1;
        directionY = -1;
        setSpeed(speed);
    }

    /**
     * Đảo hướng ngang của bóng (khi chạm tường trái/phải hoặc gạch/paddle ngang).
     */
    public void bounceHorizontal() {
        directionX *= -1;
        dx = speed * directionX;
    }

    /**
     * Đảo hướng dọc của bóng (khi chạm trần, sàn hoặc gạch từ trên/dưới).
     */
    public void bounceVertical() {
        directionY *= -1;
        dy = speed * directionY;
    }

    /**
     * Xử lý bóng bật lại khi va chạm với paddle.
     * Tính toán vị trí va chạm so với tâm paddle:
     * - Nếu bóng chạm bên trái paddle → đi sang trái.
     * - Nếu chạm bên phải paddle → đi sang phải.
     * - Nếu ngay giữa → mặc định đi sang phải.
     * Bóng luôn bật ngược lên trên.
     */
    public void bounceOff(GameObject other) {
        // Tính tâm của paddle và độ lệch của bóng
        double otherCenter = other.getBounds().getMinX() + other.getBounds().getWidth() / 2;
        double offset = (getCenterX() - otherCenter);

        // Quyết định hướng ngang
        directionX = (int) Math.signum(offset);
        if (directionX == 0) {
            directionX = 1; // Tránh trường hợp đứng yên
        }
        directionY = -1; // Luôn bật lên
        setSpeed(speed);
    }

    /** Kiểm tra va chạm bằng AABB (Axis-Aligned Bounding Box). */
    public boolean checkCollision(GameObject other) {
        return getBounds().intersects(other.getBounds());
    }

    /**
     * Đếm ngược và kiểm tra thời gian hiệu ứng.
     */
    @Override
    public void update() {
        move();
        if (speedBoostTimer > 0) {
            speedBoostTimer--;
            if (speedBoostTimer <= 0) {
                resetSpeed();
            }
        }
    }

    /**
     * Cập nhật tốc độ dựa vào cấp độ bóng.
     */
    private void updateSpeedByLevel() {
        if (speedBoostLevel == 1) {
            setSpeed(baseSpeed + 2);
        } else if (speedBoostLevel == 2) {
            setSpeed(baseSpeed + 4);
        }
        else {
            setSpeed(baseSpeed);
        }
    }

    /**
     * Kích hoạt hoặc cộng dồn hiệu ứng tăng tốc.
     */
    public void activateFastBallEffect() {
        if (speedBoostLevel < 2) {
            speedBoostLevel ++;
        }
        this.speedBoostTimer += Config.DURATION_PER_POWERUP;
        updateSpeedByLevel();
    }

    /**
     * Reset tốc độ bóng về trạng thái ban đầu.
     */
    public void resetSpeed() {
        speedBoostLevel = 0;
        speedBoostTimer = 0;
        updateSpeedByLevel();
    }

    @Override
    public void render(GraphicsContext gc) {
        // Vẽ bóng hình tròn màu hồng nhạt
        gc.setFill(Color.rgb(240, 140, 140));
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    // Getter & Setter tốc độ
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = Math.max(1, speed);
        this.dx = this.speed * directionX;
        this.dy = this.speed * directionY;
    }
}


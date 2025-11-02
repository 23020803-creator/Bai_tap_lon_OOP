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
     * - Nếu bóng chạm bên trái paddle: đi sang trái.
     * - Nếu chạm bên phải paddle: đi sang phải.
     * - Nếu ngay giữa: mặc định đi sang phải.
     * Bóng luôn bật ngược lên trên.
     */
    public void bounceOff(GameObject other) {
        // Chuẩn hoá độ lệch va chạm so với tâm paddle
        double ox = other.getBounds().getMinX();
        double ow = other.getBounds().getWidth();
        double otherCenter = ox + ow / 2.0; // tâm paddle
        double half = ow / 2.0; // một nửa chiều rộng
        double norm = (getCenterX() - otherCenter) / half;  // chuẩn hóa [-1, 1]

        // Tham số góc min/max
        final double THETA_MIN = 10.0 * Math.PI/180;  // 10°
        final double THETA_MAX = 75.0 * Math.PI/180;  // 75°
        final double GAMMA = 1.35;                // độ lệch nhẹ

        // Góc lệch theo norm
        double u = Math.pow(Math.abs(norm), GAMMA);           // [0..1]
        double theta = THETA_MIN + (THETA_MAX - THETA_MIN) * u;

        // Phân rã vận tốc theo góc
        double newDx = speed * Math.sin(theta);
        double newDy = speed * Math.cos(theta);
        if (norm >= 0) {
            directionX = 1;
        } else {
            directionX = -1;
        }
        directionY = -1;
        this.dx = directionX * newDx;
        this.dy = directionY * newDy;
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

        // Tính hướng bay hiện tại (độ lớn vector vận tốc)
        double currentMag = Math.hypot(dx, dy);
        if (currentMag > 0) {
            // Giữ nguyên hướng bóng và tăng độ lớn vận tốc
            dx = (dx / currentMag) * this.speed;
            dy = (dy / currentMag) * this.speed;
        } else {
            // Nếu bóng đứng yên thì đặt hướng mặc định
            dx = this.speed * directionX;
            dy = this.speed * directionY;
        }
    }
}


package com.arkanoid.model.powerup;

import com.arkanoid.model.object.MovableObject;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;

/**
 * Lớp trừu tượng đại diện cho PowerUp (vật phẩm rơi xuống).
 * - PowerUp là đối tượng có thể rơi xuống (kế thừa MovableObject).
 * - Khi người chơi (Paddle) ăn được thì áp dụng hiệu ứng tạm thời.
 * - Sau một khoảng thời gian thì hiệu ứng sẽ hết tác dụng.
 */
public abstract class PowerUp extends MovableObject {
    private final PowerUpType type;       // Loại PowerUp (EXPAND_PADDLE, FAST_BALL, ...)
    private Image icon;                   // Ảnh minh họa cho PowerUp
    /**
     * Khởi tạo một PowerUp.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng hiển thị
     * @param h chiều cao hiển thị
     * @param type loại PowerUp
     */
    protected PowerUp(double x, double y, double w, double h, PowerUpType type) {
        super(x, y, w * 2, w * 2);
        this.type = type;
        this.setDy(2); // Tốc độ rơi mặc định = 2 pixel/frame
        loadIcon();    // load ảnh
    }

    /**
     * Áp dụng hiệu ứng ngay khi người chơi ăn được PowerUp.
     * (ví dụ: tăng tốc bóng, mở rộng Paddle, v.v.)
     */
    public abstract void applyEffect(Paddle paddle, Ball ball);

    /**
     * Update trạng thái khi vật phẩm rơi
     */
    @Override
    public void update() {
        setY(getY() + getDy());
    }

    /** Lấy loại PowerUp. */
    public PowerUpType getPowerUpType() {
        return type;
    }

    /**
     * Load ảnh phù hợp với loại PowerUp.
     */
    private void loadIcon() {
        String imagePath = switch (type) {
            case EXPAND_PADDLE -> "/images/powerup/expand_paddle.png";
            case FAST_BALL -> "/images/powerup/fast_ball.png";
            case EXTRA_LIFE -> "/images/powerup/extra_life.png";
        };

        try {
            icon = new Image(getClass().getResourceAsStream(imagePath),
                    getWidth(), getHeight(), true, false);
        } catch (Exception e) {
            icon = null;
        }
    }

    public Image getIcon() {
        return icon;
    }

    /**
     * Vẽ PowerUp lên canvas JavaFX.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (icon != null) {
            gc.save();
            // Thêm hiệu ứng phóng to thu nhỏ cho từng loại powerup.
            double t = System.currentTimeMillis() / 300.0;
            double scale = 1.0 + 0.15 * Math.sin(t); // tạo nhịp to nhỏ theo thời gian.
            double alpha = 0.9 + 0.1 * Math.sin(t * 1.5);
            double newW = getWidth() * scale;
            double newH = getHeight() * scale;
            double offsetX = getX() - (newW - getWidth()) / 2;
            double offsetY = getY() - (newH - getHeight()) / 2;

            gc.setGlobalAlpha(alpha);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.drawImage(getIcon(), offsetX, offsetY, newW, newH);
            gc.restore();
        }
    }
}

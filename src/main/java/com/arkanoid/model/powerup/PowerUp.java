package com.arkanoid.model.powerup;

import com.arkanoid.model.object.MovableObject;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp trừu tượng đại diện cho PowerUp (vật phẩm rơi xuống).
 * - PowerUp là đối tượng có thể rơi xuống (kế thừa MovableObject).
 * - Khi người chơi (Paddle) ăn được thì áp dụng hiệu ứng tạm thời.
 * - Sau một khoảng thời gian thì hiệu ứng sẽ hết tác dụng.
 */
public abstract class PowerUp extends MovableObject {
    private final PowerUpType type;       // Loại PowerUp (EXPAND_PADDLE, FAST_BALL, ...)
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
        super(x, y, w, h);
        this.type = type;
        this.setDy(2); // Tốc độ rơi mặc định = 2 pixel/frame
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
     * Vẽ PowerUp lên canvas của JavaFX.
     */
    @Override
    public void render(GraphicsContext gc) {
        switch (getPowerUpType()) {
            case EXPAND_PADDLE -> gc.setFill(Color.RED);    // Mở rộng Paddle = đỏ
            case FAST_BALL ->  gc.setFill(Color.GREEN);     // Tăng tốc bóng = xanh
            case EXTRA_LIFE -> gc.setFill(Color.YELLOW);    // Thêm mạng chơi = vàng
        }
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}

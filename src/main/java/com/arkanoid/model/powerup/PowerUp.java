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
 * - Sau một khoảng thời gian (durationFrames) thì hiệu ứng sẽ hết tác dụng.
 */
public abstract class PowerUp extends MovableObject {
    private final PowerUpType type;       // Loại PowerUp (EXPAND_PADDLE, FAST_BALL, ...)
    private final int durationFrames;     // Thời gian hiệu lực tính theo số frame
    private int ageFrames = 0;            // Số frame đã tồn tại kể từ khi xuất hiện

    /**
     * Khởi tạo một PowerUp.
     *
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng hiển thị
     * @param h chiều cao hiển thị
     * @param type loại PowerUp
     * @param durationFrames thời gian hiệu lực (theo frame)
     */
    protected PowerUp(double x, double y, double w, double h, PowerUpType type, int durationFrames) {
        super(x, y, w, h);
        this.type = type;
        this.durationFrames = durationFrames;
        this.setDy(2); // Tốc độ rơi mặc định = 2 pixel/frame
    }

    /**
     * Áp dụng hiệu ứng ngay khi người chơi ăn được PowerUp.
     * (ví dụ: tăng tốc bóng, mở rộng Paddle, v.v.)
     */
    public abstract void applyEffect(Paddle paddle, Ball ball);

    /**
     * Gỡ hoặc hoàn tác hiệu ứng khi hết thời gian hiệu lực.
     */
    public abstract void removeEffect(Paddle paddle, Ball ball);

    @Override
    public void update() {
        // Mỗi frame: PowerUp rơi xuống + tăng tuổi thọ
        move();
        ageFrames++;
    }

    /** Kiểm tra xem hiệu ứng đã hết hạn chưa. */
    public boolean isExpired() {
        return ageFrames >= durationFrames;
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
            default            -> gc.setFill(Color.WHITE);  // Các loại khác (nếu có)
        }
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}

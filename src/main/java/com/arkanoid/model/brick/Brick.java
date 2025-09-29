package com.arkanoid.model.brick;

import com.arkanoid.model.object.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Lớp trừu tượng đại diện cho một viên gạch trong game.
 * - Mỗi viên gạch có số lần chịu va chạm (hitPoints).
 * - Có kiểu (type) để phân biệt gạch thường, gạch cứng,...
 * - Khi va chạm với bóng thì hitPoints giảm dần cho đến khi bị phá hủy.
 */
public abstract class Brick extends GameObject {
    private int hitPoints;      // Số lần va chạm còn lại để phá hủy
    private final String type;  // Loại gạch (NORMAL, STRONG, ...)

    /**
     * Khởi tạo một viên gạch.
     * @param x vị trí X
     * @param y vị trí Y
     * @param w chiều rộng
     * @param h chiều cao
     * @param hitPoints số lần cần đánh để phá gạch
     * @param type loại gạch (dùng để phân loại hiển thị/logic)
     */
    protected Brick(double x, double y, double w, double h, int hitPoints, String type) {
        super(x, y, w, h);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    /** Giảm số hitPoints đi 1 khi bị bóng va chạm. */
    public void takeHit() {
        if (hitPoints > 0) hitPoints--;
    }

    /** Kiểm tra gạch đã bị phá hủy hay chưa. */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    // Getter
    public int getHitPoints() {
        return hitPoints;
    }

    public String getType() {
        return type;
    }

    @Override
    public void update() {
        // Gạch là đối tượng tĩnh, không cần cập nhật mỗi frame
    }

    @Override
    public void render(GraphicsContext gc) {
        // Màu gạch thay đổi cường độ dựa theo số hitPoints còn lại
        int intensity = Math.min(255, 80 + getHitPoints() * 40);
        gc.setFill(Color.rgb(intensity, 160, 220));
        gc.fillRect(getX(), getY(), getWidth(), getHeight());

        // Vẽ viền đen cho gạch
        gc.setStroke(Color.BLACK);
        gc.strokeRect(getX(), getY(), getWidth(), getHeight());
    }
}



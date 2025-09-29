package com.arkanoid.model.object;

import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

/**
 * Lớp cơ sở trừu tượng cho tất cả các đối tượng có thể vẽ trong game.
 * - Mọi đối tượng hiển thị (Paddle, Ball, Brick, PowerUp, ...) đều kế thừa từ đây.
 * - Lưu trữ vị trí (x, y) và kích thước (width, height).
 * - Cung cấp các phương thức cơ bản như update(), render() và getBounds().
 */
public abstract class GameObject {
    private double x;       // Toạ độ X (góc trên bên trái)
    private double y;       // Toạ độ Y (góc trên bên trái)
    private double width;   // Chiều rộng
    private double height;  // Chiều cao

    /**
     * Khởi tạo một đối tượng GameObject với vị trí và kích thước cho trước.
     */
    protected GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Cập nhật logic của đối tượng (mỗi frame).
     * Các lớp con (Ball, Paddle, PowerUp...) sẽ override để định nghĩa hành vi riêng.
     */
    public abstract void update();

    /**
     * Vẽ đối tượng ra màn hình bằng GraphicsContext (JavaFX).
     * Các lớp con sẽ override để hiển thị theo cách riêng (hình chữ nhật, hình tròn...).
     */
    public abstract void render(GraphicsContext gc);

    /**
     * Trả về hình chữ nhật bao quanh đối tượng, dùng cho kiểm tra va chạm (collision).
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // ---------------- Tiện ích tính toán hình học -----------------------------
    public double getRight() {
        return x + width; // Toạ độ X cạnh phải
    }

    public double getBottom() {
        return y + height; // Toạ độ Y cạnh dưới
    }

    public double getCenterX() {
        return x + width / 2; // Toạ độ X tâm đối tượng
    }

    public double getCenterY() {
        return y + height / 2; // Toạ độ Y tâm đối tượng
    }

    // ---------------- Getter / Setter -----------------------------------------
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}

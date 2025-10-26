package com.arkanoid.model.object;

/**
 * Lớp cơ sở trừu tượng cho các đối tượng có khả năng di chuyển.
 * - Kế thừa từ GameObject (có vị trí và kích thước).
 * - Bổ sung thêm vận tốc dx, dy để đối tượng có thể di chuyển.
 * - Các đối tượng như Ball, Paddle, PowerUp sẽ kế thừa lớp này
 *   để sử dụng logic di chuyển.
 */
public abstract class MovableObject extends GameObject {
    public double dx; // Vận tốc theo trục X (mỗi frame di chuyển dx pixel)
    public double dy; // Vận tốc theo trục Y (mỗi frame di chuyển dy pixel)

    /**
     * Khởi tạo đối tượng có khả năng di chuyển.
     *
     * @param x vị trí X ban đầu
     * @param y vị trí Y ban đầu
     * @param width chiều rộng
     * @param height chiều cao
     */
    protected MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Cập nhật vị trí theo vận tốc hiện tại (dx, dy).
     * - getX() + dx → toạ độ X mới
     * - getY() + dy → toạ độ Y mới
     */
    public void move() {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    // ---------------- Getter & Setter cho vận tốc ----------------
    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }
}

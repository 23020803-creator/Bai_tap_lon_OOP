package com.arkanoid.engine;
import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.Brick;
import com.arkanoid.model.brick.NormalBrick;
import com.arkanoid.model.brick.StrongBrick;
import com.arkanoid.model.paddle.Paddle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;
/**
 * Lớp phụ trách vẽ khung hình.
 */
public class Renderer {
    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }

    /**
     * Tô đen để xóa hình ảnh cũ của game.
     */
    private void drawBackground() {
        gc.setFill(Color.web("#101016"));
        gc.fillRect(0, 0, Config.VIEW_WIDTH, Config.VIEW_HEIGHT);
    }

    /**
     * In ra điểm và mạng lên màn hình.
     * @param score điểm.
     * @param lives mạng.
     */
    private void drawHud(int score, int lives) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", 18));
        gc.fillText("Điểm: " + score, 16, 24);
        gc.fillText("Mạng: " + lives, Config.VIEW_WIDTH - 16, 24);
    }

    /**
     * Vẽ màn hình game và in trạng thái.
     * @param msg trạng thái.
     */
    private void drawBanner(String msg) {
        gc.setFill(Color.color(0, 0, 0, 0.6));
        gc.fillRect(100, Config.VIEW_HEIGHT / 2.0 - 50, Config.VIEW_WIDTH - 200, 100);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Consolas", 24));
        double w = msg.length() * gc.getFont().getSize() * 0.55;
        gc.fillText(msg, (Config.VIEW_WIDTH - w) / 2.0, Config.VIEW_HEIGHT / 2.0 + 8);
    }

    /**
     * Vẽ gạch - brick.
     * @param b gạch.
     */
    private void drawBrick(Brick b) {
        // Chọn màu theo loại gạch
        if (b instanceof StrongBrick) {
            gc.setFill(Color.web("#EF476F"));
            gc.fillRoundRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), 6, 6);
            gc.setFill(Color.color(1,1,1,0.25));
        } else if (b instanceof NormalBrick) {
            gc.setFill(Color.web("#7AE582"));
            gc.fillRoundRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), 6, 6);
        } else {
            gc.setFill(Color.SILVER);
            gc.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
    }

    /**
     * Vẽ paddle.
     * @param paddle.
     */
    private void drawPaddle(Paddle p) {
        gc.setFill(Color.web("#22CCFF"));
        gc.fillRoundRect(p.getX(), p.getY(), p.getWidth(), p.getHeight(), 8, 8);
    }

    /**
     * Vẽ ball - bóng.
     * @param ball bóng.
     */
    private void drawBall(Ball ball) {
        gc.setFill(Color.web("#FFD166"));
        gc.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
    }

    /**
     * Vẽ toàn bộ khung hình dựa trên state hiện tại.
     */
    public void renderAll(
            GameState state,
            int score,
            int lives,
            List<Brick> bricks,
            Paddle paddle,
            Ball ball
    ) {
        drawBackground();
        drawHud(score, lives);

        // Bricks
        for (Brick b : bricks) {
            if (!b.isDestroyed()) drawBrick(b);
        }

        // Paddle & Ball
        drawPaddle(paddle);
        drawBall(ball);

        // Nhắc nhở trạng thái.
        switch (state) {
            case MENU -> drawBanner("Nhấn SPACE để bắt đầu");
            case PAUSED -> drawBanner("Tạm dừng — nhấn P để tiếp tục");
            case GAME_OVER -> drawBanner("Thua — nhấn SPACE để chơi lại");
            case WIN -> drawBanner("Chiến thắng! — nhấn SPACE");
            default -> {}
        }
    }
}
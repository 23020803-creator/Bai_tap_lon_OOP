package com.arkanoid.engine;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.Brick;
import com.arkanoid.model.effect.ExplosionEffect;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.powerup.PowerUp;
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
        gc.setFont(Font.font("Consolas", 16));
        gc.fillText("Scores: " + score, 16, 24);
        gc.fillText("Lives: " + lives, 150, 24);
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
        b.render(gc);
    }

    /**
     * Vẽ paddle.
     */
    private void drawPaddle(Paddle p) {
        if (p != null) p.render(gc);
    }

    /**
     * Vẽ ball - bóng.
     * @param balls nhiều bóng.
     */
    private void drawBalls(List<Ball> balls) {
        if (balls == null) return;
        for (Ball b : balls) {
            if (b != null) b.render(gc);
        }
    }

    /**
     * Vẽ toàn bộ khung hình dựa trên state hiện tại.
     * Lưu ý: thêm parameter explosions để vẽ hiệu ứng nổ.
     */
    public void renderAll(GameState state, int score, int lives,
                          List<Brick> bricks, Paddle paddle, List<Ball> balls,
                          List<PowerUp> powerUps, List<ExplosionEffect> explosions) {

        // xóa nền
        drawBackground();
        //vẽ thanh điểm và mạng
        drawHud(score, lives);

        // Bricks
        if (bricks != null) {
            for (Brick b : bricks) {
                if (!b.isDestroyed()) drawBrick(b);
            }
        }

        // PowerUps
        if (powerUps != null) {
            for (PowerUp p : powerUps) {
                p.render(gc);
            }
        }

        // Paddle & Ball
        drawPaddle(paddle);
        drawBalls(balls);

        // Vẽ hiệu ứng nổ gạch
        if (explosions != null) {
            for(ExplosionEffect  e : explosions){
                e.render(gc);
            }
        }

        // Nhắc nhở trạng thái.
        switch (state) {
            case MENU -> drawBanner("PRESS SPACE TO START");
            case PAUSED -> drawBanner("PAUSED – PRESS P TO RESUME");
            case GAME_OVER -> drawBanner("GAME OVER – PRESS SPACE TO RESTART");
            case WIN -> drawBanner("YOU WIN! – PRESS SPACE TO RESTART");
            default -> {}
        }
    }
}

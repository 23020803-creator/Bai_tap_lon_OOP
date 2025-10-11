package com.arkanoid.engine;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.powerup.*;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import java.util.*;

/**
 * Bộ điều khiển chính của game.
 * Quản lý logic cập nhật, trạng thái game, va chạm, điểm, mạng, v.v.
 */
public final class GameManager {
    private final List<Brick> bricks = new ArrayList<>();   // Danh sách gạch
    private final List<PowerUp> powerUps = new ArrayList<>(); // Danh sách PowerUp đang rơi
    private final AnimationTimer loop;                      // Vòng lặp chính của game
    private Paddle paddle;
    private Ball ball;
    private GameState state = GameState.MENU;               // Trạng thái hiện tại
    private int score = 0;
    private final Renderer renderer;
    private int lives = Config.START_LIVES;
    private volatile boolean leftPressed = false;
    private volatile boolean rightPressed = false;

    public GameManager(GraphicsContext gc) {
        this.renderer = new Renderer(gc);
        // Vòng lặp khung hình chính
        this.loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (state == GameState.RUNNING) {
                    updateGame();  // Cập nhật logic
                }
                renderer.renderAll(state, score, lives, bricks, paddle, ball, powerUps); // cập nhật đồ họa
            }
        };
    }

    /**
     * Bắt đầu vòng lặp game.
     */
    public void startLoop() {
        loop.start();
    }

    /**
     * Hiển thị màn hình menu chờ.
     */
    public void showMenu() {
        state = GameState.MENU;
        score = 0;
        lives = Config.START_LIVES;
        initPaddleAndBall();
    }

    /**
     * Tạo mới paddle và bóng.
     */
    private void initPaddleAndBall() {
        paddle = new Paddle(
                (Config.VIEW_WIDTH - Config.PADDLE_WIDTH) / 2.0,
                Config.VIEW_HEIGHT - 60,
                Config.PADDLE_WIDTH,
                Config.PADDLE_HEIGHT,
                Config.PADDLE_SPEED);

        ball = new Ball(
                paddle.getCenterX() - Config.BALL_SIZE / 2.0,
                paddle.getY() - Config.BALL_SIZE - 2,
                Config.BALL_SIZE,
                Config.BALL_SIZE,
                Config.BALL_SPEED);
    }

    /**
     * Khi nhấn phím SPACE — bắt đầu hoặc tiếp tục game.
     */
    public void handleSpace() {
        if (state == GameState.MENU || state == GameState.GAME_OVER || state == GameState.WIN) {
            startGame();
            return;
        }
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Dừng hoặc tiếp tục game bằng phím P.
     */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Khởi tạo lại toàn bộ màn chơi.
     */
    public void startGame() {
        score = 0;
        lives = Config.START_LIVES;
        state = GameState.RUNNING;
        initPaddleAndBall();

        // Sinh ma trận gạch
        bricks.clear();
        int offsetX = (Config.VIEW_WIDTH - (Config.BRICK_COLS * (Config.BRICK_WIDTH + Config.BRICK_GAP) - Config.BRICK_GAP)) / 2;
        int offsetY = 60;
        for (int r = 0; r < Config.BRICK_ROWS; r++) {
            for (int c = 0; c < Config.BRICK_COLS; c++) {
                int x = offsetX + c * (Config.BRICK_WIDTH + Config.BRICK_GAP);
                int y = offsetY + r * (Config.BRICK_HEIGHT + Config.BRICK_GAP);
                if ((r + c) % 3 == 0)
                    bricks.add(new StrongBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT));
                else
                    bricks.add(new NormalBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT));
            }
        }
        paddle.resetSize();
        ball.resetSpeed();
        powerUps.clear();
    }

    /**
     * Vòng cập nhật chính
     */
    public void updateGame() {
        // Di chuyển Paddle & cập nhật trạng thái
        int vx = 0;
        if (leftPressed)  vx -= paddle.getSpeed();
        if (rightPressed) vx += paddle.getSpeed();
        paddle.setX(Math.max(0, Math.min(Config.VIEW_WIDTH - paddle.getWidth(), paddle.getX() + vx)));
        paddle.update();

        // Di chuyển bóng & cập nhật trạng thái
        ball.update();

        // Xử lý va chạm tường
        if (ball.getX() <= 0 || ball.getRight() >= Config.VIEW_WIDTH) {
            ball.bounceHorizontal();
        }
        if (ball.getY() <= 0) {
            ball.bounceVertical();
        }

        // Va chạm với Paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.bounceOff(paddle);
        }

        // Va chạm với gạch
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            if (!b.isDestroyed() && ball.checkCollision(b)) {
                Rectangle2D inter = intersection(ball.getBounds(), b.getBounds());
                if (inter.getWidth() > inter.getHeight()) {
                    ball.bounceVertical();
                } else {
                    ball.bounceHorizontal();
                }
                b.takeHit();
                if (b.isDestroyed()) {
                    score += 100;
                    maybeSpawnPowerUp(b);
                    it.remove();
                }
                break;
            }
        }

        // PowerUp rơi xuống và kiểm tra ăn
        Iterator<PowerUp> pit = powerUps.iterator();
        while (pit.hasNext()) {
            PowerUp p = pit.next();
            p.update();
            if (p.getY() > Config.VIEW_HEIGHT) {
                pit.remove(); // rơi khỏi màn hình
                continue;
            }

            if (p.getBounds().intersects(paddle.getBounds())) {
                p.applyEffect(paddle, ball);
                if (p instanceof ExtraLifePowerUp) {
                    if(lives < Config.MAX_LIVES) {
                        lives++;
                    }
                }
                pit.remove();
            }
        }

        // Kiểm tra bóng rơi khỏi màn hình
        if (ball.getY() > Config.VIEW_HEIGHT) {
            lives--;
            if (lives <= 0) state = GameState.GAME_OVER;
            else ball.resetOnPaddle(paddle);
        }

        // Kiểm tra thắng
        if (bricks.isEmpty() && state == GameState.RUNNING) state = GameState.WIN;
    }

    /**
     * Tính giao nhau giữa 2 hình chữ nhật.
     */
    private Rectangle2D intersection(Rectangle2D a, Rectangle2D b) {
        double x1 = Math.max(a.getMinX(), b.getMinX());
        double y1 = Math.max(a.getMinY(), b.getMinY());
        double x2 = Math.min(a.getMaxX(), b.getMaxX());
        double y2 = Math.min(a.getMaxY(), b.getMaxY());
        if (x2 >= x1 && y2 >= y1)
            return new Rectangle2D(x1, y1, x2 - x1, y2 - y1);
        return Rectangle2D.EMPTY;
    }

    /**
     * Sinh PowerUp tại vị trí gạch bị phá.
     */
    private void maybeSpawnPowerUp(Brick source) {
        double centerX = source.getCenterX();
        double y = source.getCenterY();
        // Sinh ngẫu nhiên 3 loại PowerUp
        PowerUp p;
        if (Math.random() < 0.6) {
            return;
        } else if (Math.random() < 0.8) {
            // Tạo PowerUp mở rộng Paddle
            p = new ExpandPaddlePowerUp(centerX - 12, y, 24, 12);
        } else if (Math.random() < 0.9) {
            // Tạo PowerUp tăng tốc bóng
            p = new FastBallPowerUp(centerX - 12, y, 24, 12);
        }  else {
            // Tạo PowerUp thêm mạng chơi
            p = new ExtraLifePowerUp(centerX - 12, y, 24, 12);
        }
        powerUps.add(p);
    }


    // Setter cho cờ phím
    public void setLeftPressed(boolean v) {
        leftPressed = v;
    }

    public void setRightPressed(boolean v) {
        rightPressed = v;
    }
}


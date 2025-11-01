package com.arkanoid.engine;

import com.arkanoid.model.ball.Ball;
import com.arkanoid.model.brick.*;
import com.arkanoid.model.effect.ExplosionEffect;
import com.arkanoid.model.paddle.Paddle;
import com.arkanoid.model.powerup.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import com.arkanoid.engine.ScoreManager;

import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Bộ điều khiển chính của game.
 * Quản lý logic cập nhật, trạng thái game, va chạm, điểm, mạng, v.v.
 */
public final class GameManager {
    private final List<Brick> bricks = new ArrayList<>();               // Danh sách gạch
    private final List<PowerUp> powerUps = new ArrayList<>();           // Danh sách PowerUp đang rơi
    private final List<ExplosionEffect> explosions = new ArrayList<>(); // Hiệu ứng nổ tạm thời
    private final Timeline loop;                                        // Vòng lặp chính của game
    private Paddle paddle;
    private final List<Ball> balls = new ArrayList<>();                 // Danh sách bóng
    private GameState state = GameState.MENU;                           // Trạng thái hiện tại
    private int score = 0;
    private Image background;
    private final Renderer renderer;
    private int lives = Config.START_LIVES;
    private volatile boolean leftPressed = false;
    private volatile boolean rightPressed = false;
    private Stage stage;
    private Scene gameScene;
    public boolean isFromLevels = false; // true nếu chơi từ chế độ Levels
    private StackPane root; // lưu root để overlay có thể hiển thị
    private int currentLevel = 1;
    private static final int TOTAL_LEVELS = 8;

    /**
     * Setter cho cờ phím.
     */
    public void setLeftPressed(boolean v) {
        leftPressed = v;
    }

    public void setRightPressed(boolean v) {
        rightPressed = v;
    }

    /**
     * Getter cho danh sách bóng.
     */
    public List<Ball> getBalls() {
        return balls;
    }

    public void setRoot(StackPane root) {
        this.root = root;
    }


    public void setFromLevels(boolean fromLevels) {
        this.isFromLevels = fromLevels;
    }

    /**
     * Getter cho chế độ chơi
     */
    public boolean getFromLevels() {
        return isFromLevels;
    }

    /**
     * Getter cho Level hiện tại
     */
    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Liên kết Stage và Scene hiện tại cho GameManager, giúp chuyển giữa game, menu
     * và màn hình cài đặt.
     *
     * @param stage cửa sổ chính của game
     * @param scene scene hiện tại đang hiển thị game
     */
    public void setStageAndScene(Stage stage, Scene scene) {
        this.stage = stage;
        this.gameScene = scene;
    }

    /**
     * Tạm dừng game ngay lập tức.
     */
    public void pauseGame() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            SoundManager.stopAllBGM();
        }
    }

    /**
     *  Tiếp tục game sau khi pause.
     */
    public void resumeGame() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
            SoundManager.playBGM("BackgroundMusic1.mp3", true);
        }
    }

    public boolean isRunning() {
        return state == GameState.RUNNING;
    }

    public boolean isPaused() {
        return state == GameState.PAUSED;
    }

    /**
     * Màn chơi mới sau khi Win hoặc Level kế tiếp
     */
    public void nextLevel() {
        if (!isFromLevels) {
            // Chế độ random (từ Menu)
            startGameKeepScore(); // chơi tiếp, giữ điểm
            return;
        }

        // Sang level kế
        currentLevel++;
        if (currentLevel > TOTAL_LEVELS) {
            // Nếu đã hết level
            new MenuScreen(stage, this, gameScene).show();
            return;
        }

        // Load level tiếp theo từ file
        String nextFile = "/levels/Level" + currentLevel + ".txt";
        startLevelFromFile(nextFile);
    }

    /**
     * Lưu điểm sau khi Win để cộng dồn với màn chơi mới
     */
    public void startGameKeepScore() {
        int oldScore = ScoreManager.getCurrentScore();
        startGame();
        ScoreManager.add(oldScore);
    }

    public Image getBackground() {
        return background;
    }

    public GameManager(GraphicsContext gc) {
        this.renderer = new Renderer(gc);
        double frameDuration = 1000.0 / 60.0; // FPS = 60
        // Vòng lặp khung hình chính
        KeyFrame frame = new KeyFrame(Duration.millis(frameDuration), e -> {
            if (state == GameState.RUNNING) {
                updateGame();
            }
            renderer.renderAll(state, score, lives, bricks, paddle, balls, powerUps, explosions, background);
        });
        this.loop = new Timeline(frame);
        this.loop.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Bắt đầu vòng lặp game.
     */
    public void startLoop() {
        loop.play();
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
                Config.PADDLE_SPEED + Config.BALL_SPEED_MULTIPLIER
        );

        balls.clear();
        Ball b0 = new Ball(
                paddle.getCenterX() - Config.BALL_SIZE / 2.0,
                paddle.getY() - Config.BALL_SIZE - 2,
                Config.BALL_SIZE,
                Config.BALL_SIZE,
                Config.BALL_SPEED + Config.PADDLE_SPEED_MULTIPLIER
        );
        balls.add(b0);
    }

    /**
     * Method nhân bóng.
     */
    public void spawnClonedBalls(Ball ref, int count) {
        if (balls.size() >= 10) { // Giới hạn số bóng được nhân ra
            return;
        }
        for (int i = 0; i < count; i++) {
            Ball clone = new Ball(
                    ref.getX(), ref.getY(),
                    ref.getWidth(), ref.getHeight(),
                    Math.hypot(ref.getDx(), ref.getDy())
            );
            // Bóng tạo ra bắn lệch hướng & tốc độ để tách quỹ đạo
            double fx = (i == 0 ? 1.7 : 0.3);
            double fy = (i == 0 ? 0.3 : 1.7);
            clone.setDx(ref.getDx() * fx);
            clone.setDy(ref.getDy() * fy);
            balls.add(clone);
        }
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
            // Phát nhạc nền
            SoundManager.playBGM("BackgroundMusic1.mp3", true);
        }
    }

    /**
     * Dừng hoặc tiếp tục game bằng phím P.
     */
    public void togglePause() {
        if (state == GameState.RUNNING) {
            pauseGame();
        } else if (state == GameState.PAUSED) {
            resumeGame();
        }
    }


    /**
     * Khởi tạo màn chơi từ file txt thay vì sinh màn ngẫu nhiên
     */
    public void startLevelFromFile(String fileName) {
        isFromLevels = true;

        try {
            String num = fileName.replaceAll("\\D+", "");
            currentLevel = Integer.parseInt(num);
        } catch (Exception ignored) {
            currentLevel = 1;
        }

        ScoreManager.resetScore();
        score = 0;
        lives = Config.START_LIVES;
        state = GameState.RUNNING;
        initPaddleAndBall();
        bricks.clear();
        powerUps.clear();
        explosions.clear();

        try {
            String bgPath = "/images/backgrounds/bg_level" + currentLevel + ".png";
            background = new Image(Objects.requireNonNull(getClass().getResourceAsStream(bgPath)));
        } catch (Exception e) {
            // Nếu không có ảnh riêng thì dùng ảnh mặc định
            background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/backgrounds/bg_default.png")));
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream("/Levels/" + fileName))))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }

            int rows = lines.size();
            int cols = lines.getFirst().length();
            int offsetX = (Config.VIEW_WIDTH - (cols * (Config.BRICK_WIDTH + Config.BRICK_GAP) - Config.BRICK_GAP)) / 2;
            int offsetY = 60;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    char ch = lines.get(r).charAt(c);
                    double x = offsetX + c * (Config.BRICK_WIDTH + Config.BRICK_GAP);
                    double y = offsetY + r * (Config.BRICK_HEIGHT + Config.BRICK_GAP);

                    // Đọc ký hiệu gạch từ file
                    Brick b = switch (ch) {
                        case 'N' -> new NormalBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                        case 'S' -> new StrongBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                        case 'U' -> new UnbreakableBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                        case 'H' -> new HorizontalExplodeBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                        case 'V' -> new VerticalExplodeBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                        default -> null;
                    };
                    if (b != null) bricks.add(b);
                }
            }

        } catch (Exception e) {
            System.err.println("Không thể đọc level: " + fileName);
            e.printStackTrace();
            startGame(); // fallback: random map
            return;
        }

        paddle.resetSize();
        for (Ball b : balls) {
            b.resetSpeed();
        }
        SoundManager.stopAllBGM();
        SoundManager.playBGM("BackgroundMusic1.mp3", true);
    }

    /**
     * Khởi tạo lại toàn bộ màn chơi.
     * Sinh map động: hỗn hợp các loại gạch (NORMAL, STRONG, UNBREAKABLE, H_EXPLODE, V_EXPLODE)
     */
    public void startGame() {
        isFromLevels = false;
        currentLevel = 1;
        ScoreManager.resetScore();
        score = 0;
        lives = Config.START_LIVES;
        state = GameState.RUNNING;
        initPaddleAndBall();

        // Sinh ma trận gạch với tỉ lệ ngẫu nhiên
        bricks.clear();
        powerUps.clear();
        explosions.clear();

        background = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/backgrounds/bg_arcade.png")));

        int offsetX = (Config.VIEW_WIDTH - (Config.BRICK_COLS * (Config.BRICK_WIDTH
                + Config.BRICK_GAP) - Config.BRICK_GAP)) / 2;
        int offsetY = 60;
        Random rnd = new Random();

        for (int r = 0; r < Config.BRICK_ROWS; r++) {
            for (int c = 0; c < Config.BRICK_COLS; c++) {
                int x = offsetX + c * (Config.BRICK_WIDTH + Config.BRICK_GAP);
                int y = offsetY + r * (Config.BRICK_HEIGHT + Config.BRICK_GAP);

                double roll = rnd.nextDouble();
                Brick b;
                // Tỉ lệ cân gạch:
                // 0.00 - 0.55 : Normal (55%)
                // 0.55 - 0.75 : Strong (20%)
                // 0.75 - 0.85 : Unbreakable (10%)
                // 0.85 - 0.93 : Horizontal Explode (8%)
                // 0.93 - 1.00 : Vertical Explode (7%)
                if (roll < 0.55) {
                    b = new NormalBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                } else if (roll < 0.75) {
                    b = new StrongBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                } else if (roll < 0.85) {
                    b = new UnbreakableBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                } else if (roll < 0.93) {
                    b = new HorizontalExplodeBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                } else {
                    b = new VerticalExplodeBrick(x, y, Config.BRICK_WIDTH, Config.BRICK_HEIGHT);
                }
                bricks.add(b);
            }
        }

        paddle.resetSize();
        for (Ball b : balls) {
            b.resetSpeed();
        }

        // Phát nhạc nền khi chơi
        SoundManager.stopAllBGM();
        SoundManager.playBGM("BackgroundMusic1.mp3", true);
    }

    /**
     * Vòng cập nhật chính
     */
    public void updateGame() {
        // Di chuyển Paddle & cập nhật trạng thái
        int vx = 0;
        if (leftPressed) {
            vx -= (int) paddle.getSpeed();
        }
        if (rightPressed) {
            vx += (int) paddle.getSpeed();
        }
        paddle.setX(Math.max(0, Math.min(Config.VIEW_WIDTH - paddle.getWidth(), paddle.getX() + vx)));
        paddle.update();

        // Di chuyển bóng & cập nhật trạng thái
        for (Ball ball : new ArrayList<>(balls)) {
            ball.update();

            // Xử lý va chạm tường
            if (ball.getX() <= 0) {
                ball.setX(0);
                ball.bounceHorizontal();
                SoundManager.playSFX("HitBall_Anything.wav");
            }
            if (ball.getRight() >= Config.VIEW_WIDTH) {
                ball.setX(Config.VIEW_WIDTH - ball.getWidth());
                ball.bounceHorizontal();
                SoundManager.playSFX("HitBall_Anything.wav");
            }
            if (ball.getY() <= 0) {
                ball.setY(0);
                ball.bounceVertical();
                SoundManager.playSFX("HitBall_Anything.wav");
            }

            // Va chạm với Paddle
            if (ball.getBounds().intersects(paddle.getBounds())) {
                paddle.hitFlash();
                double oldSpeed = Math.hypot(ball.getDx(), ball.getDy()); // lưu lại vận tốc cũ
                ball.bounceOff(paddle);                                   // tính lại hướng bóng sau khi va chạm
                double newSpeed = Math.hypot(ball.getDx(), ball.getDy()); // tính vận tốc mới
                if (newSpeed > 0) {                                       // chuẩn hóa hướng, khôi phục tốc độ
                    ball.setDx(ball.getDx() / newSpeed * oldSpeed);
                    ball.setDy(ball.getDy() / newSpeed * oldSpeed);
                }
                SoundManager.playSFX("HitBall_Anything.wav");
            }

            // Va chạm với gạch
            for (Brick b : bricks) {
                if (!b.isDestroyed() && ball.checkCollision(b)) {
                    Rectangle2D inter = intersection(ball.getBounds(), b.getBounds());
                    if (inter.getWidth() > inter.getHeight()) {             // Va chạm theo chiều dọc
                        if (ball.getCenterY() < b.getCenterY()) {
                            ball.setY(b.getY() - ball.getHeight() - 0.5);
                        } else {
                            ball.setY(b.getBottom() + 0.5);
                        }
                        ball.bounceVertical();                              // Đảo hướng theo trục y (phản xạ)
                    } else {                                                // Va chạm theo chiều ngang
                        if (ball.getCenterX() < b.getCenterX()) {
                            ball.setX(b.getX() - ball.getWidth() - 0.5);
                        } else {
                            ball.setX(b.getRight() + 0.5);
                        }
                        ball.bounceHorizontal();                            // Đảo hướng theo trục x (phản xạ)
                    }

                    // Nếu gạch là Unbreakable thì chỉ bounce, không phá.
                    if (b instanceof UnbreakableBrick) {
                        SoundManager.playSFX("HitBall_UnBreakableBrick.mp3");
                    } else {
                        if (b instanceof HorizontalExplodeBrick || b instanceof VerticalExplodeBrick) {
                            SoundManager.playSFX("HitBall_ExplodeBrick.wav");
                        }
                        SoundManager.playSFX("HitBall_Anything.wav");
                        // Brick bình thường giảm HP.
                        b.takeHit();
                        if (b.isDestroyed()) {
                            int destroyed = handleBrickDestruction(b);
                            ScoreManager.add(destroyed * 100);
                            score = ScoreManager.getCurrentScore();
                        }
                    }
                    break;
                }
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
                if (p instanceof TripleBallPowerUp triple) {
                    // Gọi hiệu ứng nhân bóng
                    if (!balls.isEmpty()) {
                        triple.apply(this, balls.getFirst());
                    }
                } else if (p instanceof FastBallPowerUp fastBallPowerUp) {
                    for (Ball b : balls) {
                        fastBallPowerUp.applyEffect(paddle, b);
                    }
                } else {
                    // Các PowerUp thông thường
                    p.applyEffect(paddle, balls.isEmpty() ? null : balls.getFirst());
                }
                SoundManager.playSFX("HitPaddle_PowerUp.wav"); // Âm thanh hiệu ứng ăn PowerUp
                if (p instanceof ExtraLifePowerUp) {
                    if (lives < Config.MAX_LIVES) {
                        lives++;
                    }
                }
                pit.remove();
            }
        }

        // Cập nhật và dọn các hiệu ứng nổ
        Iterator<ExplosionEffect> eIt = explosions.iterator();
        while (eIt.hasNext()) {
            ExplosionEffect e = eIt.next();
            e.update();
            if (!e.isAlive()) eIt.remove();
        }

        // Bỏ các bóng rơi khỏi màn hình
        Iterator<Ball> bit = balls.iterator();
        while (bit.hasNext()) {
            Ball b = bit.next();
            if (b.getY() > Config.VIEW_HEIGHT) {
                bit.remove();
            }
        }

        // Khi hết bóng thì bắt đầu trừ mạng.
        if (balls.isEmpty()) {
            lives--;
            if (lives <= 0) {
                state = GameState.GAME_OVER;
                ScoreManager.saveScoreIfHigh();
                SoundManager.stopAllBGM();
                SoundManager.playBGM("GameOverMusic.mp3", false);

                javafx.application.Platform.runLater(() -> {
                    root.getChildren().add(new LoseOverlay(stage, this, root, isFromLevels));
                });
            }else {
                // Sinh thêm 1 bóng trên paddle
                Ball nb = new Ball(
                        paddle.getCenterX() - Config.BALL_SIZE / 2.0,
                        paddle.getY() - Config.BALL_SIZE - 2,
                        Config.BALL_SIZE, Config.BALL_SIZE, Config.BALL_SPEED
                );
                balls.add(nb);
            }
        }

        // Kiểm tra thắng
        boolean anyBreakableLeft = false;
        for (Brick b : bricks) {
            if (!(b instanceof UnbreakableBrick)) {
                anyBreakableLeft = true;
                break;
            }
        }
        // Nếu không còn gạch (tất cả đã bị loại hoặc chỉ còn unbreakable)
        boolean allDestroyedOrUnbreakable = true;
        for (Brick b : bricks) {
            if (!b.isDestroyed() && !(b instanceof UnbreakableBrick)) {
                allDestroyedOrUnbreakable = false;
                break;
            }
        }

        if ((bricks.isEmpty() || allDestroyedOrUnbreakable) && state == GameState.RUNNING) {
            ScoreManager.add(500);
            score = ScoreManager.getCurrentScore();
            state = GameState.WIN;
            SoundManager.stopAllBGM();
            SoundManager.playBGM("GameClearMusic.mp3", false);

            javafx.application.Platform.runLater(() -> {
                root.getChildren().add(new WinOverlay(stage, this, root, isFromLevels));
            });
        }

    }

    /**
     * Tính giao nhau giữa 2 hình chữ nhật.
     */
    private Rectangle2D intersection(Rectangle2D a, Rectangle2D b) {
        double x1 = Math.max(a.getMinX(), b.getMinX());
        double y1 = Math.max(a.getMinY(), b.getMinY());
        double x2 = Math.min(a.getMaxX(), b.getMaxX());
        double y2 = Math.min(a.getMaxY(), b.getMaxY());
        if (x2 >= x1 && y2 >= y1) {
            return new Rectangle2D(x1, y1, x2 - x1, y2 - y1);
        }
        return Rectangle2D.EMPTY;
    }

    /**
     * Xử lý phá hủy 1 viên gạch nguồn.
     * - Thu thập tất cả các gạch bị phá do hiệu ứng nổ.
     * - Thêm hiệu ứng nổ (ExplosionEffect) tại vị trí từng gạch.
     * - Sinh PowerUp cho mỗi viên gạch bị phá.
     * Trả về số lượng gạch thực sự bị phá.
     */
    private int handleBrickDestruction(Brick source) {
        Set<Brick> toDestroy = new HashSet<>();
        collectDestruction(source, toDestroy);

        // Thực hiện loại bỏ và sinh powerups
        int count = 0;
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            if (toDestroy.contains(b)) {
                double cx = b.getCenterX();
                double cy = b.getCenterY();
                explosions.add(new ExplosionEffect(cx, cy, 60, Math.min(b.getWidth(), b.getHeight()) ));

                // sinh powerup
                maybeSpawnPowerUp(b);

                it.remove();
                count++;
            }
        }
        return count;
    }

    /**
     * Thu thập đệ quy các gạch bị ảnh hưởng bởi hiệu ứng nổ bắt đầu từ 'b'.
     * - Nếu gặp HorizontalExplodeBrick -> thu thập toàn hàng (centerY giống nhau)
     * - Nếu gặp VerticalExplodeBrick -> thu thập toàn cột (centerX giống nhau)
     * - Nếu gặp UnbreakableBrick -> bỏ qua (không thể phá)
     * - Chain reaction: nếu trong hàng/cột gặp thêm gạch nổ khác, sẽ tiếp tục thu thập.
     */
    private void collectDestruction(Brick b, Set<Brick> collector) {
        if (b == null) {
            return;
        }
        if (collector.contains(b)) {
            return;
        }
        if (b instanceof UnbreakableBrick) { // không phá
            return;
        }
        collector.add(b);

        // nếu b là Explode Horizontal/Vertical -> thu thập thêm
        if (b instanceof HorizontalExplodeBrick) {
            double targetY = b.getCenterY();
            // thu thập mọi viên cùng hàng (so sánh centerY)
            for (Brick other : new ArrayList<>(bricks)) {
                if (other == b) continue;
                if (other instanceof UnbreakableBrick) continue;
                if (Math.abs(other.getCenterY() - targetY) <= (Config.BRICK_HEIGHT / 2.0 + 1.0)) {
                    collectDestruction(other, collector);
                }
            }
        } else if (b instanceof VerticalExplodeBrick) {
            double targetX = b.getCenterX();
            // thu thập mọi viên cùng cột (so sánh centerX)
            for (Brick other : new ArrayList<>(bricks)) {
                if (other == b) continue;
                if (other instanceof UnbreakableBrick) continue;
                if (Math.abs(other.getCenterX() - targetX) <= (Config.BRICK_WIDTH / 2.0 + 1.0)) {
                    collectDestruction(other, collector);
                }
            }
        }
    }

    /**
     * Sinh PowerUp tại vị trí gạch bị phá.
     */
    private void maybeSpawnPowerUp(Brick source) {
        double centerX = source.getCenterX();
        double y = source.getCenterY();
        // Sinh ngẫu nhiên 4 loại PowerUp
        PowerUp p;
        double r = Math.random();
        if (r < 0.65) {
            return;
        } else if (r < 0.7) {
            p = new TripleBallPowerUp(centerX - 12, y, 24, 12);
        } else if (r < 0.8) {
            // Tạo PowerUp mở rộng Paddle
            p = new ExpandPaddlePowerUp(centerX - 12, y, 24, 12);
        } else if (r < 0.9) {
            // Tạo PowerUp tăng tốc bóng
            p = new FastBallPowerUp(centerX - 12, y, 24, 12);
        }  else {
            // Tạo PowerUp thêm mạng chơi
            p = new ExtraLifePowerUp(centerX - 12, y, 24, 12);
        }
        powerUps.add(p);
    }
}

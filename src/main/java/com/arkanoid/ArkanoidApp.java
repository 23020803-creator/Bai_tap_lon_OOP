package com.arkanoid;

import com.arkanoid.engine.Config;
import com.arkanoid.engine.GameManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Điểm khởi chạy của ứng dụng Arkanoid.
 * Class này chịu trách nhiệm:
 * - Tạo cửa sổ JavaFX.
 * - Tạo Canvas và lấy GraphicsContext.
 * - Liên kết GameManager với Renderer.
 * - Gắn sự kiện bàn phím .
 * - Bắt đầu game loop.
 */
public final class ArkanoidApp extends Application {
    private GameManager gameManager;

    @Override
    public void start(Stage stage) {
        // Tạo Canvas theo kích thước cấu hình
        Canvas canvas = new Canvas(Config.VIEW_WIDTH, Config.VIEW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gameManager = new GameManager(gc);

        // Tạo Scene và gắn Canvas vào
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, Config.VIEW_WIDTH, Config.VIEW_HEIGHT);

        // Cấu hình Stage
        stage.setTitle("Arkanoid");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        //nhận sự kiện phím
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        // Gán sự kiện phím: di chuyển, bắt đầu/tạm dừng
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameManager.setLeftPressed(true);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameManager.setRightPressed(true);
            } else if (code == KeyCode.SPACE) {
                gameManager.handleSpace();   // MENU/WIN/GAME_OVER -> start, PAUSED -> resume
            } else if (code == KeyCode.P) {
                gameManager.togglePause();   // RUNNING <-> PAUSED
            }
        });

        scene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameManager.setLeftPressed(false);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameManager.setRightPressed(false);
            }
        });

        // Vào menu chờ và khởi động vòng lặp khung hình
        gameManager.showMenu();
        gameManager.startLoop();
    }

    /** khởi chạy JavaFX Application */
    public static void main(String[] args) {
        launch(args);
    }
}

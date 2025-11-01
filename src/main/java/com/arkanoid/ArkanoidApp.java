package com.arkanoid;

import com.arkanoid.engine.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Khởi tạo và cấu hình cửa sổ chính của Arkanoid.
 * Tạo canvas, gắn điều khiển bàn phím, hiển thị menu chính và vòng lặp game.
 */
public final class ArkanoidApp extends Application {
    private GameManager gameManager;
    private Scene gameScene;
    private MenuScreen menuScreen;

    @Override
    public void start(Stage stage) {
        // Tạo game canvas
        Canvas canvas = new Canvas(Config.VIEW_WIDTH, Config.VIEW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gameManager = new GameManager(gc);

        // StackPane để chồng lớp (canvas + overlay)
        StackPane root = new StackPane(canvas);
        gameScene = new Scene(root, Config.VIEW_WIDTH, Config.VIEW_HEIGHT);

        // Gán Stage & Scene cho GameManager
        gameManager.setStageAndScene(stage, gameScene);

        // Gắn phím sự kiện
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        gameScene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameManager.setLeftPressed(true);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameManager.setRightPressed(true);
            } else if (code == KeyCode.SPACE) {
                gameManager.handleSpace(); // MENU/WIN/GAME_OVER -> start, PAUSED -> resume
            } else if (code == KeyCode.P) {
                if (gameManager.isRunning()) {
                    gameManager.togglePause();
                    PauseOverlay overlay = new PauseOverlay(stage, gameManager, root);
                    root.getChildren().add(overlay);
                } else if (gameManager.isPaused()) {
                    root.getChildren().removeIf(node -> node instanceof PauseOverlay);
                    gameManager.togglePause();
                }
            }
        });

        gameScene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameManager.setLeftPressed(false);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameManager.setRightPressed(false);
            }
        });

        // Tạo menu chính
        menuScreen = new MenuScreen(stage, gameManager, gameScene);

        // Cấu hình cửa sổ
        stage.setTitle("Arkanoid");
        stage.setResizable(false);

        // Mở menu khi bắt đầu
        menuScreen.show();

        // Khởi động vòng lặp game (ẩn)
        gameManager.startLoop();
    }

    /** Khởi chạy JavaFX Application */
    public static void main(String[] args) {
        launch(args);
    }
}

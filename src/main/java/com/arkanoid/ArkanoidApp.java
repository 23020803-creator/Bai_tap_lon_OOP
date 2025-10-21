package com.arkanoid;

import com.arkanoid.engine.Config;
import com.arkanoid.engine.GameManager;
import com.arkanoid.engine.MenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 Điểm khởi chạy của ứng dụng Arkanoid.
 Class này chịu trách nhiệm:
 Tạo cửa sổ JavaFX.
 Quản lý chuyển đổi giữa Menu và Game.
 Gắn sự kiện bàn phím.
 Bắt đầu vòng lặp game.
 */
public final class ArkanoidApp extends Application {
    private GameManager gameManager;
    private Scene gameScene;
    private MenuScreen menuScreen;

    @Override
    public void start(Stage stage) {
// ====== TẠO GAME SCENE ======
        Canvas canvas = new Canvas(Config.VIEW_WIDTH, Config.VIEW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gameManager = new GameManager(gc);

        StackPane gameRoot = new StackPane(canvas);
        gameScene = new Scene(gameRoot, Config.VIEW_WIDTH, Config.VIEW_HEIGHT);

        // ====== GẮN SỰ KIỆN PHÍM ======
        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        gameScene.setOnKeyPressed(e -> {
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

        gameScene.setOnKeyReleased(e -> {
            KeyCode code = e.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                gameManager.setLeftPressed(false);
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                gameManager.setRightPressed(false);
            }
        });

        // ====== TẠO MENU SCENE ======
        menuScreen = new MenuScreen(stage, gameManager, gameScene);

        // ====== CẤU HÌNH STAGE BAN ĐẦU ======
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
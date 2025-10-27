package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Màn hình cài đặt (Settings Screen).
 * Cho phép người chơi:
 *   Điều chỉnh âm lượng nhạc nền (BGM)
 *   Điều chỉnh âm lượng hiệu ứng (SFX)
 *   Chọn độ khó của trò chơi (Easy / Normal / Hard)
 * Hiển thị theo phong cách retro giống Menu chính.
 */
public class SettingScreen {

    private final Stage stage;
    private final GameManager gameManager;
    private final Scene gameScene;

    public SettingScreen(Stage stage, GameManager gameManager, Scene gameScene) {
        this.stage = stage;
        this.gameManager = gameManager;
        this.gameScene = gameScene;
    }

    /**
     * Hiển thị màn hình cài đặt.
     */
    public void show() {
        // Load phông chữ retro.
        Font retroFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 16);
        Font retroTitle = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 26);

        // Hiển thị tiêu đề.
        Label title = new Label("SETTINGS");
        title.setFont(retroTitle);
        title.setTextFill(Color.CYAN);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.DEEPSKYBLUE);
        glow.setRadius(15);
        glow.setSpread(0.5);
        title.setEffect(glow);

        // Âm lượng nhạc BGM.
        Label bgmLabel = new Label("MUSIC VOLUME: ");
        bgmLabel.setFont(retroFont);
        bgmLabel.setTextFill(Color.WHITE);
        bgmLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider bgmSlider = createSlider(Config.BGM_VOLUME, val -> {
            Config.BGM_VOLUME = val;
            SoundManager.setBGMVolume(val);
        });
        HBox bgmRow = new HBox(20, bgmLabel, bgmSlider);
        bgmRow.setAlignment(Pos.CENTER_LEFT);

        // Âm lượng hiệu ứng âm thanh SFX.
        Label sfxLabel = new Label("SOUND EFFECTS:");
        sfxLabel.setFont(retroFont);
        sfxLabel.setTextFill(Color.WHITE);
        sfxLabel.setAlignment(Pos.CENTER_RIGHT);
        Slider sfxSlider = createSlider(Config.SFX_VOLUME, val -> {
            Config.SFX_VOLUME = val;
            SoundManager.setSFXVolume(val);
        });
        HBox sfxRow = new HBox(20, sfxLabel, sfxSlider);
        sfxRow.setAlignment(Pos.CENTER_LEFT);

        // Độ khó
        Label diffLabel = new Label("DIFFICULTY:");
        diffLabel.setFont(retroFont);
        diffLabel.setTextFill(Color.WHITE);
        diffLabel.setAlignment(Pos.CENTER_RIGHT);
        ComboBox<String> diffBox = new ComboBox<>();
        diffBox.getItems().addAll("EASY", "NORMAL", "HARD");
        diffBox.setValue(Config.CURRENT_DIFFICULTY.name());
        diffBox.setOnAction(e -> updateDifficulty(diffBox.getValue()));
        diffBox.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 12px;");
        HBox diffRow = new HBox(20, diffLabel, diffBox);
        sfxRow.setAlignment(Pos.CENTER_LEFT);

        // Nút quay lại
        Button backButton = new Button("BACK TO MENU");
        backButton.setFont(retroFont);
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #0040ff, #00bfff);" +
                        "-fx-background-radius: 15; -fx-padding: 10 25 10 25;"
        );
        backButton.setOnAction(e -> new MenuScreen(stage, gameManager, gameScene).show());

        // Layout chính
        VBox root = new VBox(45, title, bgmRow, sfxRow, diffRow, backButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #000000;");

        // Tạo Scene
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Arkanoid");
        stage.show();
    }

    /**
     *  Cập nhật độ khó.
     */
    private void updateDifficulty(String value) {
        switch (value) {
            case "EASY":
                Config.CURRENT_DIFFICULTY = Config.Difficulty.EASY;
                Config.BALL_SPEED_MULTIPLIER = 0.8; // giảm tốc độ ball 20%
                Config.PADDLE_SPEED_MULTIPLIER = 1.2; // tăng tốc độ paddle 20%
                break;
            case "HARD":
                Config.CURRENT_DIFFICULTY = Config.Difficulty.HARD;
                Config.BALL_SPEED_MULTIPLIER = 1.3; // tăng tốc độ ball 30%
                Config.PADDLE_SPEED_MULTIPLIER = 0.9; // giảm tốc độ paddle 10%
                break;
            default:
                Config.CURRENT_DIFFICULTY = Config.Difficulty.NORMAL;
                Config.BALL_SPEED_MULTIPLIER = 1.0;
                Config.PADDLE_SPEED_MULTIPLIER = 1.0;
                break;
        }
    }

    /**
     *  Tạo thanh trượt chung cho BGM và SFX.
     */
    private Slider createSlider(double initialValue, java.util.function.DoubleConsumer listener) {
        Slider slider = new Slider(0, 1, initialValue);
        slider.setPrefWidth(350);
        slider.valueProperty().addListener((obs, oldVal, newVal)
                -> listener.accept(newVal.doubleValue()));
        slider.setStyle("-fx-control-inner-background: #222; -fx-accent: #00bfff;");
        return slider;
    }
}

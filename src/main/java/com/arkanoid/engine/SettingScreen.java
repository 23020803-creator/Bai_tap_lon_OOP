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
    private static final Font RETRO_FONT = Font.loadFont(
            SettingScreen.class.getResourceAsStream("/fonts/PressStart2P.ttf"), 16);
    private static final Font RETRO_TITLE = Font.loadFont(
            SettingScreen.class.getResourceAsStream("/fonts/PressStart2P.ttf"), 26);
    /**
     * Khởi tạo màn hình cài đặt.
     */
    public SettingScreen(Stage stage, GameManager gameManager, Scene gameScene) {
        this.stage = stage;
        this.gameManager = gameManager;
        this.gameScene = gameScene;
    }

    /**
     * Hiển thị màn hình cài đặt.
     */
    public void show() {
        // Hiển thị tiêu đề
        Label title = new Label("SETTINGS");
        title.setFont(RETRO_TITLE);
        title.setTextFill(Color.CYAN);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.DEEPSKYBLUE);
        glow.setRadius(15);
        glow.setSpread(0.5);
        title.setEffect(glow);

        // Âm lượng nhạc BGM.
        Label bgmLabel = new Label("MUSIC VOLUME: ");
        bgmLabel.setFont(RETRO_FONT);
        bgmLabel.setTextFill(Color.WHITE);
        Slider bgmSlider = createSlider(Config.BGM_VOLUME, val -> {
            Config.BGM_VOLUME = val;
            SoundManager.setBGMVolume(val);
        });
        HBox bgmRow = new HBox(20, bgmLabel, bgmSlider);
        bgmRow.setAlignment(Pos.CENTER_LEFT);

        // Âm lượng hiệu ứng âm thanh SFX.
        Label sfxLabel = new Label("SOUND EFFECTS:");
        sfxLabel.setFont(RETRO_FONT);
        sfxLabel.setTextFill(Color.WHITE);
        Slider sfxSlider = createSlider(Config.SFX_VOLUME, val -> {
            Config.SFX_VOLUME = val;
            SoundManager.setSFXVolume(val);
        });
        HBox sfxRow = new HBox(20, sfxLabel, sfxSlider);
        sfxRow.setAlignment(Pos.CENTER_LEFT);

        // Độ khó
        Label diffLabel = new Label("DIFFICULTY:");
        diffLabel.setFont(RETRO_FONT);
        diffLabel.setTextFill(Color.WHITE);
        diffLabel.setAlignment(Pos.CENTER_RIGHT);
        ComboBox<String> diffBox = new ComboBox<>();
        diffBox.getItems().addAll("EASY", "NORMAL", "HARD");
        diffBox.setValue(Config.CURRENT_DIFFICULTY.name());
        diffBox.setOnAction(e -> updateDifficulty(diffBox.getValue()));
        diffBox.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 12px; -fx-padding: 3 0 0 0");
        HBox diffRow = new HBox(20, diffLabel, diffBox);
        sfxRow.setAlignment(Pos.CENTER_LEFT);

        // Nút quay lại
        Button backButton = new Button("BACK TO MENU");
        backButton.setFont(RETRO_FONT);
        backButton.setTextFill(Color.WHITE);
        // Nút "BACK TO MENU" với hiệu ứng màu và viền trắng
        backButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0033cc, #00aaff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;"
                        + "-fx-padding: 14 35 10 35;" + "-fx-font-weight: bold;"
        );
        // Hiệu ứng sáng hơn khi di chuột vào
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0044ff, #33ccff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;" + "-fx-padding: 14 35 10 35;"
        ));
        // Trở lại màu gốc khi rời chuột khỏi nút
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0033cc, #00aaff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;" + "-fx-padding: 14 35 10 35;"
        ));
        backButton.setOnAction(e -> new MenuScreen(stage, gameManager, gameScene).show());

        // Layout chính
        VBox settingsBox = new VBox(35, bgmRow, sfxRow, diffRow);
        settingsBox.setAlignment(Pos.CENTER);
        HBox backBox = new HBox(backButton);
        backBox.setAlignment(Pos.CENTER);
        VBox root = new VBox(45, title, settingsBox, backBox);
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
                Config.BALL_SPEED_MULTIPLIER = -2; // giảm tốc độ ball đi 2
                Config.PADDLE_SPEED_MULTIPLIER = 0; // giữ nguyên tốc độ paddle
                break;
            case "HARD":
                Config.CURRENT_DIFFICULTY = Config.Difficulty.HARD;
                Config.BALL_SPEED_MULTIPLIER = 4; // tăng tốc độ ball thêm 4
                Config.PADDLE_SPEED_MULTIPLIER = 2; // tăng tốc độ paddle thêm 2
                break;
            default:
                Config.CURRENT_DIFFICULTY = Config.Difficulty.NORMAL;
                Config.BALL_SPEED_MULTIPLIER = 0;
                Config.PADDLE_SPEED_MULTIPLIER = 0;
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

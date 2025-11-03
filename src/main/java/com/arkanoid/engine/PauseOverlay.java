package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Màn hình Pause hiển thị khi nhấn P.
 * Hiển thị các tùy chọn âm thanh, độ khó, Resume và Back to Menu.
 */
public final class PauseOverlay extends VBox {

    public PauseOverlay(Stage stage, GameManager gm, StackPane root) {

        Font retroFont = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 16);
        Font retroTitle = Font.loadFont(
                getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 26);

        // Hiển thị tiêu đề
        Label title = new Label("PAUSED");
        title.setFont(retroTitle);
        title.setTextFill(Color.CYAN);
        DropShadow glow = new DropShadow(20, Color.DEEPSKYBLUE);
        glow.setSpread(0.5);
        title.setEffect(glow);

        // Thanh trượt chỉnh âm lượng nhạc nền (BGM)
        Label bgmLabel = new Label("MUSIC VOLUME: ");
        bgmLabel.setFont(retroFont);
        bgmLabel.setTextFill(Color.WHITE);
        Slider bgmSlider = createSlider(Config.BGM_VOLUME, val -> {
            Config.BGM_VOLUME = val;
            SoundManager.setBGMVolume(val);
        });

        HBox bgmRow = new HBox(20, bgmLabel, bgmSlider);
        bgmRow.setAlignment(Pos.CENTER_LEFT);

        Label sfxLabel = new Label("SOUND EFFECTS:");
        sfxLabel.setFont(retroFont);
        sfxLabel.setTextFill(Color.WHITE);
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

        ComboBox<String> diffBox = new ComboBox<>();
        diffBox.getItems().addAll("EASY", "NORMAL", "HARD");
        diffBox.setValue(Config.CURRENT_DIFFICULTY.name());
        diffBox.setStyle("-fx-font-family: 'Press Start 2P'; -fx-font-size: 12px; -fx-padding: 3 0 0 0");
        diffBox.setOnAction(e -> updateDifficulty(diffBox.getValue()));

        HBox diffRow = new HBox(20, diffLabel, diffBox);
        diffRow.setAlignment(Pos.CENTER_LEFT);

        VBox settingsBox = new VBox(25, bgmRow, sfxRow, diffRow);
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setPadding(new Insets(10, 0, 20, 0));

        // Nút tiếp tục
        Button resumeBtn = createButton("RESUME", retroFont);
        resumeBtn.setOnAction(e -> {
            gm.resumeGame();
            root.getChildren().remove(this);
        });

        // Nút quay lại
        Button backBtn = createButton("BACK TO MENU", retroFont);
        backBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            new MenuScreen(stage, gm, root.getScene()).show();
        });

        VBox btnBox = new VBox(20, resumeBtn, backBtn);
        btnBox.setAlignment(Pos.CENTER);

        // Kiểu lớp phủ mờ trên background
        setAlignment(Pos.CENTER);
        setSpacing(40);
        setPadding(new Insets(60));
        setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(title, settingsBox, btnBox);
    }

    /**
     * Tạo thanh trượt âm lượng.
     */
    private Slider createSlider(double init, java.util.function.DoubleConsumer listener) {
        Slider s = new Slider(0, 1, init);
        s.setPrefWidth(350);
        s.valueProperty().addListener((obs, old, val)
                -> listener.accept(val.doubleValue()));
        s.setStyle("-fx-control-inner-background: #222; -fx-accent: #00bfff;");
        return s;
    }

    /**
     * Tạo nút bấm.
     */
    private Button createButton(String text, Font font) {
        Button btn = new Button(text);
        btn.setFont(font);
        btn.setTextFill(Color.WHITE);
        // Nút bấm với hiệu ứng màu và viền trắng
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0033cc, #00aaff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;"
                        + "-fx-padding: 14 35 10 35;" + "-fx-font-weight: bold;"
        );
        // Hiệu ứng sáng hơn khi di chuột vào
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0044ff, #33ccff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;" + "-fx-padding: 14 35 10 35;"
        ));
        // Trở lại màu gốc khi rời chuột khỏi nút
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #0033cc, #00aaff);"
                        + "-fx-background-radius: 8;" + "-fx-border-color: white;"
                        + "-fx-border-width: 4;" + "-fx-border-radius: 8;" + "-fx-padding: 14 35 10 35;"
        ));
        return btn;
    }

    /**
     * Cập nhật độ khó.
     */
    private void updateDifficulty(String value) {
        switch (value) {
            case "EASY" -> {
                Config.CURRENT_DIFFICULTY = Config.Difficulty.EASY;
                Config.BALL_SPEED_BONUS = -2;
                Config.PADDLE_SPEED_BONUS = 0;
            }
            case "HARD" -> {
                Config.CURRENT_DIFFICULTY = Config.Difficulty.HARD;
                Config.BALL_SPEED_BONUS = 4;
                Config.PADDLE_SPEED_BONUS = 2;
            }
            default -> {
                Config.CURRENT_DIFFICULTY = Config.Difficulty.NORMAL;
                Config.BALL_SPEED_BONUS = 0;
                Config.PADDLE_SPEED_BONUS = 0;
            }
        }
    }
}

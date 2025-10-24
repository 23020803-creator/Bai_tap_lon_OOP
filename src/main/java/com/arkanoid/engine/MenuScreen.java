package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class MenuScreen {
    private final Stage stage;
    private final GameManager gameManager;
    private final Scene gameScene;

    public MenuScreen(Stage stage, GameManager gameManager, Scene gameScene) {
        this.stage = stage;
        this.gameManager = gameManager;
        this.gameScene = gameScene;
    }

    public void show() {
        // Phát nhạc menu.
        com.arkanoid.engine.SoundManager.stopAllBGM();
        com.arkanoid.engine.SoundManager.playBGM("OpeningMusic.mp3", true);
        // --- Nền menu ---
        Image bg = new Image(getClass().getResource("/images_menuscreen/Background.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(800, 600, false, false, false, false));

        // --- Cột chính giữa ---
        VBox centerMenu = new VBox(-50);
        centerMenu.setAlignment(Pos.CENTER);

        Button playButton = createButton("PLAY", "/images_menuscreen/PlayButton.png", 300, 120);
        playButton.setOnAction(e -> {
            playClick();
            gameManager.startGame();
            stage.setScene(gameScene);
            gameScene.getRoot().requestFocus();
        });

        Button levelButton = createButton("LEVELS", "/images_menuscreen/LevelsButton.png", 300, 120);
        Button settingButton = createButton("SETTING", "/images_menuscreen/SettingButton.png", 300, 120);
        Button exitButton = createButton("EXIT", "/images_menuscreen/ExitButton.png", 300, 120);
        exitButton.setOnAction(e -> {
            playClick();
            stage.close();
        });

        centerMenu.getChildren().addAll(playButton, levelButton, settingButton, exitButton);

        // --- Nút góc trên bên phải (Ranking) ---
        HBox topRight = new HBox();
        topRight.setAlignment(Pos.TOP_RIGHT);
        topRight.setPadding(new Insets(10, 20, 0, 0));

        Button rankingButton = createButton("", "/images_menuscreen/RankingButton.png", 100, 100);
        rankingButton.setPrefSize(60, 60);
        topRight.getChildren().add(rankingButton);

        // --- Gộp bố cục ---
        BorderPane root = new BorderPane();
        root.setBackground(new Background(bgImage));
        root.setCenter(centerMenu);
        root.setTop(topRight);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void playClick() {
        try {
            AudioClip click = new AudioClip(getClass().getResource("/sounds/click.wav").toExternalForm());
            click.play();
        } catch (Exception ignored) {
        }
    }

    private Button createButton(String text, String imagePath, double w, double h) {
        Button btn = new Button(text);
        btn.setPrefSize(w, h);
        btn.setMinSize(w, h);
        btn.setMaxSize(w, h);

        // Load image vào ImageView để làm "graphic" cho button
        try {
            Image img = new Image(getClass().getResource(imagePath).toExternalForm());
            javafx.scene.image.ImageView iv = new javafx.scene.image.ImageView(img);
            iv.setFitWidth(w);
            iv.setFitHeight(h);
            iv.setPreserveRatio(false); // hoặc true nếu muốn giữ tỉ lệ
            btn.setGraphic(iv);
        } catch (Exception ignored) {
            // nếu thiếu file ảnh thì vẫn hiển thị text (dễ debug)
        }

        // Ẩn hoàn toàn text (vẫn giữ text trong code nếu cần)
        btn.setText(""); // để chắc chắn chữ không hiển thị

        // Làm nền button trong suốt, bỏ padding mặc định
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0;" // tránh padding khiến ảnh bị "co" lại
        );

        // Hiệu ứng hover (ít mờ khi hover)
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));

        return btn;
    }


}

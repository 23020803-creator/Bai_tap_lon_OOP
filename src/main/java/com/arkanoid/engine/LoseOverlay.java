package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public final class LoseOverlay extends VBox {

    public LoseOverlay(Stage stage, GameManager gm, StackPane root, boolean isFromLevels) {

        Font retroFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 16);
        Font retroTitle = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 26);

        // Tiêu đề “GAME OVER”
        Label title = new Label("GAME OVER");
        title.setFont(retroTitle);
        title.setTextFill(Color.RED);
        DropShadow glow = new DropShadow(20, Color.RED);
        glow.setSpread(0.5);
        title.setEffect(glow);

        // Hiển thị điểm số
        Label scoreLabel = new Label("YOUR SCORE: " + ScoreManager.getCurrentScore());
        scoreLabel.setFont(retroFont);
        scoreLabel.setTextFill(Color.WHITE);

        // Nút Try Again
        Button retryBtn = createButton("TRY AGAIN", retroFont);
        retryBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            ScoreManager.saveScoreIfHigh();
            if (gm.getFromLevels()) {
                String currentFile = "/levels/Level" + gm.getCurrentLevel() + ".txt";
                gm.startLevelFromFile(currentFile);
            } else {
                gm.startGame();
            }
        });

        // Nút quay lại menu
        Button backBtn = createButton("BACK TO MENU", retroFont);
        backBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            ScoreManager.saveScoreIfHigh();
            new MenuScreen(stage, gm, root.getScene()).show();
        });

        VBox btnBox = new VBox(20, retryBtn, backBtn);
        btnBox.setAlignment(Pos.CENTER);

        setAlignment(Pos.CENTER);
        setSpacing(40);
        setPadding(new Insets(60));
        setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.75), CornerRadii.EMPTY, Insets.EMPTY)));
        getChildren().addAll(title, scoreLabel, btnBox);
    }

    private Button createButton(String text, Font font) {
        Button btn = new Button(text);
        btn.setFont(font);
        btn.setTextFill(Color.WHITE);
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #cc0000, #990000);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;" +
                        "-fx-font-weight: bold;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #ff3333, #cc0000);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #cc0000, #990000);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;"
        ));
        return btn;
    }
}

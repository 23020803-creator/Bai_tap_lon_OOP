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

public final class WinOverlay extends VBox {

    public WinOverlay(Stage stage, GameManager gm, StackPane root, boolean isFromLevels) {

        Font retroFont = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 16);
        Font retroTitle = Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 26);

        // Tiêu đề “YOU WIN!”
        Label title = new Label("YOU WIN!");
        title.setFont(retroTitle);
        title.setTextFill(Color.LIMEGREEN);
        DropShadow glow = new DropShadow(20, Color.LIMEGREEN);
        glow.setSpread(0.5);
        title.setEffect(glow);

        // Hiển thị điểm số
        Label scoreLabel = new Label("YOUR SCORE: " + ScoreManager.getCurrentScore());
        scoreLabel.setFont(retroFont);
        scoreLabel.setTextFill(Color.WHITE);

        // Nút Continue
        Button continueBtn = createButton("CONTINUE", retroFont);
        continueBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            ScoreManager.saveScoreIfHigh();
            if (gm.getFromLevels()) {
                gm.nextLevel();
            } else {
                gm.startGameKeepScore();
            }
        });


        // Nút quay lại menu
        Button backBtn = createButton("BACK TO MENU", retroFont);
        backBtn.setOnAction(e -> {
            root.getChildren().remove(this);
            ScoreManager.saveScoreIfHigh();
            new MenuScreen(stage, gm, root.getScene()).show();
        });

        VBox btnBox = new VBox(20, continueBtn, backBtn);
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
                "-fx-background-color: linear-gradient(to bottom, #00cc33, #009933);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;" +
                        "-fx-font-weight: bold;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #00ff66, #00cc33);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #00cc33, #009933);" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 8;" +
                        "-fx-padding: 14 35 10 35;"
        ));
        return btn;
    }
}

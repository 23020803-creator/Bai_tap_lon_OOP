package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class RankingScreen {
    private final Stage stage;
    private final GameManager gameManager;
    private final Scene gameScene;

    public RankingScreen(Stage stage, GameManager gameManager, Scene gameScene) {
        this.stage = stage;
        this.gameManager = gameManager;
        this.gameScene = gameScene;
    }

    public void show() {
        Image bg = new Image(getClass().getResource("/images/levelselectscreen/LevelSelectScreen.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(
                bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(800, 600, false, false, false, false)
        );

        BorderPane root = new BorderPane();
        root.setBackground(new Background(bgImage));

        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 28));
        gc.setFill(Color.web("#FFD700"));
        gc.fillText(" TOP 10 SCORES ", 200, 80);

        List<Integer> scores = ScoreManager.loadScores();
        gc.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P.ttf"), 18));
        gc.setFill(Color.WHITE);

        int y = 180;
        int rank = 1;
        for (int score : scores) {
            gc.fillText(rank + ". " + score, 320, y);
            y += 35;
            rank++;
        }

        Button backButton = createButton("/images/levelselectscreen/BackButton.png", 100, 100);
        backButton.setOnAction(e -> {
            new MenuScreen(stage, gameManager, gameScene).show();
        });

        StackPane stackPane = new StackPane(canvas, backButton);
        StackPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backButton, new Insets(0, 0, 20, 20));

        root.setCenter(stackPane);
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    private Button createButton(String path, double w, double h) {
        Button btn = new Button();
        btn.setPrefSize(w, h);
        try {
            Image img = new Image(getClass().getResource(path).toExternalForm());
            ImageView iv = new ImageView(img);
            iv.setFitWidth(w);
            iv.setFitHeight(h);
            btn.setGraphic(iv);
        } catch (Exception ignored) {}
        btn.setStyle("-fx-background-color: transparent;");
        return btn;
    }
}

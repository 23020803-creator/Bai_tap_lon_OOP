package com.arkanoid.engine;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Màn hình chọn level — hiển thị danh sách các cấp độ từ 1 đến total_levels.
 * Khi click vào 1 nút level, gọi GameManager.startLevelFromFile(levelName + ".txt")
 * và chuyển sang màn chơi chính.
 */
public class LevelSelectScreen {
    private final Stage stage;
    private final GameManager gameManager;
    private final Scene gameScene;

    // Số lượng level, có thể chỉnh tùy ý
    private static final int TOTAL_LEVELS = 8;

    public LevelSelectScreen(Stage stage, GameManager gameManager, Scene gameScene) {
        this.stage = stage;
        this.gameManager = gameManager;
        this.gameScene = gameScene;
    }

    public void show() {
        SoundManager.stopAllBGM();
        SoundManager.playBGM("OpeningMusic.mp3", true);

        Image bg = new Image(getClass().getResource("/images/levelselectscreen/LevelSelectScreen.png").toExternalForm());
        BackgroundImage bgImage = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                new BackgroundSize(800, 600, false, false, false, false));

        // Dùng GridPane thay cho VBox
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(30); // khoảng cách ngang
        grid.setVgap(30); // khoảng cách dọc
        grid.setPadding(new Insets(40, 0, 40, 0));

        // Tạo các nút Level 1 - TOTAL_LEVELS
        int col = 0;
        int row = 0;
        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            String levelName = "Level" + i;
            Button btn = createLevelButton(levelName, "/images/levelselectscreen/Level" + i + "Button.png", 120, 120);
            btn.setOnAction(e -> {
                playClick();
                gameManager.startLevelFromFile(levelName + ".txt");
                stage.setScene(gameScene);
                gameScene.getRoot().requestFocus();
            });

            grid.add(btn, col, row);
            col++;
            if (col >= 4) { // 4 nút mỗi hàng
                col = 0;
                row++;
            }
        }

        // Nút Back ở góc trên trái
        Button backButton = createLevelButton("BACK", "/images/levelselectscreen/BackButton.png", 100, 100);
        backButton.setOnAction(e -> {
            playClick();
            MenuScreen menu = new MenuScreen(stage, gameManager, gameScene);
            menu.show();
        });

        HBox topLeft = new HBox(backButton);
        topLeft.setAlignment(Pos.TOP_LEFT);
        topLeft.setPadding(new Insets(10, 0, 0, 10));

        // Gộp bố cục
        BorderPane root = new BorderPane();
        root.setBackground(new Background(bgImage));
        root.setCenter(grid);
        root.setTop(topLeft);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button createLevelButton(String text, String imagePath, double w, double h) {
        Button btn = new Button(text);
        btn.setPrefSize(w, h);
        btn.setMinSize(w, h);
        btn.setMaxSize(w, h);

        try {
            Image img = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView iv = new ImageView(img);
            iv.setFitWidth(w);
            iv.setFitHeight(h);
            iv.setPreserveRatio(false);
            btn.setGraphic(iv);
        } catch (Exception ignored) {

        }

        btn.setText(""); // không hiển thị chữ
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-padding: 0;");
        btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
        btn.setOnMouseExited(e -> btn.setOpacity(1.0));

        return btn;
    }

    private void playClick() {
        try {
            javafx.scene.media.AudioClip click = new javafx.scene.media.AudioClip(
                    getClass().getResource("/sounds/click.wav").toExternalForm());
            click.play();
        } catch (Exception ignored) {}
    }
}

package project.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MinesweeperApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Image mineImg = new Image(getClass().getResourceAsStream("/project/img/MineIcon50px.png"));
        Parent root = FXMLLoader.load((getClass().getResource("/project/MainScene.fxml")));
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.getIcons().add(mineImg);
        primaryStage.setScene(new Scene(root, MSController.CELL_SIZE * MSController.X_SIZE,
                MSController.CELL_SIZE * MSController.Y_SIZE + 30));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

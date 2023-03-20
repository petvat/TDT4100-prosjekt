package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MinesweeperApp extends Application {
    // gi nåde i start
    // VH, VW, CELL_SIZE
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load((getClass().getResource("/project/GameScene1.fxml")));
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(new Scene(root, Controller.VW, Controller.VH + 30));
        primaryStage.show();
    }

    // EIGE GAME CLASS?
    public static void main(String[] args) {
        launch(args);
    }
}

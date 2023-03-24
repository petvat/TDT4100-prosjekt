package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import scrapped.Controller;

public class MinesweeperApp extends Application {
    private Image mineImg = new Image(getClass().getResourceAsStream("/project/img/MineIcon50px.png"));

    // gi nåde i start
    // VH, VW, CELL_SIZE
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load((getClass().getResource("/project/MSmain.fxml")));
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.getIcons().add(mineImg);
        primaryStage.setScene(new Scene(root, Controller.VW, Controller.VH + 30));
        primaryStage.show();
    }

    // EIGE GAME CLASS?
    public static void main(String[] args) {
        launch(args);
    }
}


// Start timer ved første click/ if not started start() for right/left click event
// smartare mine-velging, sett ex 70 celle fordel slik at akkurat 70, mulig
// ha cell changed i  game og la play returnere liste Cell ... cells, ha change graphics på alle
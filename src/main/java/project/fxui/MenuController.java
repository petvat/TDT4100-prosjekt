package project.fxui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class MenuController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Slider ySlider;

    @FXML
    Slider xSlider;

    @FXML
    Slider mineSlider;

    @FXML
    Button newGameBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        var product = Bindings.createIntegerBinding(() -> (int) (ySlider.getValue() * xSlider.getValue() / 4),
                ySlider.valueProperty(), xSlider.valueProperty());
        mineSlider.maxProperty().bind(product);
    }

    public void createNewGame(ActionEvent event) throws IOException {
        int y = (int) ySlider.getValue();
        System.out.println(y);
        int x = (int) xSlider.getValue();
        System.out.println(x);
        int mines = (int) mineSlider.getValue();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/MainScene.fxml"));
        root = loader.load();
        MSController msController = loader.getController();
        // x, y????
        msController.initNewGame(y, x, mines);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, MSController.CELL_SIZE * x, MSController.CELL_SIZE * y + 30);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

    }

}

package project;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @FXML
    Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
         * List<Slider> sliders = new ArrayList<>(Arrays.asList(xSlider, ySlider,
         * mineSlider));
         * for (Slider slider : sliders) {
         * slider.setShowTickLabels(true);
         * slider.setShowTickMarks(true);
         * }
         */
        var product = Bindings.createIntegerBinding(() -> (int) (ySlider.getValue() * xSlider.getValue() / 4),
                ySlider.valueProperty(), xSlider.valueProperty());
        mineSlider.maxProperty().bind(product);
    }

    public void createNewGame(ActionEvent event) throws IOException {
        int y = (int) ySlider.getValue();
        int x = (int) xSlider.getValue();
        int mines = (int) mineSlider.getValue();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/MSmain.fxml"));
        root = loader.load();
        MSController msController = loader.getController();
        msController.handleNewGame(y, x, mines);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // dynamisk, umulig?
        scene = new Scene(root, MSController.VW, MSController.VH + 30);
        stage.setScene(scene);
        stage.show();
    }

    public void cancel(ActionEvent event) throws IOException {
        System.out.println("CANT CANCEL SORRY");
    }

}

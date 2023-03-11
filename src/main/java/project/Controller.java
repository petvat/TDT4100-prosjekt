package project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class Controller implements Initializable {

    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // create grid
        Board bd = new Board();

        // skal berre lage buttons til cells

        for (int y = 0; y < bd.getColLength(); y++) {
            for (int x = 0; x < bd.getRowLength(); x++) {
                Button btn = new Button();
                // temp hardcode
                btn.setPrefSize(40, 40);
                // customisatioon

                Cell cell = bd.getCellAt(y, x);
                //btn.setUserData(cell);

                //!!!
                btn.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        handleRevealing(cell);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        handleFlagging(cell);
                    }
                });

                grid.add(btn, y, x);

            }
        }
    }

    public void handleFlagging(Cell cell, Button btn) {
        cell.flag();
    }

    public void handleRevealing(Cell cell, Button btn) {
        cell.reveal();
    }

    public void setBtnFlagged(Button btn) {
        btn.setStyle("-fx-background-color: red;");
    }

    public void setBtnRevealed(Button btn, Cell cell) {
        String mineCount = Integer.toString(cell.reveal());

        cell.reveal();
        btn.setText(mineCount);
        btn.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
    }


    public void handleMineCountChange(Board board) {
        // getCount()
    }

}

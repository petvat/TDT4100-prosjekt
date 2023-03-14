package project;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller implements Initializable, CellListener {

    @FXML
    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // create grid
        Board bd = new Board();

        // skal berre lage buttons til cells

        for (int y = 0; y < bd.getColLength(); y++) {
            for (int x = 0; x < bd.getRowLength(); x++) {
                // btn
                Rectangle btn = new Rectangle();
                Text txt = new Text();

                // default utsjånad
                btn.setFill(Color.GRAY);
                btn.setStroke(Color.LIGHTGRAY);
                txt.setFill(Color.DARKGRAY);
                txt.setText("");

                // 2 lag
                StackPane stack = new StackPane(btn, txt);
                // temp hardcode
                //btn.setPrefSize(40, 40);
                // customisation

                Cell cell = bd.getCellAt(y, x);
                // fancy shit
                cell.setUserData(stack);

                stack.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        cell.reveal();
                        // kan ikkje ha update() her for vil ikkje affecte adjacents 
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        cell.flag();
                    }
                });

                /* 
                btn.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        cell.reveal();
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        cell.flag();
                    }
                });
                */
                grid.add(stack, y, x);

            }
        }
    }


    // kan også brukast for mineCount i Board
    @Override
    public void cellChanged(Cell cell) {
        StackPane stack =  (StackPane) cell.getUserData();
        Rectangle rect = (Rectangle) stack.getChildren().get(0);
        Text txt = (Text) stack.getChildren().get(1);

        if (cell.isRevealed()) {
            if (cell.isMine()) {
                rect.setFill(Color.WHITESMOKE);
            } else {
                txt.setText(Integer.toString(cell.adjacentMineCount()));
            }
        }
        else if (cell.isFlagged()) {
            txt.setText("F");
            txt.setFill(Color.WHITE);
            rect.setFill(Color.RED);
        }
        else if (!cell.isFlagged()) {
            // default
            rect.setFill(Color.LIGHTGRAY);
            txt.setFill(Color.DARKGRAY);
            txt.setText("");

        }
    }


    public void updateStack(Cell cell) {
        
    }
 

    public void updateRect(Rectangle rect) {
    }

    /* 
    public void createButton() {

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
    */

    

}

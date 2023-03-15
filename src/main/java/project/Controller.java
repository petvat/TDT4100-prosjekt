package project;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller implements Initializable, CellListener {

    private Map<Cell, StackPane> cellMap = new HashMap<Cell, StackPane>();

    private static final int VH = 800;
    private static final int VW = 800;
    private static final int CELL_SIZE = 40;
    private static final int X_SIZE = VW / CELL_SIZE;
    private static final int Y_SIZE = VH / CELL_SIZE;

    @FXML
    private AnchorPane root;

    @FXML
    private Text timer;

    @FXML
    private Text MineCount;

    @FXML
    private Button save;

    @FXML
    private Button reset;

    @FXML
    private BorderPane border;

    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // FXMLLoader loader = new
        // FXMLLoader(getClass().getResource("/project/GameScene1.fxml"));

        // ???
        // fix scene dimensions

        // create grid
        grid = new GridPane();

        // get Values

        Board bd = new Board(Y_SIZE, X_SIZE);
        bd.init("EASY");

        // skal berre lage buttons til cells

        for (int y = 0; y < bd.getRows() - 1; y++) {
            for (int x = 0; x < bd.getCols() - 1; x++) {
                // btn
                // Må sette -2 fordi stroke tar 1px
                Rectangle btn = new Rectangle(CELL_SIZE - 2, CELL_SIZE - 2);
                Text txt = new Text();

                // default utsjånad
                btn.setFill(Color.GRAY);
                btn.setStroke(Color.LIGHTGRAY);
                txt.setFill(Color.DARKGRAY);
                txt.setText("");

                // 2 lag
                StackPane stack = new StackPane(btn, txt);
                //stack.setMaxSize(40, 40);
                // temp hardcode
                // customisation

                Cell cell = bd.getCellAt(y, x);
                cell.addChangeListener(this);

                // Lag map for å finne igjen stack frå cell
                cellMap.put(cell, stack);

                // EVT BOARD Bd.cellAt(y,x).reveal()
                stack.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        cell.reveal();
                        // kan ikkje ha update() her for vil ikkje affecte adjacents
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        cell.flag();
                    }
                });
                grid.add(stack, y, x);

            }
        }
        // root.getChildren().add(grid);
        border.setCenter(grid);
    }

    // kan også brukast for mineCount i Board
    @Override
    public void cellChanged(Cell cell) {
        StackPane stack = (StackPane) cellMap.get(cell);
        Rectangle rect = (Rectangle) stack.getChildren().get(0);
        Text txt = (Text) stack.getChildren().get(1);

        if (cell.isRevealed()) {
            rect.setFill(Color.WHITESMOKE);
            stack.setDisable(true);

            if (cell.isMine()) {
                txt.setText("X");
            } else {
                txt.setFill(Color.BLACK);
                txt.setText(Integer.toString(cell.getAdjacentMineCount()));
            }
        } else if (cell.isFlagged()) {
            txt.setText("F");
            txt.setFill(Color.WHITE);
            rect.setFill(Color.RED);
        } else if (!cell.isFlagged()) {
            // default
            rect.setFill(Color.LIGHTGRAY);
            txt.setFill(Color.DARKGRAY);
            txt.setText("");

        }
    }

    public void save() {

    }

    public void reset() {

    }

    public void updateStack(Cell cell) {

    }

    public void updateRect(Rectangle rect) {
    }

    /*
     * public void createButton() {
     * 
     * }
     * 
     * public void handleFlagging(Cell cell, Button btn) {
     * cell.flag();
     * }
     * 
     * public void handleRevealing(Cell cell, Button btn) {
     * cell.reveal();
     * }
     * 
     * public void setBtnFlagged(Button btn) {
     * btn.setStyle("-fx-background-color: red;");
     * }
     * 
     * public void setBtnRevealed(Button btn, Cell cell) {
     * String mineCount = Integer.toString(cell.reveal());
     * 
     * cell.reveal();
     * btn.setText(mineCount);
     * btn.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
     * }
     * 
     * 
     * public void handleMineCountChange(Board board) {
     * // getCount()
     * }
     */

}

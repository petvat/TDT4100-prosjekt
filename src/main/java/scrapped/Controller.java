package scrapped;

import java.net.URL;
//import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import project.CellListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// CONTROLLER FOR NYTT GAME

public class Controller implements Initializable, CellListener {
    public static final int VH = 800;
    public static final int VW = 800;
    public static final int CELL_SIZE = 40;
    public static final int X_SIZE = VW / CELL_SIZE;
    public static final int Y_SIZE = VH / CELL_SIZE;

    private int time = 0;
    private Map<CellOLD, StackPane> cellMap = new HashMap<CellOLD, StackPane>();
    private BoardOLD bd;
    // private timer

    // treng berre board, -> gå inn i board -> ha en "sjekker" som går igjennom alle
    // Cell og set opp grafikk etter det

    @FXML
    private AnchorPane root;

    @FXML
    private Text timer;

    @FXML
    private Text mineCount;

    @FXML
    private Button save;

    @FXML
    private Button reset;

    @FXML
    private BorderPane border;

    private GridPane grid;

    // NEW GAME
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lag timer
        // Lag eigen plass
        timer.setText("Time: " + Integer.toString(time));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            time++;
            timer.setText("Time: " + Integer.toString(time));
        }));
        timeline.setCycleCount(1000);
        timeline.play();

        // Sett opp mine-teller

        // Lag grafisk grid
        grid = new GridPane();

        // Klargjer board
        bd = new BoardOLD(Y_SIZE, X_SIZE);
        bd.init("NORMAL");

        // Lagar grafisk mine counter

        // Lag Cell-grafikk
        for (int y = 0; y < bd.getRows(); y++) {
            for (int x = 0; x < bd.getCols(); x++) {

                // DEL INN I NY CLASS?
                // Må sette -2 fordi stroke tar 1px
                Rectangle btn = new Rectangle(CELL_SIZE - 2, CELL_SIZE - 2);
                Text txt = new Text();

                // Default utsjånad
                btn.setFill(Color.GRAY);
                btn.setStroke(Color.LIGHTGRAY);
                txt.setFill(Color.DARKGRAY);
                txt.setText("");

                // Grafisk representasjon av Cell består av StackPane av rect og txt
                StackPane stack = new StackPane(btn, txt);

                CellOLD cell = bd.getCellAt(y, x);
                cell.addChangeListener(this);
                // Litt scuffed
                cell.addChangeListener(bd);

                // Lag map for å finne igjen stack frå cell
                cellMap.put(cell, stack);

                // Bind Cell til brukar-interaksjon
                stack.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        cell.reveal();
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        cell.flag();
                    }
                });
                stack.setOnMouseEntered(e -> {
                    btn.setTranslateX(-0.5);
                    btn.setTranslateY(-0.5);
                });
                stack.setOnMouseExited(e -> {
                    btn.setTranslateX(0);
                    btn.setTranslateY(0);
                });
                grid.add(stack, y, x);
            }
        }
        border.setCenter(grid);

        // Sett opp mine-teller
        // BØR HA MINECOUNTER
        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));
    }

    public void handleSave() {
        
    }

    public void handleLoad() {

    }

    @Override
    public void cellChanged(CellOLD cell) {
        updateMineCount();
        // start timer ?
        StackPane stack = (StackPane) cellMap.get(cell);
        Rectangle rect = (Rectangle) stack.getChildren().get(0);
        Text txt = (Text) stack.getChildren().get(1);

        if (cell.isRevealed()) {
            rect.setFill(Color.WHITESMOKE);
            stack.setDisable(true);
            if (cell.isMine()) {
                txt.setText("X");
                lost();
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
            rect.setFill(Color.GRAY);
            txt.setText("");
        }

        // PLACEHOLDER
        // GÅR IKKJE
    }

    public void updateMineCount() {
        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));

    }

    public void lost() {
        System.out.println("LOST");
        // SHOW ALL
    }

    public void startTimer() {
    }

    public void save() {
        System.out.println("(NOT)SAVED");

        // TA BOARD OG LES FRÅ DET
        // TRENG MAIN MENU AREA
        // Gjer som om dette kunne implementerast likt i konsoll

        // Arrays.asList(bd.getGrid())
    }

    public void reset() {
        // Initialiser på nyy
    }

    public void updateStack(CellOLD cell) {

    }

    public void updateRect(Rectangle rect) {
    }
}

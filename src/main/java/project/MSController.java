package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
//import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// CONTROLLER FOR NYTT GAME

public class MSController implements Initializable, CellListener {
    public static final int VH = 800;
    public static final int VW = 800;
    public static final int CELL_SIZE = 40;
    public static final int X_SIZE = VW / CELL_SIZE;
    public static final int Y_SIZE = VH / CELL_SIZE;

    private Map<Cell, StackPane> cellMap = new HashMap<Cell, StackPane>();
    private Game game;
    private IGameSaveHandler saver;
    // private timer

    // treng berre board, -> gå inn i board -> ha en "sjekker" som går igjennom alle
    // Cell og set opp grafikk etter det

    @FXML
    private AnchorPane root;

    @FXML
    private TextField filename;

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

    @FXML
    private ComboBox<String> comboBox;

    private GridPane grid;

    // NEW GAME
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        grid = new GridPane();
        comboBox.setItems(FXCollections.observableArrayList(setSlots()));
        comboBox.setOnAction(this::handleLoad);

        saver = new GameSaveHandler();
        game = new Game(new Board(X_SIZE, CELL_SIZE), 0, "NORMAL");
        Board bd = game.getBoard();
        drawBoard(bd);

        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));

        border.setCenter(grid);
        startTimer(game);
    }

    private void startTimer(Game game) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            game.timeElapsed();
            timer.setText(Integer.toString(game.getTimeElapsed()));
        }));
        timeline.setCycleCount(1000);
        timeline.play();
    }


    private List<String> setSlots() {
        File folder = new File("project/resources/saves");
        List<String> slots = new ArrayList<>();
        File[] lst = folder.listFiles();
        int i = 0;
        for (File file : lst) {
            if (file.isFile()) {
                slots.add(file.getName());
                i++;
            }
        }
        while (i < 4) {
            slots.add("--Empty slot" + Integer.toString(i) + "--");
            i++;
        }
        return slots;
    }

    private void drawBoard(Board bd) {
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

                Cell cell = bd.getCellAt(y, x);
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
    }

    public void handleReset() {
        Game game = new Game(new Board(X_SIZE, CELL_SIZE), 0, "NORMAL");
        Board bd = game.getBoard();
        drawBoard(bd);
        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));
        startTimer(game);
    }

    public void handleSave(ActionEvent e) {
        try {
            saver.save(getUserInputFilename(), game);
            setSlots();

        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
            //
        }
    }

    public void handleLoad() {
        try {
            saver.load(getUserInputFilename());
        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
        }
    }

    public String getUserInputFilename() {
        String filename = this.filename.getText();
        if (filename.isEmpty()) {
            return "auto_save";
        }
        return filename;
    }

    @Override
    public void cellChanged(Cell cell) {
        // temporary
        updateMineCount(cell.getBoard());
        // start timer ?
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
            rect.setFill(Color.GRAY);
            txt.setText("");
        }

        // PLACEHOLDER
        // GÅR IKKJE
    }

    public void updateMineCount(Board bd) {
        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));

    }
}

package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
//import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
    private String gameFile;
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
    private Button saveBtn;

    @FXML
    private Button reset;

    @FXML
    private BorderPane border;

    @FXML
    private ComboBox loadBox;

    private GridPane grid;

    @FXML

    // NEW GAME
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        grid = new GridPane();

        // ?????????
        saver = new GameSaveHandler();
        game = new Game(new Board(X_SIZE, CELL_SIZE), 0, "NORMAL");
        Board bd = game.getBoard();
        bd.init(game.getDifficulty());
        drawBoard(bd);

        mineCount.setText("Mines: " + Integer.toString(bd.getMinesLeft()));

        border.setCenter(grid);
        startTimer(game);

        // PRØVE
        gameFile = null;

        // saveBox.setItems(FXCollections.observableArrayList(setSlots()));
        // saveBox.setOnAction(this::handleSave);
        ObservableList<String> list = FXCollections.observableArrayList(setSlots());
        loadBox.setItems(list);
        // loadBox.setOnAction(this::handleLoad);
        // initSaveBox();
    }

    /*
     * private void initSaveBox() {
     * // FORSØK PÅ Å LEGGE TIL SLOT-VELGER + PROMPT FOR Å LEGGE TIL NY FIL
     * saveBox.getSelectionModel().selectedItemProperty().addListener((observable,
     * oldValue, newValue) -> {
     * int index = saveBox.getSelectionModel().getSelectedIndex();
     * saveBox.getItems().remove(index);
     * 
     * TextInputDialog dialog = new TextInputDialog();
     * dialog.setTitle("overwrite slot");
     * dialog.setHeaderText("Enter name of new file:");
     * Optional<String> result = dialog.showAndWait();
     * 
     * if (result.isPresent()) {
     * String newItem = result.get();
     * saveBox.getItems().add(index, newItem);
     * handleSave(newItem);
     * }
     * });
     * }
     */

    private void initLoadBox() {
        // BRUKE I SAVEBOX ?? VISS IKKJE BERRE ADD
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
        int i = 0;
        List<String> slots = new ArrayList<>();
        if (folder.listFiles() != null) {
            File[] lst = folder.listFiles();
            for (File file : lst) {
                if (file.isFile()) {
                    String[] parts = file.getName().split(".");
                    slots.add(parts[0]);
                    i++;
                }
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
                        // bd.reveal(cell);
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

    // SAVE AS METODE? + OVERWRITE?
    @FXML
    public void handleSave() {
        // getUserInputFilename()
        if (gameFile == null) {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Save game");
            tid.setHeaderText("Enter name of new game file:");
            Optional<String> result = tid.showAndWait();

            if (result.isPresent()) {
                gameFile = result.get();
            } else {
                gameFile = "unnamed save";
            }
        }

        try {
            saver.save(gameFile, game);
            // setSlots();

        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
            //
        }

        ObservableList<String> list = FXCollections.observableArrayList(setSlots());
        loadBox.setItems(list);

    }

    @FXML
    public void handleLoad() {
        String file = loadBox.getSelectionModel().getSelectedItem().toString();
        try {
            // handle if game object is null
            if (saver.load(file) != null) {
                game = saver.load(file);
            }
            game = saver.load(file);
            gameFile = file;
            Board bd = game.getBoard();

            // HANDLE REINITIALISATION OF CELLS EASY WAY
            // ser no at scuffed med controller implements listener, fordi dette må vere her
            for (int y = 0; y < bd.getCols(); y++) {
                for (int x = 0; x < bd.getRows(); x++) {
                    bd.getCellAt(y, x).update();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
        }
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

package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MSController implements Initializable, CellListener {
    public static final int VH = 800;
    public static final int VW = 800;
    public static final int CELL_SIZE = 40;
    public static final int X_SIZE = VW / CELL_SIZE;
    public static final int Y_SIZE = VH / CELL_SIZE;
    private Image mineImg = new Image(getClass().getResourceAsStream("/project/img/MineIcon50px.png"));
    private Image cellImg = new Image(getClass().getResourceAsStream("/project/img/mineTile25px.png"));
    private Image FlaggedImg = new Image(getClass().getResourceAsStream("/project/img/Flagged.png"));
    private Font pixelFont = Font.loadFont(
            MSController.class.getResource("/project/font/upheavtt.ttf").toExternalForm(),
            26);

    private Map<Cell, StackPane> cellMap;
    private Game game;
    private GameSaveHandler saver;
    private Timeline timeline;

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
    private ComboBox<String> loadBox;

    private GridPane grid;

    @FXML

    // NEW GAME
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new GridPane();
        saver = new GameSaveHandler();
        game = new Game(new Board(X_SIZE, Y_SIZE), 0, "NORMAL");
        Board bd = game.getBoard();
        // litt snodig
        bd.init(game.getDifficulty());
        drawBoard(bd);
        updateMineCount(bd);
        border.setCenter(grid);
        startTimer(game);
        ObservableList<String> list = FXCollections.observableArrayList(setSlots());
        loadBox.setItems(list);
    }

    private void startTimer(Game game) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            game.timeElapsed();
            timer.setText("TIME: " + Integer.toString(game.getTimeElapsed()));
        }));
        timeline.setCycleCount(1000);
        timeline.play();
    }

    private List<String> setSlots() {
        // BUG KAN TRYKKE PÅ WOW, SUCH EMPTY
        File folder = new File("src/main/resources/project/saves/");
        List<String> slots = new ArrayList<>();
        System.out.println(folder.listFiles());
        if (folder.listFiles() != null) {
            File[] lst = folder.listFiles();
            for (File file : lst) {
                if (file.isFile()) {
                    String filename = file.getName();
                    String name = filename.substring(0, filename.length() - 5);
                    System.out.println(name);
                    slots.add(name);
                }
            }
        }
        if (slots.isEmpty()) {
            slots.add("Wow, such empty ...");
            // enklaste fjern denne ELLER syte for removeSave()
            // legge til ved scenebuilder og fjern etter første save
        }
        return slots;
    }

    private void setUpGame() {
        Board bd = game.getBoard();
        drawBoard(bd);
        updateMineCount(bd);
        startTimer(game);
    }

    public void updateLoadMenu() {

    }

    private void drawBoard(Board bd) {
        grid.getChildren().clear();
        cellMap = new HashMap<Cell, StackPane>();
        // Lag Cell-grafikk
        for (int y = 0; y < bd.getRows(); y++) {
            for (int x = 0; x < bd.getCols(); x++) {
                Rectangle btn = new Rectangle(CELL_SIZE, CELL_SIZE);
                Text txt = new Text();

                btn.setFill(new ImagePattern(cellImg));
                txt.setFont(pixelFont);
                txt.setText("");

                // Grafisk representasjon av Cell består av StackPane av rect og txt
                StackPane stack = new StackPane(btn, txt);

                Cell cell = bd.getCellAt(y, x);
                cell.addChangeListener(this);

                // Lag map for å finne igjen stack frå cell
                cellMap.put(cell, stack);

                // Bind Cell til brukar-interaksjon
                stack.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        // game.playInteractionReveal(Cell cell) (returns List<Cell>) static
                        // return all affected cells after interaction
                        // for all affected cells, updateGraphics(Cell cell)
                        // må ha bd.reveal() returns List<Cell>?
                        // HADDE VORE BEST viss board ikkje blei referert til her i det heile,
                        // controller-game-board-cell
                        // KUNNE gått game.CellChanged(Cell cell) returns Cell ... cells 
                        bd.reveal(cell);
                        // workaround cell.isMine() -> drawLost()
                        if (game.isWon()) {
                            won();
                        } else if (game.isLost()) {
                            // UUUH
                        }
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        bd.flag(cell);
                        updateMineCount(bd);
                    }
                });
                stack.setOnMouseEntered(e -> {
                    btn.setTranslateX(0.5);
                    btn.setTranslateY(0.5);
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
        game = new Game(new Board(Y_SIZE, X_SIZE), 0, "NORMAL");
        game.setName(null);
        Board bd = game.getBoard();
        bd.init(game.getDifficulty());
        drawBoard(bd);
        System.out.println(Integer.toString(bd.getMinesLeft()));
        // DRITA
        timeline.stop(); // 
        //timer.setText("TIME  ");
        updateMineCount(bd);
        startTimer(game);
        System.out.println(game.getName());
        //rotete
    }

    @FXML
    public void handleSave() {
        if (game.getName() == null) {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Save game");
            tid.setHeaderText("Enter name of new game file:");
            Optional<String> result = tid.showAndWait();

            if (result.isPresent()) {
                game.setName(result.get());
            }
        }
        try {
            saver.save(game);
        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
        }
        System.out.println(game.getName());
        ObservableList<String> list = FXCollections.observableArrayList(setSlots());
        loadBox.setItems(list);
    }

    @FXML
    public void handleLoad() {
        // ENTEN LOAD(FILE, GAME) FOR Å SLEPPE Å LAGE NYTT GAME
        // PROS: ENKELT Å FIKSE, CONS: GAME BOARD-STORLEIK MÅ VERE FIXED
        // ELLER DRAWBOARD() HER
        Object selectedSavedFile = loadBox.getSelectionModel().getSelectedItem();
        if (selectedSavedFile == null)
            return;
        String file = selectedSavedFile.toString();
        try {
            if (saver.load(file) == null || file == game.getName()) {
                return;
            }
            game = saver.load(file);
            game.setName(file);
            Board bd = game.getBoard();
            drawBoard(bd);
            // DRITA
            timeline.stop();
            updateMineCount(bd);
            startTimer(game);
            System.out.println(game.getName());
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

    public void won() {
        System.out.println("WON!! YE");
    }

    public void drawLost() {
        // kryss viss flag og !isMine
        // opne alle mines
    }

    @Override
    public void cellChanged(Cell cell) {
        StackPane stack = (StackPane) cellMap.get(cell);
        Rectangle rect = (Rectangle) stack.getChildren().get(0);
        Text txt = (Text) stack.getChildren().get(1);
        if (cell.isRevealed()) {
            rect.setFill(Color.WHITESMOKE);
            stack.setDisable(true);
            if (cell.isMine()) {
                rect.setFill(new ImagePattern(mineImg));

            } else {
                int mines = cell.getAdjacentMineCount();
                txt.setText(mines == 0 ? "" : Integer.toString(mines));
                switch (mines) {
                    case 1:
                        txt.setFill(Color.ROYALBLUE);
                        break;
                    case 2:
                        txt.setFill(Color.FORESTGREEN);
                        break;
                    case 3:
                        txt.setFill(Color.HOTPINK);
                        break;
                    case 4:
                        txt.setFill(Color.DARKORCHID);
                        break;
                    case 5:
                        txt.setFill(Color.MEDIUMAQUAMARINE);
                        break;
                    default:
                        txt.setFill(Color.BLACK);
                        break;
                }
            }
        } else if (cell.isFlagged()) {
            rect.setFill(new ImagePattern(FlaggedImg));
        } else if (!cell.isFlagged()) {
            // default
            rect.setFill(new ImagePattern(cellImg));
            txt.setText("");
        }
    }

    public void updateMineCount(Board bd) {
        mineCount.setText("MINES: " + Integer.toString(bd.getMinesLeft()));
    }

    public void updateTimer() {

    }
}

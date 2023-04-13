package project.fxui;

import project.model.Board;
import project.model.Cell;
import project.model.CellListener;
import project.model.Game;
import project.model.GameSaveHandler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.stage.Stage;

public class MSController implements Initializable, CellListener {
    public static final int VH = 800;
    public static final int VW = 800;
    public static final int CELL_SIZE = 40;
    public static int X_SIZE = VW / CELL_SIZE;
    public static int Y_SIZE = VH / CELL_SIZE;
    public int TOP_BAR_HEIGHT = 30;
    public int MINE_COUNT = 75;
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
    private Stage stage;
    private Scene scene;
    private Parent parent;

    @FXML
    private Label gameOverLabel;
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
    private Button newGameBtn;
    @FXML
    private BorderPane border;
    @FXML
    private ComboBox<String> loadBox;

    private GridPane grid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grid = new GridPane();
        border.setCenter(grid);

        saver = new GameSaveHandler();
        game = new Game(Y_SIZE, X_SIZE, 0, MINE_COUNT);
        Board board = game.getBoard();
        board.init();
        drawBoard(board);
        updateMineCount();
        startTimer(game);

        gameOverLabel.setVisible(false);

        ObservableList<String> list = FXCollections.observableArrayList(saver.setSlots());
        if (list != null) {
            loadBox.setItems(list);
        }
    }

    private void startTimer(Game game) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            game.timeElapsed();
            timer.setText("TIME: " + Integer.toString(game.getTimeElapsed()));
        }));
        timeline.setCycleCount(1000);
        timeline.play();
    }

    public void initNewGame(int ySize, int xSize, int mines) {
        Y_SIZE = ySize;
        X_SIZE = xSize;
        this.MINE_COUNT = mines;
        timeline.stop();
        initialize(null, null);
        game.setName(null);
    }

    private void drawBoard(Board board) {
        grid.getChildren().clear();
        cellMap = new HashMap<Cell, StackPane>();
        // Lag Cell-grafikk
        board.forEachCell(cell -> {
            if (cell.getListeners() != null) {
                if (!cell.getListeners().contains(this)) {
                    cell.addChangeListener(this);
                }
            }
            grid.add(createCellRepresentation(cell), cell.getX(), cell.getY());
        });
    }

    private StackPane createCellRepresentation(Cell cell) {
        Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE);
        Text txt = new Text();
        rect.setFill(new ImagePattern(cellImg));
        txt.setFont(pixelFont);
        txt.setText("");
        // Grafisk representasjon av Cell består av StackPane av rect og txt
        StackPane stack = new StackPane(rect, txt);
        // Lag map for å finne igjen stack frå cell
        cellMap.put(cell, stack);
        // Bind Cell til brukar-interaksjon
        stack.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                game.reveal(cell);
                if (game.isWon()) {
                    drawWon();
                } else if (game.isLost()) {
                    drawLost();
                }
            } else if (e.getButton() == MouseButton.SECONDARY) {
                game.flag(cell);
                updateMineCount();
            }
        });
        stack.setOnMouseEntered(e -> {
            rect.setTranslateX(0.5);
            rect.setTranslateY(0.5);
        });
        stack.setOnMouseExited(e -> {
            rect.setTranslateX(0);
            rect.setTranslateY(0);
        });
        return stack;
    }

    @FXML
    public void handleNewGame(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/MenuScene.fxml"));
        parent = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    @FXML
    public void handleSave() {
        String filename = game.getName();
        if (filename == null) {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("Save game");
            tid.setHeaderText("Enter name of new game file:");
            Optional<String> result = tid.showAndWait();

            if (result.isPresent()) {
                filename = result.get();
            } else {
                return;
            }
        }
        try {
            saver.save(game, filename);
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error during saving ..." + e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Failed");
            alert.setHeaderText("Could not save file");
            alert.setContentText("There was an error saving the file. Please check the file name and try again.");
            alert.showAndWait();
        }
        System.out.println(game.getName() + "THISTHISHTO");
        ObservableList<String> list = FXCollections.observableArrayList(saver.setSlots());
        loadBox.setItems(list);
    }

    @FXML
    public void handleLoad() {
        gameOverLabel.setText("");
        gameOverLabel.setVisible(false);

        Object selectedSavedFile = loadBox.getSelectionModel().getSelectedItem();
        if (selectedSavedFile == null)
            return;
        String file = selectedSavedFile.toString();
        // liten bug når trykker på current file i load-menu, ikkje reset, må loade anna fil, så tilbake
        try {
            if (saver.load(file) == null || file == game.getName()) {
                return;
            }
            game = saver.load(file);
            game.setName(file);
            Board bd = game.getBoard();
            drawBoard(bd);
            timeline.stop();
            updateMineCount();
            bd.forEachCell(cell -> cell.update());
            Y_SIZE = bd.getRows();
            X_SIZE = bd.getCols();
            if (game.isLost()) {
                drawLost();
                timer.setText("TIME: " + Integer.toString(game.getTimeElapsed()));
            } else if (game.isWon()) {
                drawWon();
                timer.setText("TIME: " + Integer.toString(game.getTimeElapsed()));
            } else {
                startTimer(game);
            }
            stage = (Stage) root.getScene().getWindow();
            stage.setWidth(CELL_SIZE * X_SIZE + 10);
            stage.setHeight(CELL_SIZE * Y_SIZE + 70);
        } catch (FileNotFoundException e) {
            System.out.println("File not found." + e);
        }
    }

    public void drawWon() {
        timeline.stop();
        cellMap.forEach((key, value) -> value.setDisable(true));

        gameOverLabel.setText("YOU WON!");
        gameOverLabel.setTranslateY(20);

        gameOverLabel.setVisible(true);
        timeline.stop();
    }

    public void drawLost() {
        timeline.stop();
        cellMap.forEach((key, value) -> {
            value.setDisable(true);
            if (key.isMine()) {
                key.setRevealed(true);
                key.update();
            }
        });
        gameOverLabel.setTranslateY(20);
        gameOverLabel.setText("GAME OVER ...");
        gameOverLabel.setVisible(true);
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

    public void updateMineCount() {
        mineCount.setText("MINES: " + Integer.toString(game.getBoard().getMinesLeft()));
    }
}

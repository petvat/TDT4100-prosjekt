package project;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

public class Cell extends Button {
    private int x, y;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private Board board;
    private int mineCounter;
    private List<CellListener> listeners = new ArrayList<>();
    // ein grid av hosliggande celler, der (0, 0) er cella som vert trykka på
    private int[] adjacents = new int[] {
            -1, -1,
            -1, 0,
            -1, 1,
            0, -1,
            0, 1,
            1, -1,
            1, 0,
            1, 1
    };

    public Cell(int y, int x, boolean isMine) {
        this.y = y;
        this.x = x;
        this.isMine = isMine;
        isRevealed = false;
        isFlagged = false;
        // this.cellSize = cellSize;
        findAdjacents().stream()
                .filter(Cell::isMine)
                .forEach(adjacent -> mineCounter++);

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public boolean isMine() {
        return isMine;
    }


    private boolean isValidCoordinate(int pos, int max) {
        return (pos >= 0 && pos < max);
    }

    // KAN vere i BOARD class
    public List<Cell> findAdjacents() {

        // samling av hosliggande celler definert i board
        List<Cell> trueAdjacents = new ArrayList<>();

        for (int i = 0; i < adjacents.length; i += 2) {
            int dx = adjacents[i];
            int dy = adjacents[i + 1];

            int adjacentX = this.x + dx;
            int adjacentY = this.y + dy;

            // BØR VERE I BOARD/AERA fordi verdi
            if (isValidCoordinate(adjacentX, board.getCellSize()) && isValidCoordinate(adjacentY, board.getCellSize()))
                trueAdjacents.add(board.getCellAt(adjacentY, adjacentX));

            // her kan en lambda funksjon gjere seg
        }
        return trueAdjacents;
    }

    public int adjacentMineCount() {
        // clunky
        findAdjacents().stream()
                .filter(Cell::isMine)
                .forEach(adjacent -> mineCounter++);
        //
        return mineCounter;
    }

    public int getAdjacentMines() {
        return mineCounter;
    }

    public void reveal() {
        isRevealed = true;
        // if (isMine)
        this.adjacentMineCount();
        // if (adjacent.findAdjacents().stream().allMatch(cell -> cell.isMine == false))
        // dersom ingen miner, sjekk hosliggande
        if (mineCounter == 0) {
            for (Cell adjacent : findAdjacents()) {
                adjacent.reveal();
            }
        }
        update();
        // LAMBDA

        // update check isRevealed
    }

    public void flag() {
        if (!isFlagged) {
            isFlagged = true;
            board.getCounter().remove();
        } else if (isFlagged) {
            isFlagged = false;
            board.getCounter().addTo();
        }
        update();
    }
    // kanskje updateGUI(Cell cell) i controller

    public void update() {
        for (CellListener listener : listeners) {
            listener.cellChanged(this);
        }
    }

    // Kor skal denne vere, det store spørsmålet
    public void update1() {
        // Ok så reveal() og flag() må vere så enkle som mogleg
        // Kan bruke cell extends Button
        // ha her:
        // if change in cell (isRevealed etc)
        // change visual frå button stuff
        if (isRevealed() && isMine()) {
            setText("X");
            // change color
        } else if (isRevealed()) {
            if (mineCounter > 0) {
                setText(Integer.toString(mineCounter));
                // change color
            }
        } else if (isFlagged()) {
            setText("F");
        } else if (!isFlagged()) {
            setText("");
        }
    }

    // public void cellChanged ikkje nødvending, berre ein listener
}

// @FXML doSomething()
// Controller
// update
// er clicked - setDisable, endre farge
// endre farge

// rekursiv checkAdjacent

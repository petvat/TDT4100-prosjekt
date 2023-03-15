package project;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

public class Cell {
    private int x, y;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private Board board;
    private int adjacentMineCount;
    private List<CellListener> listeners;
    private List<Cell> adjacents;

    public Cell(int y, int x, boolean isMine) {
        this.y = y;
        this.x = x;
        this.isMine = isMine;
        isRevealed = false;
        isFlagged = false;
        adjacents = new ArrayList<>();
        listeners = new ArrayList<>();

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void computeAdjacents() {
        // ein grid av hosliggande celler, der (0, 0) er cella som vert trykka på
        int[] adjacentPoints = new int[] {
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1
        };
        for (int i = 0; i < adjacentPoints.length - 1; i += 2) {
            int dy = adjacentPoints[i];
            int dx = adjacentPoints[i + 1];

            int adjacentY = this.y + dy;
            int adjacentX = this.x + dx;

            // trur problem er at siste row ikkje vert laga

            // PROBLEM OPPSTÅR NÅR SISTE ROW

            if (board.isValidCoordinate(adjacentY, adjacentX)) {
                Cell cell = board.getCellAt(adjacentY, adjacentX);
                if (cell.isMine())
                    adjacentMineCount++;
                if (!cell.isRevealed()) {
                    this.adjacents.add(board.getCellAt(adjacentY, adjacentX));
                }
            } else {
                continue;
            }
        }
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

    /*
     * private boolean isValidCoordinate(int pos, int max) {
     * return (pos >= 0 && pos < max);
     * }
     */

    /*
     * public int adjacentMineCount() {
     * this.mineCounter = 0;
     * // clunky
     * // før findAdjacents()
     * adjacents.stream()
     * .filter(Cell::isMine)
     * .forEach(adjacent -> this.mineCounter++);
     * //
     * return this.mineCounter;
     * }
     */

    public int getAdjacentMineCount() {
        return adjacentMineCount;
    }

    public void reveal() {
        if (isFlagged)
            return;
        computeAdjacents();
        isRevealed = true;
        update();
        if (isMine) {
            update();
            return;
        }

        if (this.adjacentMineCount == 0) {
            for (Cell adjacent : this.adjacents) {
                if (!adjacent.isRevealed)
                    adjacent.reveal();
            }
        }
        // LAMBDA

        // update check isRevealed
    }

    public void flag() {
        if (!isFlagged) {
            isFlagged = true;
        } else if (isFlagged) {
            isFlagged = false;
        }
        update();
    }
    // kanskje updateGUI(Cell cell) i controller

    public void update() {
        for (CellListener listener : listeners) {
            listener.cellChanged(this);
        }
        // hugs add counter (mine)
    }

    public void addChangeListener(CellListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(CellListener listener) {
        listeners.remove(listener);
    }

    // Kor skal denne vere, det store spørsmålet
    // SCRAPPED
    /*
     * public void update1() {
     * // Ok så reveal() og flag() må vere så enkle som mogleg
     * // Kan bruke cell extends Button
     * // ha her:
     * // if change in cell (isRevealed etc)
     * // change visual frå button stuff
     * if (isRevealed() && isMine()) {
     * setText("X");
     * // change color
     * } else if (isRevealed()) {
     * if (mineCounter > 0) {
     * setText(Integer.toString(mineCounter));
     * // change color
     * }
     * } else if (isFlagged()) {
     * setText("F");
     * } else if (!isFlagged()) {
     * setText("");
     * }
     * }
     */
    // public void cellChanged ikkje nødvending, berre ein listener
}

// @FXML doSomething()
// Controller
// update
// er clicked - setDisable, endre farge
// endre farge

// rekursiv checkAdjacent

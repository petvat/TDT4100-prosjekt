package project;

import java.util.ArrayList;
import java.util.List;

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

    public Board getBoard() {
        return board;
    }

    public void computeAdjacents() {
        // Ein grid av hosliggande celler, der (0, 0) er cella som vert trykka på
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

            // Sjekk om Cell gitt ved adjacentY/X finst, viss ja, legg til i liste av
            // hosliggande celler
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
        // Viss alle hosliggande celler ikkje er miner, opne dei ved rekursiv reveal()
        if (this.adjacentMineCount == 0) {
            for (Cell adjacent : this.adjacents) {
                if (!adjacent.isRevealed)
                    adjacent.reveal();
            }
        }
    }

    public void flag() {
        if (!isFlagged) {
            isFlagged = true;
        } else if (isFlagged) {
            isFlagged = false;
        }
        update();
    }

    public void update() {
        // Varsle til lyttarar, i dette programmet: controllaren
        for (CellListener listener : listeners) {
            listener.cellChanged(this);
        }
    }

    public void addChangeListener(CellListener listener) {
        listeners.add(listener);
    }

    public void removeChangeListener(CellListener listener) {
        listeners.remove(listener);
    }
}
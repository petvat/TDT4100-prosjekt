package project;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x, y;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private List<CellListener> listeners;

    private Cell(int y, int x, boolean isMine) {
        this.y = y;
        this.x = x;
        this.isMine = isMine;
        isRevealed = false;
        isFlagged = false;
        listeners = new ArrayList<>();
    }



    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public boolean isMine() {
        return isMine;
    }

    public void reveal() {

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
        for (CellListener listener : listeners) {
            listener.cellChanged(this);
        }
        // hugs add counter (mine)
    }
}




    /*
     * // KAN vere i BOARD class
     * public List<Cell> findAdjacents() {
     * // pass på at berre kallast ein gong
     * 
     * // samling av hosliggande celler definert i board
     * List<Cell> trueAdjacents = new ArrayList<>();
     * 
     * for (int i = 0; i < adjacents.length; i += 2) {
     * int dx = adjacents[i];
     * int dy = adjacents[i + 1];
     * 
     * int adjacentX = this.x + dx;
     * int adjacentY = this.y + dy;
     * 
     * // BØR VERE I BOARD/AERA fordi verdi
     * if (isValidCoordinate(adjacentX, board.getCellSize()) &&
     * isValidCoordinate(adjacentY, board.getCellSize()))
     * trueAdjacents.add(board.getCellAt(adjacentY, adjacentX));
     * 
     * // her kan en lambda funksjon gjere seg
     * }
     * this.trueAdjacents = trueAdjacents;
     * // messy koe
     * return trueAdjacents;
     * }
     */
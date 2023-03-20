package project;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private int x, y;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private List<CellListener> listeners;

    public Cell(int y, int x, boolean isMine) {
        this.y = y;
        this.x = x;
        this.isMine = isMine;
        isRevealed = false;
        isFlagged = false;
        listeners = new ArrayList<>();

    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
        update();
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
        update();
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
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

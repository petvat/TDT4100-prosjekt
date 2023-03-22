package scrapped;

import java.util.Arrays;

import project.CellListener;
import project.MineCounter;

public class BoardOLD implements CellListener {

    private CellOLD[][] grid;
    private double mineDensity = 0.2;
    private int minesTotal;
    private int minesLeft;
    private MineCounter counter;

    public BoardOLD(int ySize, int xSize) {
        grid = new CellOLD[ySize][xSize];
    }

    // Init board med vanskegrad ie minetettleik
    public void init(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY":
                mineDensity = 0.1;
            case "NORMAL":
                mineDensity = 0.12625;
            case "HARD":
                mineDensity = 0.2;
        }
        // Sett opp grid og legg til mine
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                CellOLD cell = new CellOLD(y, x, Math.random() <= mineDensity);
                grid[y][x] = cell;
                cell.setBoard(this);
                // Samle miner til minesTotal
                if (cell.isMine()) {
                    minesTotal++;
                    minesLeft++;
                }
            }
        }
    }

    public void setMinesTotal(int minesTotal) {
        this.minesTotal = minesTotal;
    }

    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public int getMinesLeft() {
        return minesLeft;
    }

    public int getMinesTotal() {
        return minesTotal;
    }

    public boolean isValidCoordinate(int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length);
    }

    // SAVE ALL ELEMENTS

    public CellOLD[][] getGrid() {
        return grid;
    }

    public CellOLD getCellAt(int y, int x) {
        return grid[y][x];
    }

    public void setCellAt(CellOLD cell, int y, int x) {
        grid[y][x] = cell;
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }

    // MINECOUNTING
    public MineCounter getCounter() {
        return counter;
    }

    @Override
    public void cellChanged(CellOLD cell) {
        // Oppdater minesLeft dersom Cell er blitt flagga
        if (cell.isFlagged() && minesLeft > 0) {
            minesLeft--;
        } else if (!cell.isFlagged() && !cell.isRevealed() && minesLeft < minesTotal) {
            minesLeft++;
        }
    }

}

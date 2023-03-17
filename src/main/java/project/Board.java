package project;

import java.util.Arrays;

public class Board implements CellListener {

    private Cell[][] grid;
    private double mineDensity = 0.2;
    private int minesTotal;
    private int minesLeft;
    private MineCounter counter;

    public Board(int ySize, int xSize) {
        grid = new Cell[ySize][xSize];
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
                Cell cell = new Cell(y, x, Math.random() <= mineDensity);
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

    public int getMinesLeft() {
        return minesLeft;
    }

    public boolean isValidCoordinate(int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length);
    }

    // SAVE ALL ELEMENTS

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCellAt(int y, int x) {
        return grid[y][x];
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
    public void cellChanged(Cell cell) {
        // Oppdater minesLeft dersom Cell er blitt flagga
        if (cell.isFlagged() && minesLeft > 0) {
            minesLeft--;
        }
        else if (!cell.isFlagged() && !cell.isRevealed() && minesLeft < minesTotal) {
            minesLeft++;
        }
    }

}

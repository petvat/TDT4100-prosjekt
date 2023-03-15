package project;

import java.util.Arrays;

public class Board implements CellListener {

    private Cell[][] grid;
    // density (!)
    private double mineDensity = 0.2;
    private int minesTotal;
    private int minesLeft;
    private int cellSize;
    private MineCounter counter;

    // UTILS

    public Board(int ySize, int xSize) {
        grid = new Cell[ySize][xSize];
    }

    // init board with difficulty
    public void init(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY":
                mineDensity = 0.1;
            case "NORMAL":
                mineDensity = 0.1;
            case "HARD":
                mineDensity = 0.1;
        }
        // sett opp grid og legg til mine
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                Cell cell = new Cell(y, x, Math.random() <= mineDensity);
                grid[y][x] = cell;
                cell.setBoard(this);
                if (cell.isMine())
                    minesTotal++;
            }
        }

        // finn hosliggande celler til celle

    }

    /*
     * private boolean isValidCoordinate(int pos, int max) {
     * return (pos >= 0 && pos < max);
     * }
     */

    public boolean isValidCoordinate(int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length);
    }

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

    public int getCellSize() {
        return cellSize;
    }

    // MINECOUNTING

    public MineCounter getCounter() {
        return counter;
    }

    @Override
    public void cellChanged(Cell cell) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cellChanged'");
    }

}

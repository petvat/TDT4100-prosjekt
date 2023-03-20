package project;

import java.util.ArrayList;
import java.util.List;

public class MSBoard {
    private Cell[][] grid;
    private double mineDensity;
    private int minesTotal;
    private int minesLeft;

    public MSBoard(int ySize, int xSize) {
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
            default:
                mineDensity = 0.2;
        }
        // Sett opp grid og legg til mine
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                Cell cell = new Cell(y, x, Math.random() <= mineDensity);
                grid[y][x] = cell;
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

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCellAt(int y, int x) {
        return grid[y][x];
    }

    public void setCellAt(Cell cell, int y, int x) {
        grid[y][x] = cell;
    }

    public int getRows() {
        return grid.length;
    }

    public int getCols() {
        return grid[0].length;
    }

    public void reveal(Cell cell) {
        if (cell.isFlagged())
            return;
        cell.setRevealed(true);
        if (cell.isMine()) {
            return;
        }

        List<Cell> adjacents = computeAdjacents(cell);
        // Viss ingen miner
        if (computeAdjacentMineCount(adjacents) == 0) {
            for (Cell adjacent : adjacents) {
                if (!adjacent.isRevealed()) {
                    reveal(adjacent);
                }
            }
        }
    }

    public void flag(Cell cell) {
        if (!cell.isFlagged()) {
            cell.setFlagged(true);
            minesLeft--;
        } else if (cell.isFlagged()) {
            cell.setFlagged(true);
            minesLeft++;
        }
    }

    public List<Cell> computeAdjacents(Cell cell) {
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
        List<Cell> adjacents = new ArrayList<>();
        for (int i = 0; i < adjacentPoints.length - 1; i += 2) {
            int dy = adjacentPoints[i];
            int dx = adjacentPoints[i + 1];

            int adjacentY = cell.getY() + dy;
            int adjacentX = cell.getX() + dx;

            // Sjekk om Cell gitt ved adjacentY/X finst, viss ja, legg til i liste av
            // hosliggande celler
            if (isValidCoordinate(adjacentY, adjacentX)) {
                Cell adjCell = getCellAt(adjacentY, adjacentX);
                if (!adjCell.isRevealed()) {
                    adjacents.add(adjCell);
                }
            } else {
                continue;
            }
        }
        return adjacents;
    }

    public static int computeAdjacentMineCount(List<Cell> adjacents) {
        int adjacentMineCount = 0;
        for (Cell adjacent : adjacents) {
            if (adjacent.isMine())
                adjacentMineCount++;
        }
        return adjacentMineCount;
    }
}

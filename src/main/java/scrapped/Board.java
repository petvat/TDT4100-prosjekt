package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Board {
    private Cell[][] grid;
    private int minesTotal;
    private int minesLeft;
    private int revealedNonMineCellCount;

    public Board(int ySize, int xSize, int minesTotal) {
        grid = new Cell[ySize][xSize];
        revealedNonMineCellCount = 0;
        this.minesTotal = minesTotal;
    }

    public void init() {
        minesLeft = minesTotal;
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                Cell cell = new Cell(y, x);
                cell.setIsMine(false);
                grid[y][x] = cell;
            }
        }
        for (int i = 0; i < minesTotal; i++) {
            findRandomNonMineCell().setIsMine(true);
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

    public int getRevealedNonMinCellCount() {
        return revealedNonMineCellCount;
    }

    private boolean isValidCoordinate(int y, int x) {
        return (y >= 0 && y < grid.length && x >= 0 && x < grid[y].length);
    }

    public void forEachCell(Consumer<Cell> cellConsumer) {
        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                cellConsumer.accept(getCellAt(y, x));
            }
        }
    }

    public void reveal(Cell cell) {
        if (cell.isFlagged())
            return;
        cell.setRevealed(true);
        if (cell.isMine()) {
            cell.update();
            return;
        }
        revealedNonMineCellCount++;
        List<Cell> adjacents = computeAdjacents(cell);
        int adjacentMineCount = computeAdjacentMineCount(adjacents);
        cell.setAdjacentMineCount(adjacentMineCount);
        cell.update();
        if (adjacentMineCount == 0) {
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
            cell.update();
        } else if (cell.isFlagged()) {
            cell.setFlagged(false);
            minesLeft++;
            cell.update();
        }
    }

    public void ensureSafeFirstRevealed(Cell cell) {
        if (cell.isMine()) {
            cell.setIsMine(false);
            findRandomNonMineCell().setIsMine(true);
        }
        List<Cell> adjacents = computeAdjacents(cell);
        if (computeAdjacentMineCount(adjacents) != 0) {
            for (Cell adjacent : adjacents) {
                if (adjacent.isMine()) {
                    adjacent.setIsMine(false);
                    Cell randCell = findRandomNonMineCell();
                    while (adjacents.contains(randCell)) {
                        randCell = findRandomNonMineCell();
                    }
                    randCell.setIsMine(true);
                }
            }
        }
    }

    private Cell findRandomNonMineCell() {
        Random r = new Random();
        int y = r.nextInt(getRows());
        int x = r.nextInt(getCols());
        int attemptCount = 0;
        while (getCellAt(y, x).isMine()) {
            y = r.nextInt(getRows());
            x = r.nextInt(getCols());
         }
        return getCellAt(y, x);
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
                adjacents.add(adjCell);
            } else {
                continue;
            }
        }
        return adjacents;
    }

    public int computeAdjacentMineCount(List<Cell> adjacents) {
        int adjacentMineCount = 0;
        for (Cell adjacent : adjacents) {
            if (adjacent.isMine())
                adjacentMineCount++;
        }
        return adjacentMineCount;
    }
}

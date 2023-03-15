package project;

import java.util.List;

public class Board1 {
    private Cell[][] grid;
    private int minesTotal;
    // Miner som enno ikkje er flagga
    private int mineLeft;
    private double mineDensity;
     

    // CELL UTILS
    // ein grid av hosliggande celler, der (0, 0) er cella som vert trykka på
    private int[] adjacentsPoints = new int[] {
            -1, -1,
            -1, 0,
            -1, 1,
            0, -1,
            0, 1,
            1, -1,
            1, 0,
            1, 1
    };

    public Board1(int ySize, int xSize) {
        grid = new Cell[ySize][xSize];
    }

    // init board with difficulty
    public void init(String difficulty) {
        switch(difficulty.toUpperCase()) {
            case "EASY":
                mineDensity = 0.1;
            case "NORMAL":
                mineDensity = 0.2;
            case "HARD":
                mineDensity = 0.3;
        }   
        // sett opp grid og legg til mine
        for (int y = 0; y < getRows() - 1; y++) {
            for (int x = 0; x < getCols() - 1; x++) {
                Cell cell = new Cell(y, x, Math.random() < mineDensity);
                grid[y][x] = cell;
                if (cell.isMine())
                    minesTotal++;
            }
        }
    }


    // compute hosliggande til celle
    public List<Cell> computeAdjacents() {
        if 
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


    public Board(int vh, int vw, int cellSize) {
        this.cellSize = cellSize;

        grid = new Cell[vh / cellSize][vw / cellSize];
        int mines = 0;
        for (int y = 0; y < getRowLength() - 1; y++) {
            for (int x = 0; x < getColLength() - 1; x++) {
                Cell cell = new Cell(y, x, Math.random() < mineDensity);
                grid[y][x] = cell;
                mines++;
            }
        }
        for (int y = 0; y < getRowLength() - 1; y++) {
            for (int x = 0; x < getColLength() - 1; x++) {
                getCellAt(y, x).adjacents();
            }
        }
        counter = new MineCounter(mines);
    }

}

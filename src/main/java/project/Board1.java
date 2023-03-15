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

    // BOSSBIBLIOTEK
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

       /*
     * public void createButton() {
     * 
     * }
     * 
     * public void handleFlagging(Cell cell, Button btn) {
     * cell.flag();
     * }
     * 
     * public void handleRevealing(Cell cell, Button btn) {
     * cell.reveal();
     * }
     * 
     * public void setBtnFlagged(Button btn) {
     * btn.setStyle("-fx-background-color: red;");
     * }
     * 
     * public void setBtnRevealed(Button btn, Cell cell) {
     * String mineCount = Integer.toString(cell.reveal());
     * 
     * cell.reveal();
     * btn.setText(mineCount);
     * btn.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
     * }
     * 
     * 
     * public void handleMineCountChange(Board board) {
     * // getCount()
     * }
     */
}

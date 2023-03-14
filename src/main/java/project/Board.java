package project;

public class Board implements CellListener{

    private static final int VH = 800;
    private static final int VW = 800;
    private static final int CELL_SIZE = 40;
    private static final int CELLS_PX = VW / CELL_SIZE;
    private static final int CELLS_PY = VH / CELL_SIZE;

    private Cell[][] grid = new Cell[CELLS_PY][CELLS_PX];
    // density (!)
    private double mineDensity = 0.2;
    private int mineTotal;
    private int minesLeft;
    private MineCounter counter;

    public Board() {
        int mines = 0;
        for (int y = 0; y < CELL_SIZE; y++) {
            for (int x = 0; x < CELL_SIZE; x++) {
                Cell cell = new Cell(y, x, Math.random() < mineDensity);
                cell.setBoard(this);
                grid[y][x] = cell;
                mines++;
            }
        }
        counter = new MineCounter(mines);
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCellAt(int y, int x) {
        return grid[y][x];
    }

    public int getRowLength() {
        return grid.length;
    }

    public int getColLength() {
        return grid[0].length;
    }

    public int getCellSize() {
        return CELL_SIZE;
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

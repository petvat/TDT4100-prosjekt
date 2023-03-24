package project;

import java.util.List;

public class Game {
    private int timeElapsed;
    private Board board;
    private int mines;
    private boolean isWon;
    private boolean isLost;
    private boolean isFirstRevealed;
    private String name;

    public Game(Board board, int timeElapsed, int mines) {
        this.board = board;
        this.timeElapsed = timeElapsed;
        this.mines = mines;
        isWon = false;
        isLost = false;
        isFirstRevealed = false;
    }

    public void timeElapsed() {
        timeElapsed++;
    }

    public int getMineCount() {
        return mines;
    }

    public boolean isFirstRevealed() {
        return isFirstRevealed;
    }

   public void ensureSafeFirstRevealed(Cell cell) {
        // rar delegation
        board.ensureSafeFirstRevealed(cell);
        setFirstRevealed(true);
   }

   public void setFirstRevealed(boolean isFirstRevealed) {
        this.isFirstRevealed = isFirstRevealed;
   }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWon() {
        int mines = board.getMinesTotal();
        int cellsTotal = 0;
        int revealedCells = 0;
        // Gjer i board
        for (int y = 0; y < board.getCols(); y++) {
            for (int x = 0; x < board.getRows(); x++) {
                cellsTotal++;
                if (!board.getCellAt(y, x).isMine() && board.getCellAt(y, x).isRevealed()) {
                    revealedCells++;
                }
            }
        }
        return (revealedCells + mines == cellsTotal);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void play(Cell cell) {
        board.flag(cell);
    }

    public void playReveal() {
        // board.
    }

    // kanskje game er cellListener -> game.rightClick() -> sjekk alt som i
    // Controller.cellChanged() og basert på output frå checker, call
    // Controller.setRevealGrapics/setFlagGraphics
    public boolean isLost() {
        for (int y = 0; y < board.getCols(); y++) {
            for (int x = 0; x < board.getRows(); x++) {
                if (board.getCellAt(y, x).isMine() && board.getCellAt(y, x).isRevealed()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLost() {
        for (int y = 0; y < board.getCols(); y++) {
            for (int x = 0; x < board.getRows(); x++) {
                if (board.getCellAt(y, x).isMine() && board.getCellAt(y, x).isRevealed()) {
                    board.reveal(board.getCellAt(y, x));
                }
                // set wrongly flagged, sikkert best i controller 
            }
        }
    }
    /*
     * @Override
     * public Cell cellChanged(Cell cell) {
     * if (cell.isMine()) {
     * isLost = true;
     * }
     * if (isWon) {
     * //
     * }
     * // updateAdjacentMineCount()/ få mines left
     * 
     * }
     */
}

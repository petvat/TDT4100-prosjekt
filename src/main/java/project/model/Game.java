package project.model;

public class Game {
    private int timeElapsed;
    private Board board;
    private boolean isWon;
    private boolean isLost;
    private boolean isFirstRevealed;
    private String name;

    public Game(int ySize, int xSize, int timeElapsed, int mines) {
        board = new Board(ySize, xSize, mines);
        this.timeElapsed = timeElapsed;
        isFirstRevealed = false;
        isLost = false;
        isWon = false;
        name = null;
    }

    public boolean isFirstRevealed() {
        return isFirstRevealed;
    }

    public void setFirstRevealed(boolean isFirstRevealed) {
        this.isFirstRevealed = isFirstRevealed;
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean isWon) {
        this.isWon = isWon;
        if (isWon) {
            board.forEachCell(cell -> {
                if (cell.isMine()) {
                    cell.setRevealed(true);
                }
            });
        }
    }

    public boolean isLost() {
        return isLost;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void timeElapsed() {
        timeElapsed++;
    }

    public int getTimeElapsed() {
        return timeElapsed;
    }

    public Board getBoard() {
        return board;
    }

    public void reveal(Cell cell) {
        if (isWon() || isLost()) {
            return;
        }
        if (!isFirstRevealed) {
            board.ensureSafeFirstRevealed(cell);
            setFirstRevealed(true);
        }
        board.reveal(cell);
        if (cell.isMine() && !cell.isFlagged()) {
            setLost(true);
            // Betre i Board.getGame -> men innkapsling
        } else if (board.getCols() * board.getRows() - board.getMinesTotal() == board.getRevealedNonMinCellCount()) {
            setWon(true);
        }
    }

    public void flag(Cell cell) {
        if (isWon() || isLost()) {
            return;
        }
        board.flag(cell);
    }
}

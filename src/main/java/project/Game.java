package project;

import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Game {
    private int timeElapsed;
    private Board board;
    private String difficulty;
    private boolean isWon;

    public Game(Board board, int timeElapsed, String difficulty) {
        this.board = board;
        this.timeElapsed = timeElapsed;
        this.difficulty = difficulty;
        isWon = false;
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

    public String getDifficulty() {
        return difficulty;
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

    // kanskje game er cellListener -> game.rightClick() -> sjekk alt som i Controller.cellChanged() og basert på output frå checker, call Controller.setRevealGrapics/setFlagGraphics
    public boolean isLost() {
        return false;
    }
}

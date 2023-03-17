package project;


import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Game {
    private int timeElapsed;
    private Board board;
    private String difficulty;

    public Game(Board board, int timeElapsed, String difficulty) {
        this.board = board;
        this.timeElapsed = timeElapsed;
        this.difficulty = difficulty;
        board.init(difficulty);
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

    public boolean isWon() {
        return false;
    }

    public boolean isLost() {
        return false;
    }
}

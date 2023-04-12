package project.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game(3, 3, 0, 1);
    }

    @Test
    void testGameLost() {
        for (int y = 0; y < game.getBoard().getRows(); y++) {
            for (int x = 0; x < game.getBoard().getCols(); x++) {
                Cell cell = new Cell(y, x);
                cell.setIsMine(false);
                game.getBoard().getGrid()[y][x] = cell;
            }
        }
        game.getBoard().getCellAt(1, 1).setIsMine(true);
        game.reveal(game.getBoard().getCellAt(1, 1));
        assertTrue(game.isLost());
    }
    @Test
    void testGameWon() {
        for (int y = 0; y < game.getBoard().getRows(); y++) {
            for (int x = 0; x < game.getBoard().getCols(); x++) {
                Cell cell = new Cell(y, x);
                cell.setIsMine(true);
                game.getBoard().getGrid()[y][x] = cell;
            }
        }
        game.getBoard().getCellAt(1, 1).setIsMine(false);
        game.reveal(game.getBoard().getCellAt(1, 1));
        assertTrue(game.isWon());
    }
}

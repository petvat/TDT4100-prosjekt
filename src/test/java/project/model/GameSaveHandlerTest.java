package project.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project.model.Game;
import project.model.GameSaveHandler;

public class GameSaveHandlerTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game(10, 10, 0, 1);
    }

    @Test
    void testEmptyInputThrowsIllegalArgumentException() {
        GameSaveHandler gsh = new GameSaveHandler();
        String emptyFilename = "";
        assertThrows(IllegalArgumentException.class, () -> {
            gsh.save(game, emptyFilename);
        });
    }

    @Test
    void testInputIllegalFileNameThrowsIOException() {
        GameSaveHandler gsh = new GameSaveHandler();
        String illegalFilename = "|";
        assertThrows(IOException.class, () -> {
            gsh.save(game, illegalFilename);
        });
    }

    @Test
    void testCreateFile() {
        GameSaveHandler gsh = new GameSaveHandler();
        String filename = "validName";
        gsh.save(game, filename);
        File file = new File(gsh.getPath(filename));
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    void testInputSavedGameEqualsReturnLoadedGame() {
        Game game1 = new Game(10, 10, 10, 10);
        Game game2 = new Game(10, 10, 10, 10);
    }

}
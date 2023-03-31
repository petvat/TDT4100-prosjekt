package project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BoardTest {
    @Test
    void testBoardConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Board b = new Board(0, 0, 0);
        });
    }

    @Test
    void testTotalMineCountEqualsAmountCellsThatAreMines() {
        Board b = new Board(10, 10, 20);
        b.init();
        assertEquals(20, b.getMinesTotal());
        int mineCount = 0;
        for (int y = 0; y < b.getRows(); y++) {
            for (int x = 0; x < b.getCols(); x++) {
                if (b.getCellAt(y, x).isMine())
                    mineCount++;
            }
        }
        assertEquals(20, mineCount);
    }

    @Test
    void testFlag() {
        // b.init(int mines) er betre ser eg i ettertid
        Board b = new Board(3, 3, 1);
        b.init();
        assertEquals(1, b.getMinesLeft());
        b.flag(b.getCellAt(1, 1));
        assertTrue(b.getCellAt(1, 1).isFlagged());
        assertEquals(0, b.getMinesLeft());
    }

    // HA EN initWithoutCellState()
    // bd.getCellAt(y,x).setRevealed().setFlagged.setIsMine()
    // I GameSaveHandler load()

    @Test
    void testReveal() {
        Board b = new Board(3, 3, 1);
        for (int y = 0; y < b.getRows(); y++) {
            for (int x = 0; x < b.getCols(); x++) {
                Cell cell = new Cell(y, x);
                cell.setIsMine(false);
                b.getGrid()[y][x] = cell;
            }
        }
        b.reveal(b.getCellAt(1, 1));
        assertTrue(b.getCellAt(0, 0).isRevealed());
        assertTrue(b.getCellAt(0, 1).isRevealed());
        assertTrue(b.getCellAt(0, 2).isRevealed());
        assertTrue(b.getCellAt(1, 0).isRevealed());
        assertTrue(b.getCellAt(1, 1).isRevealed());
        assertTrue(b.getCellAt(1, 2).isRevealed());
        assertTrue(b.getCellAt(2, 0).isRevealed());
        assertTrue(b.getCellAt(2, 1).isRevealed());
        assertTrue(b.getCellAt(2, 2).isRevealed());
    }
}

package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.Scanner;

public class GameSaveHandler implements IGameSaveHandler, Serializable {
    public static final String EXTENSION = "msfx";

    @Override
    public void save(String file, Game game) throws FileNotFoundException {
        // Forskjellige grids?

        try (PrintWriter wr = new PrintWriter(new File(getPath(file)))) {
            Board bd = game.getBoard();
            String gameInfo = String.format("%d,%d,%d,%d,%d, %s", bd.getCols(), bd.getRows(), game.getTimeElapsed(),
                    bd.getMinesLeft(), bd.getMinesTotal(), game.getDifficulty());
            wr.println(gameInfo);
            for (int y = 0; y < bd.getCols(); y++) {
                for (int x = 0; x < bd.getRows(); x++) {
                    Cell cell = bd.getCellAt(y, x);
                    String cellBools = String.format("%b,%b,%b", cell.isMine(), cell.isFlagged(), cell.isRevealed());
                    String cellCoords = String.format("%d,%d", y, x);

                    wr.print(cellCoords + ":" + cellBools + "\n");
                }
            }
            wr.close();
        }
        // ENKEL ? metode
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(getPath(file))));
            out.writeObject(game);
            out.close();
        }

        catch (Exception e) {
            System.out.println("File not found.");
        }
    }

    @Override
    public Game load(String file) throws FileNotFoundException {

        try (Scanner sc = new Scanner(new File(getPath(file)))) {
            String[] ps = sc.nextLine().split(",");
            int cols = Integer.parseInt(ps[0]);
            int rows = Integer.parseInt(ps[1]);
            int timeElapsed = Integer.parseInt(ps[2]);
            int minesLeft = Integer.parseInt(ps[3]);
            int minesTotal = Integer.parseInt(ps[4]);
            String difficulty = ps[5];

            Board bd = new Board(cols, rows);
            // retarda løysing
            bd.setMinesTotal(minesTotal);
            bd.setMinesLeft(minesLeft);

            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().split(":");
                String[] coords = parts[0].split(",");
                String[] bools = parts[1].split(",");
                int y = Integer.parseInt(coords[0]);
                int x = Integer.parseInt(coords[1]);
                boolean isMine = Boolean.parseBoolean(bools[0]);
                boolean isFlagged = Boolean.parseBoolean(bools[1]);
                boolean isRevealed = Boolean.parseBoolean(bools[2]);

                Cell cell = new Cell(y, x, isMine);
                // cell.setBoard(bd);
                bd.getGrid()[y][x] = cell;
                cell.setRevealed(isRevealed);
                cell.setFlagged(isFlagged);
                cell.setIsMine(isMine);
            }
            sc.close();
            /*
             * for (int y = 0; y < bd.getCols(); y++) {
             * for (int x = 0; x < bd.getRows(); x++) {
             * Cell cell = bd.getCellAt(y, x);
             * cell.computeAdjacents();
             * }
             * }
             */
            // problem overføre CellMap
            Game game = new Game(bd, timeElapsed, difficulty);
            return game;
        }
    }

    // IF # REVEALED TRUE && !cell.isRevealed() -> CELL.REVEAL()
    // IF NOT EXIST CREATE QUESTION
    private static String getPath(String file) {
        return GameSaveHandler.class.getResource("saves/").getFile() + file + "." + EXTENSION;
    }

}

/*
 * try(
 * 
 * ObjectInputStream in = new ObjectInputStream(new FileInputStream(new
 * File(getPath(file)))))
 * {
 * Game inGame = null;
 * inGame = (Game) in.readObject();
 * in.close();
 * // SETT OPP GAME
 * // MANIPULER
 * // SKAL VERE I MAIN MENU?
 * // I handleLoad() {currenctGame = this.load(file)}
 * return inGame;
 * }catch(IOException|
 * ClassNotFoundException e)
 * {
 * }
 * {
 * return null;
 * }
 * 
 * // ENTEN NY FUNKSJON -> revealNotRecursive() : TAR OG SETT TIL reveal og
 * berre
 * // det
 * // BEDRE KANSKJE -> cell.computeAdjacents
 * 
 * }
 * 
 */

// SCANNER 2D
// SCANNER -> MINE TRUE ->
// SCANNER -> FLAGGED TRUE -> FLAG()
// ALLE
// SCANNER -> REVEALED TRUE -> REVEAL()
// .................

// handle update in Controller

// anna metode
// if ("TRUE" && !cell.isMine()) {
// cell.setIsMine(true);
// }
// if ("TRUE" && !cell.isFlagged()) {
// cell.flag();
// då vert controller automatisk updated
// kva med tinga som ikkje vert påverka?
// vi MÅ kunne kalle cell.update() på ein annan måte då
// Stor flaw! må uansett forall update()
// workaround: if (!isflagged && !isRevealed) update()
// }
// else if ("TRUE" && !cell.isRevealed()) {
// cell.reveal();
// }

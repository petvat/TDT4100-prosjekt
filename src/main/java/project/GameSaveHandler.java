package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameSaveHandler {
    public static final String EXTENSION = ".msfx";
    public static final String PATH = "src/main/resources/project/saves/";

    public void save(Game game) throws FileNotFoundException {
        File file = new File(getPath(game.getName()));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                System.out.println("File could not get created");
            }
        }
        try (PrintWriter wr = new PrintWriter(file)) {
            Board bd = game.getBoard();
            String gameInfo = String.format("%d,%d,%d,%d,%d, %s", bd.getCols(), bd.getRows(), game.getTimeElapsed(),
                    bd.getMinesLeft(), bd.getMinesTotal(), game.getDifficulty());
            wr.print(gameInfo);
            for (int y = 0; y < bd.getCols(); y++) {
                for (int x = 0; x < bd.getRows(); x++) {
                    wr.print("\n");
                    Cell cell = bd.getCellAt(y, x);
                    String cellBools = String.format("%d,%d,%d,%d", cell.isMine() ? 1 : 0, cell.isFlagged() ? 1 : 0,
                            cell.isRevealed() ? 1 : 0, cell.getAdjacentMineCount());
                    String cellCoords = String.format("%d,%d", y, x);

                    wr.print(cellCoords + ":" + cellBools);
                }
            }
            wr.close();
        } catch (Exception e) {
            System.out.println("could not save file. " + e);
        }
    }


    public Game load(String filename) throws FileNotFoundException {
        File file = new File(PATH + filename + EXTENSION);
        try (Scanner sc = new Scanner(file)) {
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
                boolean isMine = Integer.parseInt(bools[0]) == 1 ? true : false;
                boolean isFlagged = Integer.parseInt(bools[1]) == 1 ? true : false;
                boolean isRevealed = Integer.parseInt(bools[2]) == 1 ? true : false;
                int adjacentMineCount = Integer.parseInt(bools[3]);

                Cell cell = new Cell(y, x, isMine);
                // cell.setBoard(bd);
                cell.setRevealed(isRevealed);
                cell.setFlagged(isFlagged);
                cell.setIsMine(isMine);
                cell.setAdjacentMineCount(adjacentMineCount);
                bd.getGrid()[y][x] = cell;
            }
            sc.close();
            Game game = new Game(bd, timeElapsed, difficulty);
            return game;
        }
    }
    
    private static String getPath(String filename) {
        return (PATH + filename + EXTENSION);
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

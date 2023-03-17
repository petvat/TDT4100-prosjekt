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

public class GameSaveHandler implements IGameSaveHandler, Serializable {

    @Override
    public void save(String file, Game game) throws FileNotFoundException {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getPath(file)));
            out.writeObject(game);
            out.close();
        }

        catch (Exception e) {
            System.out.println("File not found.");
        }
    }

    @Override
    public Game load(String file) throws FileNotFoundException {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(getPath(file)));
            Game inGame = null;
            inGame = (Game) in.readObject();
            in.close();

            // SETT OPP GAME
            // MANIPULER
            // SKAL VERE I MAIN MENU?

            // I handleLoad() {currenctGame = this.load(file)}
            Game game;

            return game;
        }

        catch (Exception e) {
            System.out.println("File not found.");
        }
    }

    private static String getPath(String file) {
        return GameSaveHandler.class.getResource("saves/").getFile() + file + ".txt";
    }

    public static void main(String[] args) {

    }
}

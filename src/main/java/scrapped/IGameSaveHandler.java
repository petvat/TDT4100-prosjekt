package project;

import java.io.FileNotFoundException;

public interface IGameSaveHandler {
    public void save(String file, Game game) throws FileNotFoundException;
    public Game load(String file) throws FileNotFoundException;
}

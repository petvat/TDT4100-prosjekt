package project;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class ScreenController {
    private HashMap<String, Pane> screenMap;
    private Scene main;

    public ScreenController(Scene main) {
        this.main = main;
    }

    public void addScene(String name, Pane scene) {
        screenMap.put(name, scene);
    }

    public void removeScene(String name) {
        screenMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot(screenMap.get(name));
    }
}

package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.terrain.Terrain;
import main.java.windows.MapWindow;
import main.java.windows.ControlWindow;

import java.util.Vector;

public class Main extends Application {

    public String workingDirectory = System.getProperty("user.dir");

    public MapWindow mapStage;
    public ControlWindow panelWrap;
    public Terrain terrain;

    public Vector<Airport> airports = new Vector<Airport>();

    public void start(Stage stage) {
        mapStage = new MapWindow(this, stage);
        panelWrap = new ControlWindow(this);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
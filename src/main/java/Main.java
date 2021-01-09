package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.terrain.Terrain;
import main.java.vehicles.*;
import main.java.windows.MapWindow;
import main.java.windows.ControlWindow;

import java.util.Vector;

public class Main extends Application {

    public String workingDirectory = System.getProperty("user.dir");

    public MapWindow mapStage;
    public ControlWindow panelWrap;
    public TabsUpdater tabsUpdater;
    public Terrain terrain;

    public Vector<Airport> airports = new Vector<>();

    public Vector<PassengerPlane> passengerPlanes = new Vector<>();
    public Vector<FighterPlane> fighterPlanes = new Vector<>();
    public Vector<Ferry> ferries = new Vector<>();
    public Vector<Carrier> carriers = new Vector<>();

    public void start(Stage stage) {
        mapStage = new MapWindow(this, stage);
        panelWrap = new ControlWindow(this);
        tabsUpdater = new TabsUpdater(panelWrap);
        tabsUpdater.start();
    }

    /**
     * Stopps all the Threads created during runtime.
     */
    public void stop() {
        for (Vehicle v : passengerPlanes) {
            v.stop();
        }
        for (Vehicle v : fighterPlanes) {
            v.stop();
        }
        for (Vehicle v : ferries) {
            v.stop();
        }
        for (Vehicle v : carriers) {
            v.stop();
        }
        tabsUpdater.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
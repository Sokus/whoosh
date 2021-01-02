package main.java;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.terrain.Terrain;
import main.java.utility.Vector3D;
import main.java.vehicles.*;
import main.java.windows.MapWindow;
import main.java.windows.ControlWindow;

import java.util.Vector;

public class Main extends Application {

    public String workingDirectory = System.getProperty("user.dir");

    public MapWindow mapStage;
    public ControlWindow panelWrap;
    public Terrain terrain;

    public Vector<Airport> airports = new Vector<>();

    public Vector<PassengerPlane> passengerPlanes = new Vector<>();
    public Vector<FighterPlane> fighterPlanes = new Vector<>();
    public Vector<Ferry> ferries = new Vector<>();
    public Vector<Carrier> carriers = new Vector<>();

    public void start(Stage stage) {
        mapStage = new MapWindow(this, stage);
        panelWrap = new ControlWindow(this);

        mapStage.createAirports(5);

        Vector<Airport> path = new Vector<>(airports);
        PassengerPlane plane = new PassengerPlane(0, new Vector3D(0,7, 0),5);
        plane.path = path;
        plane.nextAirport = path.get(0);
        mapStage.planeModels.getChildren().add(plane.getModel());
        new Thread(plane).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
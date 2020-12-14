package main.java;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Vector;

public class Main extends Application {

    MapWrap mapWrap;
    PanelWrap panelWrap;
    Vector<Airport> airports = new Vector<Airport>();

    public void start(Stage stage) {
        mapWrap = new MapWrap(this, stage);
        panelWrap = new PanelWrap(this);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
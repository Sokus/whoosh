package main.java.vehicles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import main.java.utility.Vector3D;

public abstract class Vehicle implements Runnable {
    int updateDelay = 17;
    volatile boolean running = true;

    public int UID;
    public String name;
    public Vector3D position;
    Vector3D initialPosition;
    public double maxSpeed; // units per second
    double cruiseLevel;

    Sphere model;

    Vehicle(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        this.UID = UID;
        this.position = new Vector3D(position);
        this.initialPosition = new Vector3D(position);
        this.maxSpeed = maxSpeed;
        this.cruiseLevel = cruiseLevel;

        model = new Sphere();
        model.setTranslateX(position.x);
        model.setTranslateY(position.y);
        model.setTranslateZ(position.z);

        model.setRadius(0.2);

        model.setMaterial(new PhongMaterial(Color.WHITE));
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public Sphere getModel() {
        return model;
    }

    public void setDarkColor() {
        model.setMaterial(new PhongMaterial(Color.WHITE));
    }

    public void setBrightColor() {
        model.setMaterial(new PhongMaterial(Color.CYAN));
    }
}

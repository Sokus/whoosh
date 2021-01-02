package main.java.vehicles;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import main.java.utility.Vector3D;

public abstract class Vehicle implements Runnable {
    int updateDelay = 17;
    boolean running = true;

    int UID;
    Vector3D position;
    double maxSpeed; // units per second

    Sphere model;

    Vehicle(int UID, Vector3D position, double maxSpeed) {
        this.UID = UID;
        this.position = new Vector3D(position);
        this.maxSpeed = maxSpeed;

        model = new Sphere();
        model.setTranslateX(position.x);
        model.setTranslateY(position.y);
        model.setTranslateZ(position.z);

        model.setRadius(0.2);

        model.setMaterial(new PhongMaterial(Color.WHITE));
    }

    public Sphere getModel() {
        return model;
    }
}

package main.java.vehicles;

import main.java.utility.Vector3D;

public abstract class Ship extends Vehicle {
    Ship(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }

    @Override
    public void run() {

    }
}

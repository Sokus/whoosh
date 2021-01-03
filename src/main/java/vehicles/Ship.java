package main.java.vehicles;

import main.java.utility.Vector3D;

public abstract class Ship extends Vehicle {
    Ship(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }

    @Override
    public void run() {

    }
}

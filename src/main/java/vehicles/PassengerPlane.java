package main.java.vehicles;

import main.java.utility.Vector3D;

public class PassengerPlane extends Plane{
    int currentPassengers;
    int maxPassengers;

    public PassengerPlane(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }
}

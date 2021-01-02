package main.java.vehicles;

import main.java.utility.Vector3D;

public class Ferry extends Ship{
    int currentPassengers;
    int maxPassengers;
    String companyName;

    Ferry(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }
}

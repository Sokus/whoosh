package main.java.vehicles;

import main.java.utility.Vector3D;

public class Ferry extends Ship{
    public int currentPassengers;
    public int maxPassengers = 500;
    public String companyName;

    public Ferry(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }
}

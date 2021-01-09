package main.java.vehicles;

import main.java.utility.Vector3D;

import java.util.Random;

public class PassengerPlane extends Plane{
    public int currentPassengers;
    int maxPassengers = 200;

    public PassengerPlane(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }

    void ChangePassengers() throws InterruptedException {
        while(running && currentPassengers > 0) {
            Thread.sleep(10);
            currentPassengers--;
        }
        currentPassengers = 0;
        int passengersToEnter = new Random().nextInt(maxPassengers/2) + maxPassengers/2;
        while(running && currentPassengers < passengersToEnter) {
            Thread.sleep(10);
            currentPassengers++;
        }
    }

    protected void DoStationary() throws InterruptedException {
        super.DoStationary();
        ChangePassengers();
    }
}

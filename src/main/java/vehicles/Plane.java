package main.java.vehicles;

import javafx.application.Platform;
import main.java.Airport;
import main.java.utility.Vector3D;

import java.util.Vector;

public abstract class Plane extends Vehicle {
    State state = State.TRAVEL;

    public Vector<Airport> path = new Vector<>();
    public Airport nextAirport = null;
    public Airport lastAirport = null;
    public int currentAirportIndex = 0;
    double distanceBetweenAirports;

    public int fuelCapacity = 5000;
    public int currentFuel = 5000;

    Plane(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }

    void DoTravel() {
        if (nextAirport != null) {
            Vector3D lastAirportPosition2D;
            if(lastAirport == null) {
                lastAirportPosition2D = Vector3D.Flat(initialPosition);
            } else {
                lastAirportPosition2D = Vector3D.Flat(lastAirport.position);
            }
            Vector3D nextAirportPosition2D = Vector3D.Flat(nextAirport.position);
            Vector3D position2D = Vector3D.Flat(position);
            double distanceFromLastAirport = Vector3D.Mag(Vector3D.Sub(position2D, lastAirportPosition2D));
            double distanceToNextAirport = Vector3D.Mag(Vector3D.Sub(position2D, nextAirportPosition2D));
            double targetAttitude;
            if(lastAirport == null) {
                targetAttitude = Math.min(cruiseLevel, Math.min(initialPosition.y + distanceFromLastAirport, nextAirport.position.y + distanceToNextAirport));
            } else {
                targetAttitude = Math.min(cruiseLevel, Math.min(lastAirport.position.y + distanceFromLastAirport, nextAirport.position.y + distanceToNextAirport));
            }
            Vector3D difference = Vector3D.Sub(position2D, nextAirportPosition2D);
            double distance = Vector3D.Mag(difference);
            double positionDelta = maxSpeed * updateDelay / 1000;
            if (distance > positionDelta) {
                Vector3D direction = Vector3D.Norm(difference);
                Vector3D translate = Vector3D.Scale(direction, positionDelta);
                position = Vector3D.Add(position, translate);
                position.y = targetAttitude;
                currentFuel -= maxSpeed;
            } else {
                state = State.LANDING;
            }
        }
    }

    void DoLanding() {
        state = State.STATIONARY;
    }

    void RefillFuel() throws InterruptedException {
        int fuelToRefill = fuelCapacity - currentFuel;
        if(fuelToRefill > 0){
            while(running && fuelToRefill > 0){
                Thread.sleep(5);
                fuelToRefill--;
                currentFuel++;
            }
        }
        currentFuel = fuelCapacity;
    }

    protected void DoStationary() throws InterruptedException {
        RefillFuel();
        state = State.TAKEOFF;
    }

    void DoTakeOff() {
        currentAirportIndex = (currentAirportIndex + 1) % path.size();
        nextAirport = path.get(currentAirportIndex);
        if(currentAirportIndex > 0) {
            lastAirport = path.get(currentAirportIndex-1);
        } else {
            lastAirport = path.get(path.size()-1);
        }
        distanceBetweenAirports = Vector3D.Mag(Vector3D.Sub(nextAirport.position, lastAirport.position));
        state = State.TRAVEL;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(updateDelay);
                if (path.size() > 1) {
                    switch (state) {
                        case TRAVEL -> DoTravel();
                        case LANDING -> DoLanding();
                        case STATIONARY -> DoStationary();
                        case TAKEOFF -> DoTakeOff();
                    }
                }
                Platform.runLater(() -> {
                    model.setTranslateX(position.x);
                    model.setTranslateY(-position.y);
                    model.setTranslateZ(position.z);
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

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
    double distanceBetweenAirports;
    int fuel;
    int personnel;

    Plane(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }

    void DoTravel() {
        if (nextAirport != null && lastAirport != null) {
            Vector3D lastAirportPosition2D = Vector3D.Flat(lastAirport.position);
            Vector3D nextAirportPosition2D = Vector3D.Flat(nextAirport.position);
            Vector3D position2D = Vector3D.Flat(position);
            double distanceFromLastAirport = Vector3D.Mag(Vector3D.Sub(position2D, lastAirportPosition2D));
            double distanceToNextAirport = Vector3D.Mag(Vector3D.Sub(position2D, nextAirportPosition2D));
            double targetAttitude = Math.min(cruiseLevel, Math.min(lastAirport.position.y+distanceFromLastAirport, nextAirport.position.y+distanceToNextAirport));
            Vector3D difference = Vector3D.Sub(position2D, nextAirportPosition2D);
            double distance = Vector3D.Mag(difference);
            double positionDelta = maxSpeed * updateDelay / 1000;
            if (distance > positionDelta) {
                Vector3D direction = Vector3D.Norm(difference);
                Vector3D translate = Vector3D.Scale(direction, positionDelta);
                position = Vector3D.Add(position, translate);
                position.y = targetAttitude;
            } else {
                state = State.LANDING;
            }
        }
    }

    void DoLanding() throws InterruptedException {
        state = State.STATIONARY;
    }

    void DoStationary() throws InterruptedException {
        Thread.sleep(1000);
        state = State.TAKEOFF;
    }

    void DoTakeOff() throws InterruptedException {
        state = State.TRAVEL;
        for(int i=0; i<path.size()-1; i++){
            path.set(i, path.get(i+1));
        }
        path.set(path.size()-1,nextAirport);
        nextAirport = path.get(0);
        lastAirport = path.get(path.size()-1);
        distanceBetweenAirports = Vector3D.Mag(Vector3D.Sub(nextAirport.position, lastAirport.position));
    }

    @Override
    public void run() {
        try {
            while (running) {
                if (path.size() > 0) {
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
                Thread.sleep(updateDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

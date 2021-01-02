package main.java.vehicles;

import main.java.Airport;
import main.java.utility.Vector3D;

import java.util.Vector;

public abstract class Plane extends Vehicle {
    State state = State.TRAVEL;

    public Vector<Airport> path = new Vector<>();
    public Airport nextAirport = null;
    int fuel;
    int personel;

    Plane(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }

    void DoTravel() {
        if (nextAirport != null) {
            Vector3D difference = Vector3D.Sub(position, new Vector3D(nextAirport.position.x, position.y, nextAirport.position.z));
            double distance = Vector3D.Mag(difference);
            double positionDelta = maxSpeed * updateDelay / 1000;
            if (distance > positionDelta) {
                Vector3D direction = Vector3D.Norm(difference);
                Vector3D translate = Vector3D.Scale(direction, positionDelta);
                position = Vector3D.Add(position, translate);
                model.setTranslateX(position.x);
                model.setTranslateY(-position.y);
                model.setTranslateZ(position.z);
            } else {
                state = State.LANDING;
            }
        }
    }

    void DoLanding() throws InterruptedException {
        System.out.println("Plane " + UID + " is landing at " + nextAirport.name);
        Thread.sleep(1000);
        state = State.STATIONARY;
    }

    void DoStationary() throws InterruptedException {
        System.out.println("Plane " + UID + " has landed at " + nextAirport.name);
        Thread.sleep(3000);
        state = State.TAKEOFF;
    }

    void DoTakeOff() throws InterruptedException {
        System.out.println("Plane " + UID + " is taking off at " + nextAirport.name);
        Thread.sleep(1000);
        state = State.TRAVEL;
        for(int i=0; i<path.size()-1; i++){
            path.set(i, path.get(i+1));
        }
        path.set(path.size()-1,nextAirport);
        nextAirport = path.get(0);
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
                Thread.sleep(updateDelay);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

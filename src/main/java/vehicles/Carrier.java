package main.java.vehicles;

import main.java.utility.Vector3D;

public class Carrier extends Ship {
    public WeaponType weaponType;
    public int planesCreated;

    public Carrier(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }
}

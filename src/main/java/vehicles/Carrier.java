package main.java.vehicles;

import main.java.utility.Vector3D;

public class Carrier extends Ship {
    WeaponType weaponType;

    Carrier(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }
}

package main.java.vehicles;

import main.java.utility.Vector3D;

public class FighterPlane extends Plane{
    WeaponType weaponType;

    public FighterPlane(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
    }
}

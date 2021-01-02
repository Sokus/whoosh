package main.java.vehicles;

import main.java.utility.Vector3D;

public class FighterPlane extends Plane{
    WeaponType weaponType;

    FighterPlane(int UID, Vector3D position, double maxSpeed) {
        super(UID, position, maxSpeed);
    }
}

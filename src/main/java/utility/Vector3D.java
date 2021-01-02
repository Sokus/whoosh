package main.java.utility;

public class Vector3D {
    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D vector3D) {
        this.x = vector3D.x;
        this.y = vector3D.y;
        this.z = vector3D.z;
    }

    public static Vector3D Add(Vector3D a, Vector3D b) {
        return new Vector3D(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vector3D Sub(Vector3D a, Vector3D b) {
        return new Vector3D(b.x - a.x, b.y - a.y, b.z - a.z);
    }

    public static Vector3D Scale(Vector3D a, double scale) {
        return new Vector3D(a.x * scale, a.y * scale, a.z * scale);
    }

    public static Vector3D Divide(Vector3D a, double divider) {
        return new Vector3D(a.x / divider, a.y / divider, a.z / divider);
    }

    public static Vector3D Norm(Vector3D a) {
        double magnitude = Vector3D.Mag(a);
        return new Vector3D(a.x / magnitude, a.y / magnitude, a.z / magnitude);
    }

    public static double Mag(Vector3D a) {
        return Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2) + Math.pow(a.z, 2));
    }

    public static double Dot(Vector3D a, Vector3D b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
}

package main.java;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraController{
    private PerspectiveCamera camera;

    Point3D focusPoint = new Point3D(0,0,0);
    double angleAlpha = 0;
    double angleBeta = 0;
    double distance = 60;

    public void setCameraPosition(double alpha, double beta, double distance){
        this.angleBeta = beta;
        this.distance = distance;
        this.angleAlpha = Math.min(85,Math.max(15,alpha));

        double radA = Math.toRadians(alpha);
        double radB = Math.toRadians(beta);

        double y = Math.sin(radA);
        double x = Math.sin(radB)*Math.cos(radA);
        double z = -Math.cos(radB)*Math.cos(radA);
        x = x*distance + focusPoint.getX();
        y = -y*distance + focusPoint.getY();
        z = z*distance + focusPoint.getZ();
        Translate translate = new Translate(x,y,z);
        Rotate rotAlpha = new Rotate(-alpha,Rotate.X_AXIS);
        Rotate rotBeta = new Rotate(-beta, Rotate.Y_AXIS);
        camera.getTransforms().setAll(translate,rotBeta, rotAlpha);
    }

    public void moveCameraRelative(double alpha, double beta, double distance)
    {
        setCameraPosition(this.angleAlpha+alpha, this.angleBeta+beta, this.distance + distance);
    }

    public CameraController(Point3D focusPoint, double angleAlpha, double angleBeta, double distance){
        camera = new PerspectiveCamera(true);
        camera.setFarClip(1000);
        this.focusPoint = focusPoint;
        setCameraPosition(angleAlpha,angleBeta,distance);
    }

    public PerspectiveCamera getCamera() { return camera; }
}

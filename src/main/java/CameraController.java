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

    double alphaMax = 90;
    double alphaMin = -90;

    public void setRotationAbsolute(double alpha, double beta, double distance){

        alpha = Math.min(alphaMax,Math.max(alphaMin,alpha));
        this.angleAlpha = alpha;
        this.angleBeta = beta;
        this.distance = distance;

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

    public void setRotationRelative(double alpha, double beta, double distance)
    {
        setRotationAbsolute(this.angleAlpha+alpha, this.angleBeta+beta, this.distance + distance);
    }

    public CameraController(){
        camera = new PerspectiveCamera(true);
        camera.setFarClip(1000);
    }

    public PerspectiveCamera getCamera() { return camera; }
}

package main.java;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class Airport {
    String name;
    PlaneType planeType;
    Translate position;
    boolean busy = false;
    Box model = new Box();

    public Airport(Group terrain, String name, PlaneType planeType, double positionX, double positionZ)
    {
        this.name = name;
        positionX = Math.round(positionX);
        positionZ = Math.round(positionZ);
        double minY, maxY;
        ObservableList<Node> nodes = terrain.getChildren();
        minY = Double.POSITIVE_INFINITY;
        maxY = Double.NEGATIVE_INFINITY;

        for (Node node : nodes)
        {
            Box box = (Box)node;
            if(Math.abs(box.getTranslateX()-positionX)<1.5 && Math.abs(box.getTranslateZ()-positionZ)<1.5)
            {
                double boxHeight = box.getHeight();
                if(boxHeight<minY) minY = boxHeight;
                if(boxHeight>maxY) maxY = boxHeight;
            }
        }
        maxY += 0.1;
        model.setTranslateX(positionX);
        model.setTranslateY(-(minY+maxY)/2);
        model.setTranslateZ(positionZ);

        model.setWidth(1.2);
        model.setHeight(maxY-minY);
        model.setDepth(1.2);
        model.setMaterial(new PhongMaterial(Color.WHITE));
    }

    public Box getModel() { return model; }
}

package main.java;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import main.java.terrain.Terrain;
import main.java.utility.Vector3D;
import main.java.vehicles.PlaneType;


public class Airport {
    public int ID;
    public String name;
    public PlaneType planeType;
    public Vector3D position;
    boolean busy = false;
    Box model = new Box();

    public Airport(Terrain terrain, int ID, String name, PlaneType planeType, int positionX, int positionZ) {
        this.ID = ID;
        this.name = name;
        this.planeType = planeType;
        position = new Vector3D(positionX, 0, positionZ);
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (positionX + x < 0 || positionX + x >= terrain.size[0] || positionZ + z < 0 || positionZ + z >= terrain.size[1]) {
                    continue;
                }
                Box box = terrain.getChunk(positionX + x, positionZ + z).model;
                if (box != null) {
                    double boxHeight = box.getHeight();
                    if (boxHeight < minY) minY = boxHeight;
                    if (boxHeight > maxY) maxY = boxHeight;
                }

            }
        }

        maxY += 0.1;
        model.setTranslateX(position.x);
        model.setTranslateY(-(minY + maxY) / 2);
        model.setTranslateZ(position.z);

        model.setWidth(1.2);
        model.setHeight(maxY - minY);
        model.setDepth(1.2);
        position.y = -model.getTranslateY() + model.getHeight()/2;
        setDarkColor();
    }

    public Box getModel() {
        return model;
    }

    public void setDarkColor() {
        model.setMaterial(new PhongMaterial(Color.GRAY));
    }

    public void setBrightColor() {
        model.setMaterial(new PhongMaterial(Color.WHITE));
    }

}

package main.java.vehicles;

import javafx.application.Platform;
import main.java.terrain.Chunk;
import main.java.terrain.Terrain;
import main.java.terrain.TerrainType;
import main.java.utility.Vector3D;

import java.util.Random;

public abstract class Ship extends Vehicle {
    Vector3D direction;
    public Terrain terrain;
    Random random;

    Ship(int UID, Vector3D position, double maxSpeed, double cruiseLevel) {
        super(UID, position, maxSpeed, cruiseLevel);
        random = new Random();
        direction = new Vector3D(0,0,0);
        SetRandomDirection();
    }

    void DoTravel() {
        double positionDelta = maxSpeed * updateDelay / 1000;
        boolean repeat = false;
        Vector3D translate, nextPosition;
        do {
            translate = Vector3D.Scale(direction, positionDelta);
            nextPosition = Vector3D.Add(position, translate);
            int nextPositionX = (int) Math.round(nextPosition.x);
            int nextPositionZ = (int) Math.round(nextPosition.z);
            Chunk chunk = terrain.getChunk(nextPositionX, nextPositionZ);
            if (chunk == null || chunk.type == TerrainType.LAND) {
                repeat = true;
                SetRandomDirection();
            } else {
                repeat = false;
            }
        } while (repeat);
        position = nextPosition;
    }

    void SetRandomDirection() {
        double randomX = random.nextDouble()*2 - 1;
        double randomZ = random.nextDouble()*2 - 1;
        direction = Vector3D.Norm(new Vector3D(randomX, 0, randomZ));
    }

    @Override
    public void run() {
        try {
            while (running) {
                Thread.sleep(updateDelay);
                DoTravel();
                Platform.runLater(() -> {
                    model.setTranslateX(position.x);
                    model.setTranslateY(-position.y);
                    model.setTranslateZ(position.z);
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

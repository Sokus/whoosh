package main.java.terrain;

import javafx.scene.shape.Box;

public class Chunk {
    public TerrainType type;
    public Box model;

    public Chunk(TerrainType type, Box model) {
        this.type = type;
        this.model = model;
    }
}

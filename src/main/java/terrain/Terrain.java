package main.java.terrain;

import java.util.Vector;

public class Terrain {
    Vector<Vector<Chunk>> chunks;
    public int[] size;

    public Terrain(int x, int z) {
        size = new int[] {x,z};
        chunks = new Vector<>(x);
        for(int ix=0; ix<x; ix++){
            Vector<Chunk> temporary = new Vector<>(z);
            for(int iz=0; iz<z; iz++)
            {
                temporary.add(new Chunk(TerrainType.LAND, null));
            }
            chunks.add(temporary);
        }
    }

    public Chunk getChunk(int x, int z) {
        if(x >= 0 && x < chunks.size() && z>= 0 && z<chunks.get(0).size()){
            return chunks.get(x).get(z);
        } else {
            return null;
        }
    }

    public void setChunk(int x, int z, Chunk chunk) {
        if(x >= 0 && x < chunks.size() && z>= 0 && z<chunks.get(0).size()){
            chunks.get(x).get(z).type = chunk.type;
            chunks.get(x).get(z).model = chunk.model;
        }
    }


}

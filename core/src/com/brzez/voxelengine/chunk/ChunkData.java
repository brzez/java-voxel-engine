package com.brzez.voxelengine.chunk;

public class ChunkData {
    public final short size;
    protected final byte data[];

    public ChunkData(int size) {
        this.size = (short)size;
        this.data = new byte[size * size * size];
    }

    public byte get(int x, int y, int z)
    {
        return data[x * size + y * size * size + z];
    }


    public byte set(int x, int y, int z, byte value)
    {
        return data[x * size + y * size * size + z] = value;
    }
}

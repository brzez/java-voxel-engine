package com.brzez.voxelengine.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class ChunkModelBuilder {
    protected final float[] vertexBuffer;
    protected int vertexBufferPosition = 0;
    protected final short[] indexBuffer;
    protected int indexBufferPosition = 0;

    protected int nVerts = 0;

    protected final float blockSize = .5f;
    private ChunkData chunkData;

    public ChunkModelBuilder() {
        vertexBuffer = new float[16 * 16 * 16 * 3 * 2 * 8 * 2]; // no idea how big it should be. Don't care for now.
        indexBuffer  = new short[1337 * 8 * 8]; // 8-)
    }

    protected void addVertex(float x, float y, float z, Vector3 normal){
        nVerts++;
        vertexBuffer[vertexBufferPosition++] = x;
        vertexBuffer[vertexBufferPosition++] = y;
        vertexBuffer[vertexBufferPosition++] = z;

        vertexBuffer[vertexBufferPosition++] = normal.x;
        vertexBuffer[vertexBufferPosition++] = normal.y;
        vertexBuffer[vertexBufferPosition++] = normal.z;
    }

    protected void addIndex(int... indices){
        for(int i: indices){
            indexBuffer[indexBufferPosition++] = (short)(nVerts + i);
        }
    }

    protected void addMeshFromBuffers(final MeshPartBuilder meshBuilder)
    {
        float[] vertices = new float[vertexBufferPosition];
        short[] indices = new short[indexBufferPosition];

        System.arraycopy(vertexBuffer, 0, vertices, 0, vertexBufferPosition);
        System.arraycopy(indexBuffer, 0, indices, 0, indexBufferPosition);

        meshBuilder.addMesh(vertices, indices);

        vertexBufferPosition = indexBufferPosition = nVerts = 0;
    }

    protected boolean isEmpty(int x,int y,int z){
        if(x < 0 || x > chunkData.size){
            return false;
        }
        if(y < 0 || y > chunkData.size){
            return false;
        }
        if(z < 0 || z > chunkData.size){
            return false;
        }

        return chunkData.get(x,y,z) == 0;
    }

    protected void addBlock(int x,int y, int z){
        final int sides = getBlockSides(x,y,z);
        final float halfSize = blockSize * .5f;

        final Vector3 position = new Vector3(x,y,z).scl(blockSize);
        Vector3 normal = new Vector3();

        if((sides & ChunkBlockSide.FRONT) != 0) {
            normal.set(0, 0, 1);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + -halfSize, position.y + halfSize, position.z + halfSize, normal);
            addVertex(position.x + -halfSize, position.y + -halfSize, position.z + halfSize, normal);
            addVertex(position.x + halfSize, position.y + -halfSize, position.z + halfSize, normal);
            addVertex(position.x + halfSize, position.y + halfSize, position.z + halfSize, normal);
        }
        if((sides & ChunkBlockSide.BACK) != 0) {
            normal.set(0, 0, -1);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + halfSize, position.y + -halfSize, position.z - halfSize, normal);
            addVertex(position.x + -halfSize, position.y + -halfSize, position.z - halfSize, normal);
            addVertex(position.x + -halfSize, position.y + halfSize, position.z - halfSize, normal);
            addVertex(position.x + halfSize, position.y + halfSize, position.z - halfSize, normal);
        }
        if((sides & ChunkBlockSide.TOP) != 0) {
            normal.set(0,-1,0);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x - halfSize, position.y - halfSize, position.z + halfSize, normal);
            addVertex(position.x - halfSize, position.y - halfSize, position.z - halfSize, normal);
            addVertex(position.x + halfSize, position.y - halfSize, position.z - halfSize, normal);
            addVertex(position.x + halfSize,  position.y - halfSize, position.z + halfSize, normal);
        }
        if((sides & ChunkBlockSide.BOTTOM) != 0) {
            normal.set(0,1,0);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + halfSize, position.y + halfSize, position.z - halfSize, normal);
            addVertex(position.x - halfSize, position.y + halfSize, position.z - halfSize, normal);
            addVertex(position.x - halfSize, position.y + halfSize, position.z + halfSize, normal);
            addVertex(position.x + halfSize,  position.y + halfSize, position.z + halfSize, normal);
        }
        if((sides & ChunkBlockSide.LEFT) != 0) {
            normal.set(-1,0,0);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x - halfSize, position.y + halfSize, position.z - halfSize, normal);
            addVertex(position.x - halfSize, position.y - halfSize, position.z - halfSize, normal);
            addVertex(position.x - halfSize, position.y - halfSize, position.z + halfSize, normal);
            addVertex(position.x - halfSize,  position.y + halfSize, position.z + halfSize, normal);
        }
        if((sides & ChunkBlockSide.RIGHT) != 0) {
            normal.set(1,0,0);
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + halfSize, position.y - halfSize, position.z + halfSize, normal);
            addVertex(position.x + halfSize, position.y - halfSize, position.z - halfSize, normal);
            addVertex(position.x + halfSize, position.y + halfSize, position.z - halfSize, normal);
            addVertex(position.x + halfSize,  position.y + halfSize, position.z + halfSize, normal);
        }
    }

    protected int getBlockSides(int x,int y, int z){
        if(chunkData.get(x,y,z) == 0){
            return 0;
        }
        int sides = 0;

        if(isEmpty(x,y,z + 1)){
            sides |= ChunkBlockSide.FRONT;
        }
        if(isEmpty(x,y,z - 1)){
            sides |= ChunkBlockSide.BACK;
        }

        if(isEmpty(x,y - 1,z)){
            sides |= ChunkBlockSide.TOP;
        }
        if(isEmpty(x,y + 1,z)){
            sides |= ChunkBlockSide.BOTTOM;
        }
        if(isEmpty(x - 1,y,z)){
            sides |= ChunkBlockSide.LEFT;
        }
        if(isEmpty(x + 1,y,z)){
            sides |= ChunkBlockSide.RIGHT;
        }

        return sides;
    }

    public Model build(ChunkData chunkData)
    {
        this.chunkData = chunkData;
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = builder.part("potato", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.FOREST)));

        for(int x = 0; x < chunkData.size; x++){
            for(int y = 0;y < chunkData.size; y++){
                for(int z = 0;z < chunkData.size; z++){
                    addBlock(x,y,z);
                }
            }
        }
        System.out.println("Verts: " + nVerts + " tris: " + (nVerts / 6));
        addMeshFromBuffers(meshBuilder);
        return builder.end();
    }
}

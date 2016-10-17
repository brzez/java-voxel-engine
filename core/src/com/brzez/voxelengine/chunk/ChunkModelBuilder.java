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
        vertexBuffer = new float[16 * 16 * 16 * 3 * 2 * 8]; // no idea how big it should be. Don't care for now.
        indexBuffer  = new short[1337 * 8 * 8]; // 8-)
    }

    protected void addVertex(float x, float y, float z){
        nVerts++;
        vertexBuffer[vertexBufferPosition++] = x;
        vertexBuffer[vertexBufferPosition++] = y;
        vertexBuffer[vertexBufferPosition++] = z;
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

    protected void addBlock(int x,int y, int z){
        final int sides = getBlockSides(x,y,z);
        final float halfSize = blockSize * .5f;

        final Vector3 position = new Vector3(x,y,z).scl(blockSize);
//
        if((sides & ChunkBlockSide.FRONT) != 0) {
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + -halfSize, position.y + halfSize, position.z + halfSize);
            addVertex(position.x + -halfSize, position.y + -halfSize, position.z + halfSize);
            addVertex(position.x + halfSize, position.y + -halfSize, position.z + halfSize);
            addVertex(position.x + halfSize, position.y + halfSize, position.z + halfSize);
        }
        if((sides & ChunkBlockSide.BACK) != 0) {
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x + halfSize, position.y + -halfSize, position.z - halfSize);
            addVertex(position.x + -halfSize, position.y + -halfSize, position.z - halfSize);
            addVertex(position.x + -halfSize, position.y + halfSize, position.z - halfSize);
            addVertex(position.x + halfSize, position.y + halfSize, position.z - halfSize);
        }
        if((sides & ChunkBlockSide.BOTTOM) != 0) {
            addIndex(0, 1, 2, 2, 3, 0);
            addVertex(position.x - halfSize, position.y - halfSize, position.z + halfSize);
            addVertex(position.x - halfSize, position.y - halfSize, position.z - halfSize);
            addVertex(position.x + halfSize, position.y - halfSize, position.z - halfSize);
            addVertex(position.x + halfSize,  position.y - halfSize, position.z + halfSize);
        }
//        // bottom
//        meshBuilder.addMesh(new float[]{
//                -1,  -1, 1,
//                -1,  -1, -1,
//                1,   -1, -1,
//                1,   -1, 1,
//                -1,  -1, 1,
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
//
//        // top
//        meshBuilder.addMesh(new float[]{
//                1,   1, -1,
//                -1,  1, -1,
//                -1,  1, 1,
//                1,   1, 1,
//                1,   1, -1,
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
//
//
//        // left
//        meshBuilder.addMesh(new float[]{
//                -1, 1, -1,
//                -1, -1, -1,
//                -1, -1, 1,
//                -1, 1, 1,
//                -1, 1, -1
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
//
//        // right
//        meshBuilder.addMesh(new float[]{
//                1, -1, 1,
//                1, -1, -1,
//                1, 1, -1,
//                1, 1, 1,
//                1, -1, 1
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
    }

    protected int getBlockSides(int x,int y, int z){
        if(chunkData.get(x,y,z) == 0){
            return 0;
        }
        return ChunkBlockSide.TOP | ChunkBlockSide.BOTTOM
                | ChunkBlockSide.LEFT | ChunkBlockSide.RIGHT
                | ChunkBlockSide.FRONT | ChunkBlockSide.BACK;
    }

    public Model build(ChunkData chunkData)
    {
        this.chunkData = chunkData;
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = builder.part("potato", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position/* | VertexAttributes.Usage.Normal*/, new Material(ColorAttribute.createDiffuse(Color.FOREST)));

        for(int x = 0; x < chunkData.size; x++){
            for(int y = 0;y < chunkData.size; y++){
                for(int z = 0;z < chunkData.size; z++){
                    addBlock(x,y,z);
                }
            }
        }

        addMeshFromBuffers(meshBuilder);
        return builder.end();
    }
}

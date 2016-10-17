package com.brzez.voxelengine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ChunkModelBuilder {
    protected final float[] vertexBuffer;
    protected int vertexBufferPosition = 0;
    protected final short[] indexBuffer;
    protected int indexBufferPosition = 0;

    public ChunkModelBuilder() {
        vertexBuffer = new float[16 * 16 * 16 * 3 * 2]; // no idea how big it should be. Don't care for now.
        indexBuffer  = new short[1337 * 3]; // 8-)
    }

    protected void addVertex(float x, float y, float z){
        vertexBuffer[vertexBufferPosition++] = x;
        vertexBuffer[vertexBufferPosition++] = y;
        vertexBuffer[vertexBufferPosition++] = z;
    }

    protected void addIndex(int... indices){
        for(int i: indices){
            indexBuffer[indexBufferPosition++] = (short)i;
        }
    }

    protected void addMeshFromBuffers(final MeshPartBuilder meshBuilder)
    {
        float[] vertices = new float[vertexBufferPosition];
        short[] indices = new short[indexBufferPosition];

        System.arraycopy(vertexBuffer, 0, vertices, 0, vertexBufferPosition);
        System.arraycopy(indexBuffer, 0, indices, 0, indexBufferPosition);

        meshBuilder.addMesh(vertices, indices);

        vertexBufferPosition = indexBufferPosition = 0;
    }

    public Model build(ChunkData chunkData)
    {
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = builder.part("potato", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position/* | VertexAttributes.Usage.Normal*/, new Material(ColorAttribute.createDiffuse(Color.FOREST)));

        for(int x = 0; x < chunkData.size; x++){
            for(int y = 0;y < chunkData.size; y++){
                for(int z = 0;z < chunkData.size; z++){
                    System.out.println(chunkData.get(x,y,z));
                }
            }
        }

        addVertex(-1, 1, 1);
        addVertex(-1, -1, 1);
        addVertex(1, -1, 1);
        addVertex(1, 1, 1);
        addVertex(-1, 1, 1);
        addIndex(0,1,2,2,3,4);

        addMeshFromBuffers(meshBuilder);
//
//        // front
//        meshBuilder.addMesh(new float[]{
//                -1,1,  1,
//                -1,-1, 1,
//                1,-1,  1,
//                1,1,   1,
//                -1,1,  1
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
//
//        // back
//        meshBuilder.addMesh(new float[]{
//                1,-1,  -1,
//                -1,-1, -1,
//                -1,1,  -1,
//
//                1,1,   -1,
//                1,-1,  -1,
//        }, new short[]{
//                0,1,2,
//                2,3,4
//        });
//
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
        return builder.end();
    }
}

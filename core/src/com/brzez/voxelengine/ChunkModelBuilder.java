package com.brzez.voxelengine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ChunkModelBuilder {
    public Model build(ChunkData chunkData)
    {
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder meshBuilder;
        meshBuilder = builder.part("potato", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position/* | VertexAttributes.Usage.Normal*/, new Material(ColorAttribute.createDiffuse(Color.FOREST)));

        // front
        meshBuilder.addMesh(new float[]{
                -1,1,  1,
                -1,-1, 1,
                1,-1,  1,
                1,1,   1,
                -1,1,  1
        }, new short[]{
                0,1,2,
                2,3,4
        });

        // back
        meshBuilder.addMesh(new float[]{
                1,-1,  -1,
                -1,-1, -1,
                -1,1,  -1,

                1,1,   -1,
                1,-1,  -1,
        }, new short[]{
                0,1,2,
                2,3,4
        });

        // bottom
        meshBuilder.addMesh(new float[]{
                -1,  -1, 1,
                -1,  -1, -1,
                1,   -1, -1,
                1,   -1, 1,
                -1,  -1, 1,
        }, new short[]{
                0,1,2,
                2,3,4
        });

        // top
        meshBuilder.addMesh(new float[]{
                1,   1, -1,
                -1,  1, -1,
                -1,  1, 1,
                1,   1, 1,
                1,   1, -1,
        }, new short[]{
                0,1,2,
                2,3,4
        });


        // left
        meshBuilder.addMesh(new float[]{
                -1, 1, -1,
                -1, -1, -1,
                -1, -1, 1,
                -1, 1, 1,
                -1, 1, -1
        }, new short[]{
                0,1,2,
                2,3,4
        });

        // right
        meshBuilder.addMesh(new float[]{
                1, -1, 1,
                1, -1, -1,
                1, 1, -1,
                1, 1, 1,
                1, -1, 1
        }, new short[]{
                0,1,2,
                2,3,4
        });
        return builder.end();
    }
}

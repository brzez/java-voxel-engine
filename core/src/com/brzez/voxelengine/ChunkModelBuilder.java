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
        meshBuilder = builder.part("potato", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.FOREST)));

        float[] verts   = new float[3000];
        short[] indices = new short[1000];
        for(int i=0;i<verts.length; i++){
            verts[i] = (float)(Math.random() * 2 - 1) * 10f;
        }
        for(int i=0;i<indices.length; i++){
            indices[i] = (short)(Math.random() * indices.length);
        }
        meshBuilder.addMesh(verts, indices);
        return builder.end();
    }
}

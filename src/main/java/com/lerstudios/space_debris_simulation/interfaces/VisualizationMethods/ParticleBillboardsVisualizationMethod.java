package com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.VisualizationDataObject;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationDataParticle;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;

public class ParticleBillboardsVisualizationMethod implements VisualizationMethod {
    // Track all instances
    private static final List<ParticleBillboardsVisualizationMethod> instances = new ArrayList<>();

    // Array of all particle vertices.
    // Any overflow will not currently be rendered.
    //
    // If an object is no longer being rendered, it will still take up space in the array.
    // Perhaps one day we can get rid of unused objects and dynamically stream it. It is not today.
    float[] allObjects;
    int maxObjects = 2_000_000; // Max number of objects that will be stored

    int currentObjectIndex = 0;// Tracks the next index to use for an object

    float[] renderArray; // Smaller array that actually is used for rendering
    int maxParticles = 500_000; // Max number of particles that will be rendered

    // 4 vertices per particle, 3 coordinates per vertex
    int floatsPerParticle = 12;
    float halfParticleSize = 0.2f;
    Color color;

    TriangleMesh mesh;

    public ParticleBillboardsVisualizationMethod(String color, VisualizationGraphics graphics) {
        this.color = Color.web(color);

        allObjects = new float[maxObjects * floatsPerParticle];
        renderArray = new float[maxParticles * floatsPerParticle];

        mesh = new TriangleMesh();

        // Preallocate points
        mesh.getPoints().setAll(new float[maxParticles * floatsPerParticle]);

        // Single dummy tex coord
        mesh.getTexCoords().setAll(0, 0);

        // -------- ADD FACES (THIS IS THE FIX) --------
        int[] faces = new int[maxParticles * 12]; // 2 triangles * 3 vertices * (point + tex)
        int f = 0;

        for (int i = 0; i < maxParticles; i++) {
            int baseVertex = i * 4;

            // Triangle 1: 0, 1, 2
            faces[f++] = baseVertex + 0; faces[f++] = 0;
            faces[f++] = baseVertex + 1; faces[f++] = 0;
            faces[f++] = baseVertex + 2; faces[f++] = 0;

            // Triangle 2: 2, 1, 3
            faces[f++] = baseVertex + 2; faces[f++] = 0;
            faces[f++] = baseVertex + 1; faces[f++] = 0;
            faces[f++] = baseVertex + 3; faces[f++] = 0;
        }

        mesh.getFaces().setAll(faces);
        // --------------------------------------------

        MeshView view = new MeshView(mesh);

        WritableImage redEmissive = new WritableImage(1, 1);
        redEmissive.getPixelWriter().setColor(0, 0, this.color);

        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(this.color);
        redMaterial.setSelfIlluminationMap(redEmissive);

        view.setMaterial(redMaterial);
        view.setCullFace(CullFace.NONE);

        graphics.add3DObjectToGroup(view);

        // Register this instance
        instances.add(this);
    }


    @Override
    public void moveObject(VisualizationDataObject object, double seconds) {
        VisualizationData data = object.getVisualizationData();
        int index = (int) data.getVisualizationData();

        int base = index * floatsPerParticle;

        float x = (float) object.getX();
        float y = (float) object.getY();
        float z = (float) object.getZ();

        // Update all vertices
        allObjects[base + 0] = x - halfParticleSize;  allObjects[base + 1] = y - halfParticleSize;  allObjects[base + 2] = z;
        allObjects[base + 3] = x + halfParticleSize;  allObjects[base + 4] = y - halfParticleSize;  allObjects[base + 5] = z;
        allObjects[base + 6] = x - halfParticleSize;  allObjects[base + 7] = y + halfParticleSize;  allObjects[base + 8] = z;
        allObjects[base + 9] = x + halfParticleSize;  allObjects[base +10] = y + halfParticleSize;  allObjects[base +11] = z;
    }

    @Override
    public VisualizationData setupObject(VisualizationDataObject object, VisualizationGraphics graphics) {
        return new VisualizationDataParticle(allocateIndex());
    }

    public void renderParticles() {
        // Determine how many particles to render
        int count = Math.min(currentObjectIndex, maxParticles);

        // Copy particle positions from allObjects into renderArray
        System.arraycopy(allObjects, 0, renderArray, 0, count * floatsPerParticle);

        // Push the updated positions to the mesh
        mesh.getPoints().setAll(renderArray);
    }

    private int allocateIndex() {
        if (currentObjectIndex >= maxObjects) {
            throw new RuntimeException("Max number of particles exceeded. This is a reminder to improve the particle system.");
        }
        return currentObjectIndex++;
    }

    public static void renderAllParticles() {
        for (ParticleBillboardsVisualizationMethod instance : instances) {
            instance.renderParticles();
        }
    }
}


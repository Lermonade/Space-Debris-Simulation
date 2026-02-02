package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods;

import com.lerstudios.space_debris_simulation.visualizationUtilities.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData.VisualizationData;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData.VisualizationDataNode;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Object3DVisualizationMethod implements VisualizationMethod {
    Color color;
    final PhongMaterial SHARED_MATERIAL;

    public Object3DVisualizationMethod(String color) {
        this.color = Color.web(color);
        this.SHARED_MATERIAL = createSharedMaterial();
    }

    @Override
    public void moveObject(Object object, double seconds) {
        VisualizationData visualizationData = object.getVisualizationData();
        Sphere s = (Sphere) visualizationData.getVisualizationData();
        s.setTranslateX(object.getX());
        s.setTranslateY(object.getY());
        s.setTranslateZ(object.getZ());
    }

    @Override
    public VisualizationData setupObject(Object object, VisualizationGraphics graphics) {
        Sphere sphere = new Sphere(0.4, 4);
        sphere.setMaterial(SHARED_MATERIAL);
        sphere.setTranslateX(object.getX());
        sphere.setTranslateY(object.getY());
        sphere.setTranslateZ(object.getZ());

        graphics.add3DObjectToGroup(sphere);

        return new VisualizationDataNode(sphere);
    }

    private PhongMaterial createSharedMaterial() {
        WritableImage redEmissive = new WritableImage(1, 1);
        redEmissive.getPixelWriter().setColor(0, 0, this.color);

        PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(this.color); // base color
        redMaterial.setSelfIlluminationMap(redEmissive); // glow map
        return redMaterial;
    }
}

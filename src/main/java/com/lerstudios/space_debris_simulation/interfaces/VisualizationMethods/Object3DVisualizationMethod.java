package com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods;

import com.lerstudios.space_debris_simulation.interfaces.Objects.VisualizationDataObject;
import com.lerstudios.space_debris_simulation.modules.visualization.VisualizationGraphics;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationData;
import com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData.VisualizationDataNode;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Object3DVisualizationMethod implements VisualizationMethod {
    Color color;
    final PhongMaterial SHARED_MATERIAL;
    double size;

    public Object3DVisualizationMethod(String color, double size) {
        this.color = Color.web(color);
        this.size = size;
        this.SHARED_MATERIAL = createSharedMaterial();
    }

    @Override
    public void moveObject(VisualizationDataObject object, double seconds) {
        VisualizationData visualizationData = object.getVisualizationData();
        Sphere s = (Sphere) visualizationData.getVisualizationData();
        s.setTranslateX(object.getX());
        s.setTranslateY(object.getY());
        s.setTranslateZ(object.getZ());
    }

    @Override
    public VisualizationData setupObject(VisualizationDataObject object, VisualizationGraphics graphics) {
        Sphere sphere = new Sphere(size, 4);
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

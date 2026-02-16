package com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Visualization_ObjectRegistry {

    private static final Map<String, Object3D> objects = new HashMap<>();

    private Visualization_ObjectRegistry() {} // prevent instantiation

    public static void register(Object3D obj) {
        objects.put(obj.getName(), obj);
    }

    public static Object3D getObjectByName(String name) {
        return objects.get(name);
    }

    public static Collection<Object3D> getAllObjects() {
        return objects.values();
    }

    public static Object3D getFirstObject() {
        return objects.values().stream().findFirst().orElse(null);
    }
}

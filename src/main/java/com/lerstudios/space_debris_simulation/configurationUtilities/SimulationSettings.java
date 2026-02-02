package com.lerstudios.space_debris_simulation.configurationUtilities;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.PopulationObject;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.SimulationConstant;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceTemplate;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods.RemovalMethodTemplate;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SimulationSettings {

    public static List<SimulationConstant> templateConstants = new ArrayList<>();

    static {
        templateConstants.add(SimulationConstant.number("Time Step (s)", 10));
        templateConstants.add(SimulationConstant.number("Total Time Steps", 5000));
        templateConstants.add(SimulationConstant.number("Gravitational Constant", 6.67430e-11));
        templateConstants.add(SimulationConstant.string("Celestial Body Name", "Earth"));
        templateConstants.add(SimulationConstant.number("Celestial Body Radius", 6371000));
        templateConstants.add(SimulationConstant.number("Celestial Body Mass", 5.972e24));
        templateConstants.add(SimulationConstant.vector("Testing Vector", new Vector3(0, 7800, 0)));
    }

    public String simulationName = "Unnamed";
    public String simulationDescription = "No Description";

    public List<SimulationConstant> constants = new ArrayList<>();
    public ArrayList<PopulationObject> populationObjects = new ArrayList<>();

    public ArrayList<SourceTemplate> sources = new ArrayList<>();

    public ArrayList<RemovalMethodTemplate> removalmethods = new ArrayList<>();

    public SimulationSettings() {
        for (SimulationConstant c : templateConstants) {
            constants.add(c.copy());
        }
    }

    public Object getOrDefault(String name, Object defaultValue) {
        for (SimulationConstant constant : constants) {
            if (constant.getName().equals(name)) {
                return constant.getValue();
            }
        }
        return defaultValue;
    }

    public void set(String name, Object value) {
        for (SimulationConstant constant : constants) {
            if (constant.getName().equals(name)) {
                constant.value = value;
                return;
            }
        }

        if (value instanceof Number) {
            constants.add(SimulationConstant.number(name, ((Number) value).doubleValue()));
        } else if (value instanceof String) {
            constants.add(SimulationConstant.string(name, value.toString()));
        } else if (value instanceof Vector3) {
            constants.add(SimulationConstant.vector(name, (Vector3) value));
        }
    }

    public double getNumber(String name, double defaultValue) {
        Object value = getOrDefault(name, defaultValue);
        return value instanceof Number ? ((Number) value).doubleValue() : defaultValue;
    }

    public String getString(String name, String defaultValue) {
        Object value = getOrDefault(name, defaultValue);
        return value != null ? value.toString() : defaultValue;
    }

    public Vector3 getVector(String name, Vector3 defaultValue) {
        Object value = getOrDefault(name, defaultValue);
        return value instanceof Vector3 ? (Vector3) value : defaultValue;
    }

}

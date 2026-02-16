package com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData.SimToVis_PropagationData;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;
import java.util.TreeMap;

public class SimToVis_OrbitalLaser implements SimToVis_RemovalMethod {

    public String name;
    public SimToVis_PropagationData orbitalData;
    public RemovalMethodType removalMethodType = RemovalMethodType.ORBITAL_LASER;
    public String color;
    public String laserColor;
    public Boolean renderLines;
    public PropegationMethod propegationMethod;

    public Map<Double, Vector3> laserTargetPoints;

    public TimedCoordinates endData = null;


    public SimToVis_OrbitalLaser() {}

    public SimToVis_OrbitalLaser(
            String name,
            String color,
            String laserColor,
            Boolean renderLines,
            PropegationMethod propegationMethod,

            SimToVis_PropagationData propagationData,
            Map<Double, Vector3> laserTargetPoints

    ) {
        this.name = name;
        this.color = color;
        this.laserColor = laserColor;
        this.renderLines = renderLines;
        this.propegationMethod = propegationMethod;

        this.orbitalData = propagationData;
        this.laserTargetPoints = laserTargetPoints;
    }

    @Override
    public RemovalMethodType getRemovalMethodType() {
        return RemovalMethodType.ORBITAL_LASER;
    }

    @JsonIgnore
    public SimToVis_PropagationData getPropagationData() {
        return orbitalData;
    }

    @JsonIgnore
    public PropegationMethod getPropagationMethod() {
        return this.propegationMethod;
    }
}

package com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.SimplifiedVelocityVerletPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.types.ObjectClassification;
import com.lerstudios.space_debris_simulation.types.PropegationMethod;
import com.lerstudios.space_debris_simulation.types.RadianKeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.utils.PropagationTools;
import org.fxyz3d.geometry.Point3D;

public class Simulation_SimplifiedVelocityVerletObject extends Simulation_ObjectBase {
    public Simulation_SimplifiedVelocityVerletObject(String name, ObjectClassification classification, PropagationMethod propagator, RadianKeplerianElements initElements) {
        super(name, classification, PropegationMethod.SIMPLIFIED_VELOCITY_VERLET);

        Point3D initPos = PropagationTools.propagateOrbit(0, 0,0, initElements.semiMajorAxis(), initElements.eccentricity(), initElements.argPeriapsis(), initElements.inclination(), initElements.raan(), initElements.anomaly(), 0, Constants.gravitationalConstant * Constants.earthMass, true);
        this.setPosition(initPos.getX(), initPos.getY(), initPos.getZ());

        javafx.geometry.Point3D initVel = PropagationTools.propagateOrbitVelocity(0, 0,0, initElements.semiMajorAxis(), initElements.eccentricity(), initElements.argPeriapsis(), initElements.inclination(), initElements.raan(), initElements.anomaly(), 0, Constants.gravitationalConstant * Constants.earthMass, true);
        this.setVelocity(initVel.getX(), initVel.getY(), initVel.getZ());

        SimplifiedVelocityVerletPropagationData data = new SimplifiedVelocityVerletPropagationData();
        this.setPropagationData(propagator.setupObject(this, data));

    }
}

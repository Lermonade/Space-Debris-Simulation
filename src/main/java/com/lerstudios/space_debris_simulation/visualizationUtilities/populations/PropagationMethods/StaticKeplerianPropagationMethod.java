package com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods;

import com.lerstudios.space_debris_simulation.simulation.exportFormats.objects.VisualizationObject;
import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.visualizationUtilities.PropagationTools;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.Object;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData.StaticKeplerianPropagationData;
import org.fxyz3d.geometry.Point3D;

public class StaticKeplerianPropagationMethod implements PropagationMethod {
    @Override
    public void getPositionFromTime(Object object, double seconds) {

        StaticKeplerianPropagationData data = (StaticKeplerianPropagationData) object.getPropagationData();
        KeplerianElements elements = (KeplerianElements) data.getPropagationData();

        Point3D pos = PropagationTools.propagateOrbit(0, 0, 0,
                elements.semiMajorAxis(), elements.eccentricity(), elements.argPeriapsis(), elements.inclination(), elements.raan(),
                elements.anomaly(), seconds, Constants.gravitationalConstant * Constants.earthMass, true);

        double x = (pos.getX() * Constants.scaleFactor);
        double y = (pos.getY() * Constants.scaleFactor);
        double z = (pos.getZ() * Constants.scaleFactor);

        object.setPosition(x, y, z);
    }

    @Override
    public PropagationData setupObject(Object object, VisualizationObject initObject) {
        KeplerianElements elements = (KeplerianElements) initObject.getOrbitData();
        return new StaticKeplerianPropagationData(elements);
    }
}

package com.lerstudios.space_debris_simulation.modules.simulation.removalMethods;

import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_ObjectBase;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.controllers.OrbitalLaserController;
import com.lerstudios.space_debris_simulation.types.*;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.Map;

public class Simulation_OrbitalLaser extends Simulation_RemovalMethodBase {

    public String laserPowerWatts;          // W
    public String wavelengthNanometers;     // nm
    public String apertureDiameterMeters;   // m
    public String beamQualityM2;            // unitless
    public String opticalEfficiencyPercent; // %

    public String pulseRateHz;               // Hz
    public String pulseDurationPicoseconds;  // ps

    public String maxEngagementTimeSeconds;  // s
    public String cooldownTimeSeconds;       // s
    public String slewRateDegPerSec;         // deg/s
    public String targetingRangeMeters;      // m

    public String laserColor;

    public String collisionRadiusMeters;  // m

    private OrbitalLaserController controller;

    public Simulation_OrbitalLaser(
            String name,

            String laserPowerWatts,
            String wavelengthNanometers,
            String apertureDiameterMeters,
            String beamQualityM2,
            String opticalEfficiencyPercent,

            String pulseRateHz,
            String pulseDurationPicoseconds,

            String maxEngagementTimeSeconds,
            String cooldownTimeSeconds,
            String slewRateDegPerSec,
            String targetingRangeMeters,

            String collisionRadiusMeters,

            String color,
            String laserColor,
            String drawLines,

            PropegationMethod method,
            PropagationMethod propagator
    ) {
        super(name, ObjectClassification.REMOVAL_METHOD, method, propagator, color, drawLines, RemovalMethodType.ORBITAL_LASER);

        this.laserPowerWatts = laserPowerWatts;
        this.wavelengthNanometers = wavelengthNanometers;
        this.apertureDiameterMeters = apertureDiameterMeters;
        this.beamQualityM2 = beamQualityM2;
        this.opticalEfficiencyPercent = opticalEfficiencyPercent;

        this.pulseRateHz = pulseRateHz;
        this.pulseDurationPicoseconds = pulseDurationPicoseconds;

        this.maxEngagementTimeSeconds = maxEngagementTimeSeconds;
        this.cooldownTimeSeconds = cooldownTimeSeconds;
        this.slewRateDegPerSec = slewRateDegPerSec;
        this.targetingRangeMeters = targetingRangeMeters;

        this.collisionRadiusMeters = collisionRadiusMeters;

        this.color = color;
        this.laserColor = laserColor;

        controller = new OrbitalLaserController(this);
    }

    public Map<Double, Vector3> getLaserTargetData() {
        return this.controller.getTargetData();
    }

    public void propagate(double timestep) {
        super.propagate(timestep);

        if(this.propagating) {
            this.controller.update(timestep);
        }
    }
}


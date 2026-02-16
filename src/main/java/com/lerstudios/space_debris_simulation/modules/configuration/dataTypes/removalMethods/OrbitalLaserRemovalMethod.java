package com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods;

import com.lerstudios.space_debris_simulation.types.KeplerianElements;
import com.lerstudios.space_debris_simulation.types.RemovalMethodType;

public class OrbitalLaserRemovalMethod implements RemovalMethodTemplate {

    // ----------------------
    // Laser parameters
    // ----------------------
    public String name;

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

    // ----------------------
    // Orbital elements + mass (as strings for UI)
    // ----------------------
    public String semiMajorAxisMeters;                   // m
    public String eccentricity;                           // unitless
    public String inclinationDegrees;                    // °
    public String rightAscensionOfAscendingNodeDegrees; // ° (RAAN)
    public String argumentOfPeriapsisDegrees;           // °
    public String trueAnomalyDegrees;                    // °
    public String massKg;                                // kg

    // ----------------------
    // Keplerian elements record
    // ----------------------
    public KeplerianElements orbitalData;

    // ----------------------
    // Collision radius
    // ----------------------
    public String collisionRadiusMeters;  // m

    // ----------------------
    // Display properties
    // ----------------------
    public String color;         // general UI color
    public String laserColor;    // color of the laser beam
    public String drawLines;     // "true" or "false" to draw lines

    // ----------------------
    // Propagation method
    // ----------------------
    public String propagationMethod; // e.g., "Kepler", "Numerical", etc.

    public OrbitalLaserRemovalMethod() {}

    public OrbitalLaserRemovalMethod(
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

            String semiMajorAxisMeters,
            String eccentricity,
            String inclinationDegrees,
            String rightAscensionOfAscendingNodeDegrees,
            String argumentOfPeriapsisDegrees,
            String trueAnomalyDegrees,
            String massKg,

            String collisionRadiusMeters,

            String color,
            String laserColor,
            String drawLines,

            String propagationMethod
    ) {
        this.name = name;

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

        // Orbital strings
        this.semiMajorAxisMeters = semiMajorAxisMeters;
        this.eccentricity = eccentricity;
        this.inclinationDegrees = inclinationDegrees;
        this.rightAscensionOfAscendingNodeDegrees = rightAscensionOfAscendingNodeDegrees;
        this.argumentOfPeriapsisDegrees = argumentOfPeriapsisDegrees;
        this.trueAnomalyDegrees = trueAnomalyDegrees;
        this.massKg = massKg;

        this.collisionRadiusMeters = collisionRadiusMeters;

        // Display strings
        this.color = color;
        this.laserColor = laserColor;
        this.drawLines = drawLines;

        // Propagation
        this.propagationMethod = propagationMethod;

        // Build KeplerianElements from strings
        double a = parseDoubleOrZero(semiMajorAxisMeters);
        double e = parseDoubleOrZero(eccentricity);
        double i = parseDoubleOrZero(inclinationDegrees);
        double raan = parseDoubleOrZero(rightAscensionOfAscendingNodeDegrees);
        double argPeri = parseDoubleOrZero(argumentOfPeriapsisDegrees);
        double anomaly = parseDoubleOrZero(trueAnomalyDegrees);
        double mass = parseDoubleOrZero(massKg);

        this.orbitalData = new KeplerianElements(a, e, i, raan, argPeri, anomaly, mass);
    }

    private static double parseDoubleOrZero(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    @Override
    public RemovalMethodType getRemovalMethodType() {
        return RemovalMethodType.ORBITAL_LASER;
    }

    public void rebuildOrbitalData() {

        double a = parseDoubleOrZero(semiMajorAxisMeters);
        double e = parseDoubleOrZero(eccentricity);
        double i = parseDoubleOrZero(inclinationDegrees);                   // keep in degrees
        double raan = parseDoubleOrZero(rightAscensionOfAscendingNodeDegrees); // keep in degrees
        double argPeri = parseDoubleOrZero(argumentOfPeriapsisDegrees);      // keep in degrees
        double anomaly = parseDoubleOrZero(trueAnomalyDegrees);             // keep in degrees
        double mass = parseDoubleOrZero(massKg);

        this.orbitalData = new KeplerianElements(
                a, e, i, raan, argPeri, anomaly, mass
        );
    }

}


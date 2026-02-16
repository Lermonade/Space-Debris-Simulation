package com.lerstudios.space_debris_simulation.types;

public record RadianKeplerianElements(
        double semiMajorAxis,
        double eccentricity,
        double inclination,
        double raan,
        double argPeriapsis,
        double anomaly,
        double mass
) {

    public static RadianKeplerianElements fromDegrees(KeplerianElements e) {
        return new RadianKeplerianElements(
                e.semiMajorAxis(),
                e.eccentricity(),
                Math.toRadians(e.inclination()),
                Math.toRadians(e.raan()),
                Math.toRadians(e.argPeriapsis()),
                Math.toRadians(e.anomaly()),
                e.mass()
        );
    }
}


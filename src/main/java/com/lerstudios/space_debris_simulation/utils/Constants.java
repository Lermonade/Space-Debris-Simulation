package com.lerstudios.space_debris_simulation.utils;

import com.lerstudios.space_debris_simulation.Console;

public class Constants {

    // visualization units / meters, multiply physics value by this to get visualization position.
    // based on radius of earth
    public static String appName = "SDARS";
    public static double bodyRadius = 6371000;
    public static double scaleFactor = (double) 100 / bodyRadius;
    public static double earthMass = 5.972e24;
    public static double gravitationalConstant = 6.67430e-11;
    public static Console console;

    public static void setBodyRadius(double radius) {
        Constants.bodyRadius = radius;
        Constants.scaleFactor = (double) 100 / bodyRadius;
    }

    public static void setEarthMass(double mass) {
        Constants.earthMass = mass;
    }

    public static void setGravitationalConstant(double gravity) {
        Constants.gravitationalConstant = gravity;
    }
}

package com.lerstudios.space_debris_simulation.modules.simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class SimUtils {
    private static final AtomicInteger id = new AtomicInteger(0);

    private static double endTimeStep = 0;
    private static double currentTimeStep = 0;
    private static double secondsPerTimeStep = 0;

    private static boolean createVisualizationFile = true;

    public static int getNextID() {
        return id.getAndIncrement();
    }

    public static int getCurrentID() {
        return id.get();
    }

    public static void resetID() {
        id.set(0);
    }

    public static double getEndTimeStep() {
        return endTimeStep;
    }

    public static void setEndTimeStep(double endTimeStep) {
        SimUtils.endTimeStep = endTimeStep;
    }

    public static double getCurrentTimeStep() {
        return currentTimeStep;
    }

    public static double getSecondsPerTimeStep() {
        return secondsPerTimeStep;
    }

    public static boolean getCreateVisualizationFile() {
        return createVisualizationFile;
    }

    public static void setCurrentTimeStep(double currentTimeStep) {
        SimUtils.currentTimeStep = currentTimeStep;
    }

    public static void setSecondsPerTimeStep(double seconds) {
        SimUtils.secondsPerTimeStep = seconds;
    }
}

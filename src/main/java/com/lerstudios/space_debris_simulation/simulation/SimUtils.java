package com.lerstudios.space_debris_simulation.simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class SimUtils {
    private static final AtomicInteger id = new AtomicInteger(0);

    private static double endTimeStep = 0;
    private static double currentTimeStep = 0;

    public static int getNextID() {
        return id.getAndIncrement();
    }

    public static int getCurrentID() {
        return id.get();
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

    public static void setCurrentTimeStep(double currentTimeStep) {
        SimUtils.currentTimeStep = currentTimeStep;
    }
}

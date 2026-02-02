package com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource;

import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.simulation.types.SourceType;
import com.lerstudios.space_debris_simulation.simulation.types.KeplerianElements;

import java.util.Random;

public class KeplerianDistribution implements OrbitDataSource {
    String name;
    KeplerianDistributionSource source;

    private static final Random RANDOM = new Random();

    public KeplerianDistribution() {

    }

    public KeplerianDistribution(String name, KeplerianDistributionSource source) {
        this.name = name;
        this.source = source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SourceType getType() {
        return SourceType.KEPLERIAN;
    }

    public static int stringToInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static double boundedNormal(double min, double mean, double max) {
        if (min >= max) return min;

        double stdDev = (max - min) / 6.0;
        double value;

        do {
            value = mean + RANDOM.nextGaussian() * stdDev;
        } while (value < min || value > max);

        return value;
    }

    private static double stringToDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double randomFromStrings(
            String minStr,
            String avgStr,
            String maxStr,
            double defaultValue
    ) {
        double min = stringToDouble(minStr, defaultValue);
        double avg = stringToDouble(avgStr, defaultValue);
        double max = stringToDouble(maxStr, defaultValue);

        return boundedNormal(min, avg, max);
    }

    public KeplerianElements generateElements() {

        return new KeplerianElements(
                randomFromStrings(
                        source.semiMajorAxisMin,
                        source.semiMajorAxisAvg,
                        source.semiMajorAxisMax,
                        0
                ),
                randomFromStrings(
                        source.eccentricityMin,
                        source.eccentricityAvg,
                        source.eccentricityMax,
                        0
                ),
                randomFromStrings(
                        source.inclinationMin,
                        source.inclinationAvg,
                        source.inclinationMax,
                        0
                ),
                randomFromStrings(
                        source.raanMin,
                        source.raanAvg,
                        source.raanMax,
                        0
                ),
                randomFromStrings(
                        source.argPeriapsisMin,
                        source.argPeriapsisAvg,
                        source.argPeriapsisMax,
                        0
                ),
                randomFromStrings(
                        source.anomalyMin,
                        source.anomalyAvg,
                        source.anomalyMax,
                        0
                ),
                randomFromStrings(
                        source.massMin,
                        source.massAvg,
                        source.massMax,
                        0
                )
        );
    }


}

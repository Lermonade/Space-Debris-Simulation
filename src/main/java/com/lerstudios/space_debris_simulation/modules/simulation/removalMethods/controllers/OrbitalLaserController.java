package com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.controllers;

import com.lerstudios.space_debris_simulation.interfaces.Objects.Object3D;
import com.lerstudios.space_debris_simulation.modules.simulation.SimUtils;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.Simulation_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_ObjectBase;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_OrbitalLaser;
import com.lerstudios.space_debris_simulation.types.TimedCoordinates;
import com.lerstudios.space_debris_simulation.utils.Vector3;

import java.util.*;

public class OrbitalLaserController {
    Simulation_ObjectBase targetObject = null;
    Simulation_OrbitalLaser parent;

    private double maxTargetVelocityAngle = 60;
    private double maxTargetDistanceMeters = 700_000;
    private double ablationConstant;

    private Map<Double, Vector3> laserTargetPoints = new TreeMap<>();

    private int retargetCooldown = 12; // Number of timesteps to wait before retargeting
    private int timeSinceLastRetarget = 0; // Tracks time since last retarget


    public OrbitalLaserController(Simulation_OrbitalLaser parent) {
        this.parent = parent;

        ablationConstant = precomputeAblationConstant(
                parent,
                0.3,        // absorptionEfficiency (30%)
                1.0e7,      // Qstar (J/kg)
                3000.0      // ejectaVelocity (m/s)
        );
    }

    public void update(double timestep) {
        // Only retarget if the cooldown period has passed
        if (timeSinceLastRetarget >= retargetCooldown) {
            // Check if the current target is valid
            if (this.targetObject == null || !targetObject.isPropagating() ||
                    !isPointingToward(targetObject.getPosition(), targetObject.getVelocity(), parent.getPosition(), maxTargetVelocityAngle) ||
                    !isPointWithinRadius(parent.getPosition(), targetObject.getPosition(), maxTargetDistanceMeters)) {
                this.targetObject = findSuitableTarget(); // Retarget
            }

            // Reset the cooldown counter after retargeting
            if (this.targetObject != null) {
                timeSinceLastRetarget = 0;
            }
        } else {
            // Increment the time counter while waiting for cooldown
            timeSinceLastRetarget++;
        }

        // If there's no valid target, log null for visualization
        if (this.targetObject == null) {
            if (SimUtils.getCreateVisualizationFile()) {
                laserTargetPoints.put(SimUtils.getCurrentTimeStep() * SimUtils.getSecondsPerTimeStep(), null);
            }
            return;
        }

        // Visualization of target position
        if (SimUtils.getCreateVisualizationFile()) {
            Vector3 pos = targetObject.getPosition();
            laserTargetPoints.put(
                    SimUtils.getCurrentTimeStep() * SimUtils.getSecondsPerTimeStep(),
                    new Vector3(pos.x, pos.y, pos.z)
            );
        }

        // Apply laser force to target
        applyForce();
    }


    public double precomputeAblationConstant(
            Simulation_OrbitalLaser parent,
            double absorptionEfficiency,   // η_abs (0–1)
            double Qstar,                  // J/kg
            double ejectaVelocity          // m/s
    ) {

        // Parse hardware values
        double laserPower = Double.parseDouble(parent.laserPowerWatts);             // W
        double apertureDiameter = Double.parseDouble(parent.apertureDiameterMeters); // m
        double pulseRateHz = Double.parseDouble(parent.pulseRateHz);                // Hz
        double pulseDurationPs = Double.parseDouble(parent.pulseDurationPicoseconds); // ps
        double opticalEfficiencyPercent = Double.parseDouble(parent.opticalEfficiencyPercent);

        // Unit conversions
        double pulseDurationSeconds = pulseDurationPs * 1e-12; // ps → s
        double pEffective = laserPower * (opticalEfficiencyPercent / 100.0); // effective W

        // Spot area (assume circular aperture)
        double beamArea = Math.PI * Math.pow(apertureDiameter / 2.0, 2.0); // m²

        // Energy per pulse
        double energyPerPulse = pEffective / pulseRateHz; // J per pulse

        // Fluence on target
        double fluence = energyPerPulse / beamArea; // J/m² per pulse

        // Momentum coupling coefficient (small debris, rough value)
        double Cm = absorptionEfficiency * ejectaVelocity / Qstar; // N·s per J/m²

        // Collapse into a per-second "force constant"
        double ablationConstant = fluence * Cm * pulseRateHz; // N
        return ablationConstant;
    }


    private void applyForce() {
        Vector3 laserPos = parent.getPosition();
        Vector3 targetPos = targetObject.getPosition();
        double mass = targetObject.getMass();

        // 1️⃣ Compute vector to target
        double dx = targetPos.x - laserPos.x;
        double dy = targetPos.y - laserPos.y;
        double dz = targetPos.z - laserPos.z;
        double r = Math.sqrt(dx*dx + dy*dy + dz*dz); // distance to target

        // 2️⃣ Beam radius at distance r (diffraction-limited)
        double wavelengthMeters = Double.parseDouble(parent.wavelengthNanometers) * 1e-9;
        double apertureDiameter = Double.parseDouble(parent.apertureDiameterMeters);
        double beamQualityM2 = Double.parseDouble(parent.beamQualityM2);

        double theta = beamQualityM2 * wavelengthMeters / (Math.PI * apertureDiameter);
        double w = theta * r / 2; // beam radius at target
        double beamAreaAtTarget = Math.PI * w * w;

        // 3️⃣ Fraction of laser hitting target
        double targetArea = 0.0001; // 1 cm² for small debris
        double fraction = targetArea / beamAreaAtTarget;
        fraction = Math.min(fraction, 1.0); // clamp fraction to 1

        // 4️⃣ Scale ablation force by fraction
        double force = ablationConstant * fraction;

        // 5️⃣ Time-step scaling
        double timedForce = force * SimUtils.getSecondsPerTimeStep();

        // 6️⃣ Normalize direction vector
        Vector3 dir = new Vector3(dx, dy, dz);
        double mag = Math.sqrt(dir.x*dir.x + dir.y*dir.y + dir.z*dir.z);
        if (mag > 0) {
            dir.x /= mag;
            dir.y /= mag;
            dir.z /= mag;
        }

        // 7️⃣ Apply acceleration
        Vector3 additionalAccel = new Vector3(
                dir.x * timedForce / mass,
                dir.y * timedForce / mass,
                dir.z * timedForce / mass
        );

        targetObject.addAcceleration(additionalAccel);
        targetObject.setLastTargeter(this.parent);
    }



    private Simulation_ObjectBase findSuitableTarget() {

        Simulation_ObjectBase closest = null;
        double minDistSq = Double.MAX_VALUE;

        Vector3 parentPos = parent.getPosition();
        double maxDistSqAllowed = maxTargetDistanceMeters * maxTargetDistanceMeters;

        for (Simulation_ObjectBase object : Simulation_Population.allDebrisObjects) {

            if (!object.isPropagating()) {
                continue;
            }

            Vector3 objPos = object.getPosition();

            double dx = objPos.x - parentPos.x;
            double dy = objPos.y - parentPos.y;
            double dz = objPos.z - parentPos.z;

            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq > maxDistSqAllowed) {
                continue;
            }

            if (!isPointingToward(
                    object.getPosition(),
                    object.getVelocity(),
                    parentPos,
                    maxTargetVelocityAngle
            )) {
                continue;
            }

            // Pick the **closest** object
            if (distSq < minDistSq) {
                minDistSq = distSq;
                closest = object;
            }
        }

        return closest;
    }


    public static boolean isPointWithinRadius(
            Vector3 center,
            Vector3 point,
            double radius
    ) {
        double dx = point.x - center.x;
        double dy = point.y - center.y;
        double dz = point.z - center.z;

        double distanceSquared = dx * dx + dy * dy + dz * dz;
        double radiusSquared = radius * radius;

        return distanceSquared <= radiusSquared;
    }


    public static boolean isPointingToward(
            Vector3 origin,
            Vector3 direction,
            Vector3 target,
            double maxAngleDegrees
    ) {
        // Vector from origin to target
        double tx = target.x - origin.x;
        double ty = target.y - origin.y;
        double tz = target.z - origin.z;

        // Dot product
        double dot =
                direction.x * tx +
                        direction.y * ty +
                        direction.z * tz;

        // Magnitudes
        double dirMag = Math.sqrt(
                direction.x * direction.x +
                        direction.y * direction.y +
                        direction.z * direction.z
        );

        double targetMag = Math.sqrt(
                tx * tx +
                        ty * ty +
                        tz * tz
        );

        if (dirMag == 0 || targetMag == 0) {
            return false;
        }

        // cos(theta) = dot / (|a||b|)
        double cosTheta = dot / (dirMag * targetMag);

        // Clamp for numerical safety
        cosTheta = Math.max(-1.0, Math.min(1.0, cosTheta));

        double angleDegrees = Math.toDegrees(Math.acos(cosTheta));

        return angleDegrees <= maxAngleDegrees;
    }

    public Map<Double, Vector3> getTargetData() {
        return this.laserTargetPoints;
    }

}

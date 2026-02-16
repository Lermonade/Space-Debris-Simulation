package com.lerstudios.space_debris_simulation.utils;

public class Vector3 {
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;

    public Vector3() {}

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(
                this.x + other.x,
                this.y + other.y,
                this.z + other.z
        );
    }

    public void addInPlace(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

}

package com.lerstudios.space_debris_simulation.modules.configuration.dataTypes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lerstudios.space_debris_simulation.utils.Vector3;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class SimulationConstant {
    public String name;

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    public Object value;

    public ConstantType type;

    public enum ConstantType { NUMBER, VECTOR, STRING }

    public SimulationConstant() {}

    private SimulationConstant(String name, Object value, ConstantType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static SimulationConstant number(String name, double value) {
        return new SimulationConstant(name, value, ConstantType.NUMBER);
    }

    public static SimulationConstant vector(String name, Vector3 value) {
        return new SimulationConstant(name, value, ConstantType.VECTOR);
    }

    public static SimulationConstant string(String name, String value) {
        return new SimulationConstant(name, value, ConstantType.STRING);
    }

    @JsonIgnore
    public boolean isNumber() { return type == ConstantType.NUMBER; }

    @JsonIgnore
    public boolean isVector() { return type == ConstantType.VECTOR; }

    @JsonIgnore
    public boolean isString() { return type == ConstantType.STRING; }

    public String getName() { return name; }
    public Object getValue() { return value; }

    public SimulationConstant copy() {
        switch (type) {
            case NUMBER:
                return SimulationConstant.number(name, (Double) value);
            case STRING:
                return SimulationConstant.string(name, (String) value);
            case VECTOR:
                Vector3 v = (Vector3) value;
                return SimulationConstant.vector(name, new Vector3(v.x, v.y, v.z));
            default:
                throw new IllegalStateException("Unknown constant type: " + type);
        }
    }
}

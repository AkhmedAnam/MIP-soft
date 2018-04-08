package ru.nsi.mipsoft.model.api;

import java.util.Objects;

public class Vector3D<NumberType extends Number & Comparable<NumberType> > {

    private final NumberType x_coordinate;
    private final NumberType y_coordinate;
    private final NumberType z_coordinate;

    public Vector3D(NumberType x_coordinate, NumberType y_coordinate, NumberType z_coordinate) {
        this.x_coordinate = x_coordinate;
        this.y_coordinate = y_coordinate;
        this.z_coordinate = z_coordinate;
    }

    public NumberType getX_coordinate() {
        return x_coordinate;
    }

    public NumberType getY_coordinate() {
        return y_coordinate;
    }

    public NumberType getZ_coordinate() {
        return z_coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3D<?> vector3D = (Vector3D<?>) o;

        if (x_coordinate != null ? !x_coordinate.equals(vector3D.x_coordinate) : vector3D.x_coordinate != null)
            return false;
        if (y_coordinate != null ? !y_coordinate.equals(vector3D.y_coordinate) : vector3D.y_coordinate != null)
            return false;
        return z_coordinate != null ? z_coordinate.equals(vector3D.z_coordinate) : vector3D.z_coordinate == null;
    }

    @Override
    public int hashCode() {
        int result = x_coordinate != null ? x_coordinate.hashCode() : 0;
        result = 31 * result + (y_coordinate != null ? y_coordinate.hashCode() : 0);
        result = 31 * result + (z_coordinate != null ? z_coordinate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "{%s, %s, %s}",
                x_coordinate.toString(),
                y_coordinate.toString(),
                z_coordinate.toString()
        );
    }
}

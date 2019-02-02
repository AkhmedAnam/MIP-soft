package ru.nsi.mipsoft.model.api;

public class ImagePosition {

    public ImagePosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    private final float x;
    private final float y;
    private final float z;
}

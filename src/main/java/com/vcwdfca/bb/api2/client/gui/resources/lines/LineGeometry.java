package com.vcwdfca.bb.api2.client.gui.resources.lines;

/**
 * Describes a dependency line in local drawing coordinates.
 */
public final class LineGeometry {
    private final float startX;
    private final float startY;
    private final float endX;
    private final float endY;

    public LineGeometry(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public float getLength() {
        float dx = endX - startX;
        float dy = endY - startY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float getAngleRadians() {
        return (float) Math.atan2(endY - startY, endX - startX);
    }
}

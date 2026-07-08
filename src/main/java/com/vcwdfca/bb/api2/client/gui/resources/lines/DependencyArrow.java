package com.vcwdfca.bb.api2.client.gui.resources.lines;

/**
 * One arrow marker positioned on a dependency line.
 */
public final class DependencyArrow {
    private final float progress;
    private final float centerX;
    private final float width;
    private final float size;
    private final float opacity;

    public DependencyArrow(float progress, float centerX, float width, float size, float opacity) {
        this.progress = progress;
        this.centerX = centerX;
        this.width = width;
        this.size = size;
        this.opacity = opacity;
    }

    public float getProgress() {
        return progress;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getWidth() {
        return width;
    }

    public float getSize() {
        return size;
    }

    public float getOpacity() {
        return opacity;
    }
}

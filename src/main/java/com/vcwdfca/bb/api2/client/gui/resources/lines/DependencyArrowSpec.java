package com.vcwdfca.bb.api2.client.gui.resources.lines;

/**
 * Configures the size and opacity of dependency arrows.
 */
public final class DependencyArrowSpec {
    public static final float DEFAULT_ARROW_WIDTH = 0.5F;
    public static final float DEFAULT_ARROW_SIZE = 0.75F;
    public static final float DEFAULT_ARROW_OPACITY = 0.2F;

    private final float arrowWidthBase;
    private final float arrowSizeBase;
    private final float arrowOpacity;

    public DependencyArrowSpec(float arrowWidthBase, float arrowSizeBase, float arrowOpacity) {
        if (arrowWidthBase <= 0F) {
            throw new IllegalArgumentException("arrowWidthBase must be positive");
        }
        if (arrowSizeBase <= 0F) {
            throw new IllegalArgumentException("arrowSizeBase must be positive");
        }
        if (arrowOpacity < 0F || arrowOpacity > 1F) {
            throw new IllegalArgumentException("arrowOpacity must be between 0 and 1");
        }
        this.arrowWidthBase = arrowWidthBase;
        this.arrowSizeBase = arrowSizeBase;
        this.arrowOpacity = arrowOpacity;
    }

    public float getArrowWidthBase() {
        return arrowWidthBase;
    }

    public float getArrowSizeBase() {
        return arrowSizeBase;
    }

    public float getArrowOpacity() {
        return arrowOpacity;
    }
}

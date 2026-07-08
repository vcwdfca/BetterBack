package com.vcwdfca.bb.api2.client.gui.resources.lines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Default directional arrow layout.
 */
public class DependencyArrowLayoutImpl implements DependencyArrowLayout {

    @Override
    public List<DependencyArrow> buildArrows(
            LineGeometry geometry,
            DependencyArrowSpec spec,
            int lineWidth,
            boolean showArrows,
            boolean animate,
            long timeMillis) {
        Objects.requireNonNull(geometry, "geometry");
        Objects.requireNonNull(spec, "spec");

        if (!showArrows) {
            return Collections.emptyList();
        }
        if (lineWidth <= 0) {
            throw new IllegalArgumentException("lineWidth must be positive");
        }

        float length = geometry.getLength();
        if (length <= 0F) {
            return Collections.emptyList();
        }

        int arrowSegments = (int) Math.ceil(length / 20F);
        float progressOffset = arrowSegments % 2 == 1 ? 0F : (1F / (arrowSegments + 1)) / 2F;
        float animationOffset = animate ? getAnimationOffset(length, timeMillis) : 0F;
        float arrowWidth = spec.getArrowWidthBase() * lineWidth;
        float arrowSize = spec.getArrowSizeBase() * lineWidth;

        List<DependencyArrow> arrows = new ArrayList<>(arrowSegments + 1);
        for (int i = 0; i <= arrowSegments; i++) {
            float progress = (float) i / (arrowSegments + 1) + progressOffset + animationOffset;
            progress = progress - (float) Math.floor(progress);
            arrows.add(new DependencyArrow(
                    progress,
                    length * progress,
                    arrowWidth,
                    arrowSize,
                    spec.getArrowOpacity()));
        }
        return arrows;
    }

    private float getAnimationOffset(float length, long timeMillis) {
        long period = Math.max(1L, (long) (length * 50D));
        long wrappedTime = Math.floorMod(timeMillis, period);
        return wrappedTime / (float) period;
    }
}

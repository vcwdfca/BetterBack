package com.vcwdfca.bb.api2.client.gui.resources.lines;

import java.util.List;

/**
 * Builds directional arrow markers for dependency lines.
 */
public interface DependencyArrowLayout {

    /**
     * Creates arrow marker positions for one line.
     *
     * @param geometry line geometry in local drawing coordinates
     * @param spec arrow size and opacity configuration
     * @param lineWidth rendered dependency line width
     * @param showArrows whether arrows are enabled
     * @param animate whether arrows should move this frame
     * @param timeMillis current animation time
     * @return ordered arrow markers from prerequisite to dependant
     */
    List<DependencyArrow> buildArrows(
            LineGeometry geometry,
            DependencyArrowSpec spec,
            int lineWidth,
            boolean showArrows,
            boolean animate,
            long timeMillis);
}

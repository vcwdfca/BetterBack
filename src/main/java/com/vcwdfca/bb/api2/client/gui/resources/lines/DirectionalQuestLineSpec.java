package com.vcwdfca.bb.api2.client.gui.resources.lines;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * Describes one quest line entry that should use a directional line.
 */
public final class DirectionalQuestLineSpec {
    private final ResourceLocation lineId;
    private final DependencyArrowSpec arrowSpec;

    public DirectionalQuestLineSpec(ResourceLocation lineId, DependencyArrowSpec arrowSpec) {
        this.lineId = Objects.requireNonNull(lineId, "lineId");
        this.arrowSpec = Objects.requireNonNull(arrowSpec, "arrowSpec");
    }

    public ResourceLocation getLineId() {
        return lineId;
    }

    public DependencyArrowSpec getArrowSpec() {
        return arrowSpec;
    }
}

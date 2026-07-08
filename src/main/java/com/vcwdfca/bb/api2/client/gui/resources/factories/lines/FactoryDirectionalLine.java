package com.vcwdfca.bb.api2.client.gui.resources.factories.lines;

import betterquesting.api.utils.JsonHelper;
import betterquesting.api2.client.gui.resources.lines.IGuiLine;
import com.google.gson.JsonObject;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DependencyArrowSpec;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalLine;
import net.minecraft.util.ResourceLocation;

/**
 * Theme factory for BetterBack directional dependency lines.
 */
public final class FactoryDirectionalLine {
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation("betterquesting", "line_directional");

    private FactoryDirectionalLine() {
    }

    public static IGuiLine loadFromData(JsonObject data) {
        DependencyArrowSpec spec = new DependencyArrowSpec(
                JsonHelper.GetNumber(data, "arrowWidth", DependencyArrowSpec.DEFAULT_ARROW_WIDTH).floatValue(),
                JsonHelper.GetNumber(data, "arrowSize", DependencyArrowSpec.DEFAULT_ARROW_SIZE).floatValue(),
                JsonHelper.GetNumber(data, "arrowOpacity", DependencyArrowSpec.DEFAULT_ARROW_OPACITY).floatValue());
        return new DirectionalLine(spec);
    }

    public static IGuiLine createNew() {
        return new DirectionalLine();
    }
}

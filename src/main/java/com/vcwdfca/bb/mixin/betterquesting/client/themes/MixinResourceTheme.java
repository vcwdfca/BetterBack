package com.vcwdfca.bb.mixin.betterquesting.client.themes;

import betterquesting.api2.client.gui.resources.lines.IGuiLine;
import betterquesting.client.themes.ResourceTheme;
import com.google.gson.JsonObject;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalLine;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalQuestLineSpec;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalQuestLineTheme;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalQuestLineThemeImpl;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Applies upstream directional quest line resources to built-in Better Questing themes after JSON loading.
 */
@Mixin(value = ResourceTheme.class, remap = false)
public abstract class MixinResourceTheme {
    private static final DirectionalQuestLineTheme DIRECTIONAL_QUEST_LINE_THEME = new DirectionalQuestLineThemeImpl();

    @Shadow
    public abstract ResourceLocation getID();

    @Shadow
    public abstract void setLine(ResourceLocation resourceLocation, IGuiLine line);

    @Inject(method = "loadFromJson", at = @At("TAIL"))
    private void applyDirectionalQuestLines(JsonObject json, CallbackInfo ci) {
        for (DirectionalQuestLineSpec spec : DIRECTIONAL_QUEST_LINE_THEME.getQuestLineSpecs(getID())) {
            setLine(spec.getLineId(), new DirectionalLine(spec.getArrowSpec()));
        }
    }
}

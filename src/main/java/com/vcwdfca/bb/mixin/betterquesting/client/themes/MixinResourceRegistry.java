package com.vcwdfca.bb.mixin.betterquesting.client.themes;

import betterquesting.api2.client.gui.resources.lines.IGuiLine;
import betterquesting.api2.registry.FunctionRegistry;
import betterquesting.client.themes.ResourceRegistry;
import com.google.gson.JsonObject;
import com.vcwdfca.bb.api2.client.gui.resources.factories.lines.FactoryDirectionalLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ResourceRegistry.class, remap = false)
public abstract class MixinResourceRegistry {

    @Shadow
    public abstract FunctionRegistry<IGuiLine, JsonObject> getLineReg();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerDirectionalLineFactory(CallbackInfo ci) {
        getLineReg().register(
                FactoryDirectionalLine.REGISTRY_NAME,
                FactoryDirectionalLine::loadFromData,
                new JsonObject());
    }
}

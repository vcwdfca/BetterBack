package com.vcwdfca.bb.mixin.betterquesting.handlers;

import betterquesting.handlers.ConfigHandler;
import com.vcwdfca.bb.config.DependencyArrowSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ConfigHandler.class, remap = false)
public abstract class MixinConfigHandler {

    @Inject(method = "initConfigs", at = @At("TAIL"))
    private static void loadBetterBackDependencyArrowSettings(CallbackInfo ci) {
        if (ConfigHandler.config != null) {
            DependencyArrowSettings.load();
        }
    }
}

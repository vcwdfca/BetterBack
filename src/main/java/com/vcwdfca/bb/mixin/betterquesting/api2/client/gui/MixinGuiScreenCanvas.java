package com.vcwdfca.bb.mixin.betterquesting.api2.client.gui;

import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.IScene;
import betterquesting.client.gui2.GuiHome;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiScreenCanvas.class, remap = false)
public abstract class MixinGuiScreenCanvas extends GuiScreen implements IScene {
    @Unique
    GuiScreenCanvas betterBack$self = (GuiScreenCanvas)(Object)this;

    @Inject(method = "onMouseClick", at = @At("TAIL"), cancellable = true)
    private void back(int mx, int my, int click, CallbackInfoReturnable<Boolean> cir, @Local(name = "used") boolean used) {
        if(!used && click == 1) {
            if(betterBack$self.parent != null && betterBack$self.parent instanceof GuiScreenCanvas &&
                    !(betterBack$self instanceof GuiQuestLines && betterBack$self.parent instanceof GuiHome)) {
                mc.displayGuiScreen(betterBack$self.parent);
                cir.setReturnValue(false);
            }
        }
    }
}

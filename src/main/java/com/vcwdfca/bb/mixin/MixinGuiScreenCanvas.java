package com.vcwdfca.bb.mixin;

import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.IScene;
import betterquesting.client.gui2.GuiHome;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiScreenCanvas.class, remap = false)
public abstract class MixinGuiScreenCanvas extends GuiScreen implements IScene {
    @Inject(method = "onMouseClick(III)Z", at = @At("TAIL"), cancellable = true)
    private void back(int mx, int my, int click, CallbackInfoReturnable<Boolean> cir, @Local(name = "used") boolean used) {
        GuiScreenCanvas self = (GuiScreenCanvas)(Object)this;
        if(!used && click == 1) {
            if(self.parent instanceof GuiScreenCanvas &&
                    !(self instanceof GuiQuestLines && self.parent instanceof GuiHome)) {
                self.mc.displayGuiScreen(self.parent);
                cir.setReturnValue(false);
            }
        }
    }
}

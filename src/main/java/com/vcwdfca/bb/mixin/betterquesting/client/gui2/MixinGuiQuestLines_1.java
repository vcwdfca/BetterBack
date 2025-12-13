package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api2.client.gui.panels.IGuiPanel;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "betterquesting.client.gui2.GuiQuestLines$1", remap = false)
public class MixinGuiQuestLines_1 {
    @Shadow
    @Final
    boolean val$canEdit;

    @WrapOperation(method = "onMouseClick", at = @At(value = "INVOKE", target = "Lbetterquesting/client/gui2/GuiQuestLines;openPopup(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V"))
    private void fixPopup(GuiQuestLines instance, IGuiPanel panel, Operation<Void> original, @Local boolean questExistsUnderMouse) {
        if(val$canEdit || questExistsUnderMouse) original.call(instance, panel);
    }
}

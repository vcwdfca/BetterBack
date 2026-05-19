package com.vcwdfca.bb.mixin.betterquesting.api2.client.gui.controls;

import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PanelTextField.class, remap = false)
public abstract class MixinPanelTextField implements IGuiPanel {

    @Inject(method = "onMouseClick", at = @At(value = "FIELD", target = "Lbetterquesting/api2/client/gui/controls/PanelTextField;dragging:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), cancellable = true)
    private void disableBack(int mx, int my, int button, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}

package com.vcwdfca.bb.mixin.betterquesting.api2.client.gui.controls;

import betterquesting.api.misc.ICallback;
import betterquesting.api2.client.gui.controls.IFieldFilter;
import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PanelTextField.class, remap = false)
public abstract class MixinPanelTextField<T> implements IGuiPanel {
    @Shadow
    private String text;

    @Shadow
    private String watermark;

    @Shadow
    @Final
    private IFieldFilter<T> filter;

    @Shadow
    private ICallback<T> callback;

    @Shadow
    public abstract void setText(String text);

    @Inject(method = "onMouseClick", at = @At(value = "FIELD", target = "Lbetterquesting/api2/client/gui/controls/PanelTextField;canWrap:Z", opcode = Opcodes.GETFIELD))
    private void enableClearing(int mx, int my, int button, CallbackInfoReturnable<Boolean> cir) {
        if(button == 1/* && watermark.equals("Search...")*/) {
            setText("");

            if(callback != null) {
                callback.setValue(filter.parseValue(text));
            }
        }
    }

    @Inject(method = "onMouseClick", at = @At(value = "FIELD", target = "Lbetterquesting/api2/client/gui/controls/PanelTextField;isFocused:Z", opcode = Opcodes.GETFIELD, ordinal = 1), cancellable = true)
    private void disableBack(int mx, int my, int button, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}

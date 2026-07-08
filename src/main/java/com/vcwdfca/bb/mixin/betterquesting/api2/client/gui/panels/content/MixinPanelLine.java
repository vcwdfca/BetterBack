package com.vcwdfca.bb.mixin.betterquesting.api2.client.gui.panels.content;

import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.content.PanelLine;
import betterquesting.api2.client.gui.resources.colors.IGuiColor;
import betterquesting.api2.client.gui.resources.lines.IGuiLine;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vcwdfca.bb.api2.client.gui.resources.lines.DirectionalLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PanelLine.class, remap = false)
public abstract class MixinPanelLine {

    @WrapOperation(
            method = "drawPanel",
            at = @At(
                    value = "INVOKE",
                    target = "Lbetterquesting/api2/client/gui/resources/lines/IGuiLine;drawLine(Lbetterquesting/api2/client/gui/misc/IGuiRect;Lbetterquesting/api2/client/gui/misc/IGuiRect;ILbetterquesting/api2/client/gui/resources/colors/IGuiColor;F)V"
            )
    )
    private void drawDirectionalLine(
            IGuiLine line,
            IGuiRect start,
            IGuiRect end,
            int width,
            IGuiColor color,
            float partialTick,
            Operation<Void> original,
            int mx,
            int my,
            float drawPartialTick) {
        if (line instanceof DirectionalLine) {
            ((DirectionalLine) line).drawLine(start, end, width, color, partialTick,
                    start.contains(mx, my) || end.contains(mx, my));
            return;
        }
        original.call(line, start, end, width, color, partialTick);
    }
}

package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.panels.CanvasEmpty;
import betterquesting.api2.client.gui.panels.lists.CanvasQuestSearch;
import betterquesting.client.gui2.GuiQuestSearch;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiQuestSearch.class, remap = false)
public abstract class MixinGuiQuestSearch extends GuiScreenCanvas {
    @Shadow
    private PanelTextField<String> searchBox;

    @Unique
    private String betterBack$prevSearch;

    public MixinGuiQuestSearch(GuiScreen parent) {
        super(parent);
    }

    @Inject(method = "createSearchBox", at = @At(value = "INVOKE", target = "Lbetterquesting/api2/client/gui/panels/CanvasEmpty;addPanel(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V", shift = At.Shift.AFTER, ordinal = 1))
    private void saveSearch(CanvasEmpty cvInner, CallbackInfo ci, @Local(name = "canvasQuestSearch") CanvasQuestSearch canvasQuestSearch) {
        searchBox.setCallback((String searchText) -> {
            this.betterBack$prevSearch = searchText;
            canvasQuestSearch.setSearchFilter(searchText);
        });
    }

    @Inject(method = "createSearchBox", at = @At("RETURN"))
    private void prevSearch(CanvasEmpty cvInner, CallbackInfo ci, @Local(name = "canvasQuestSearch") CanvasQuestSearch canvasQuestSearch) {
        if(betterBack$prevSearch != null) {
            searchBox.setText(betterBack$prevSearch);
            searchBox.setCursorPosition(Integer.MAX_VALUE);
            canvasQuestSearch.setSearchFilter(betterBack$prevSearch);
        }
    }
}

package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api.client.gui.misc.INeedsRefresh;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.controls.PanelButton;
import betterquesting.api2.client.gui.controls.PanelButtonQuest;
import betterquesting.api2.client.gui.events.IPEventListener;
import betterquesting.api2.client.gui.panels.lists.CanvasQuestLine;
import betterquesting.api2.client.gui.resources.colors.GuiColorPulse;
import betterquesting.api2.client.gui.resources.colors.GuiColorStatic;
import betterquesting.api2.client.gui.resources.textures.GuiTextureColored;
import betterquesting.api2.storage.DBEntry;
import betterquesting.client.gui2.GuiQuestLines;
import betterquesting.client.gui2.GuiQuestSearch;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
@Mixin(value = GuiQuestLines.class, remap = false)
public abstract class MixinGuiQuestLines extends GuiScreenCanvas implements IPEventListener, INeedsRefresh {
    @Shadow
    private CanvasQuestLine cvQuest;

    @Unique
    private GuiQuestSearch betterBack$searchGui;

    public MixinGuiQuestLines(GuiScreen parent) {
        super(parent);
    }

    @Shadow
    protected abstract void openQuestLine(DBEntry<IQuestLine> q);

    @Inject(method = "initPanel()V", at = @At(value = "INVOKE", target = "Lbetterquesting/api2/client/gui/controls/PanelButton;<init>(Lbetterquesting/api2/client/gui/misc/IGuiRect;ILjava/lang/String;)V", ordinal = 1))
    private void BeforeInitBtnSearch(CallbackInfo ci) {
        if(betterBack$searchGui == null) {
            this.betterBack$searchGui = betterBack$initSearchPanel();
        }
    }

    @Redirect(method = "initPanel()V", at = @At(value = "INVOKE", target = "Lbetterquesting/api2/client/gui/controls/PanelButton;setClickAction(Ljava/util/function/Consumer;)Lbetterquesting/api2/client/gui/controls/PanelButton;", ordinal = 1))
    private PanelButton ClickAction(PanelButton instance, Consumer<PanelButton> action) {
        return instance.setClickAction((btn) -> this.mc.displayGuiScreen(this.betterBack$searchGui));
    }

    @Unique
    private GuiQuestSearch betterBack$initSearchPanel() {
        GuiQuestSearch searchGui = new GuiQuestSearch(this);
        searchGui.setCallback(entry -> {
            openQuestLine(entry.getQuestLineEntry());
            int selectedQuestId = entry.getQuest().getID();
            Optional<PanelButtonQuest> targetQuestButton = cvQuest.getQuestButtons().stream().filter(panelButtonQuest -> panelButtonQuest.getStoredValue().getID() == selectedQuestId).findFirst();
            targetQuestButton.ifPresent(panelButtonQuest -> {
                GuiTextureColored newTexture = new GuiTextureColored(panelButtonQuest.txFrame,
                        new GuiColorPulse(
                                new GuiColorStatic(255, 220, 115, 255),
                                new GuiColorStatic(255, 191, 0, 255),
                                1, 0
                        ));
                panelButtonQuest.setTextures(newTexture, newTexture, newTexture);
            });
        });

        return searchGui;
    }
}

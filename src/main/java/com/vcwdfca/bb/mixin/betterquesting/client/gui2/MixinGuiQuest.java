package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.utils.RenderUtils;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.controls.IPanelButton;
import betterquesting.api2.client.gui.controls.PanelButton;
import betterquesting.api2.client.gui.events.types.PEventButton;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.popups.PopContextMenu;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.gui2.GuiQuest;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.sugar.Local;
import com.vcwdfca.bb.questing.BetterQuestingRelationSource;
import com.vcwdfca.bb.questing.QuestLineNavigationImpl;
import com.vcwdfca.bb.questing.QuestRelationResolver;
import com.vcwdfca.bb.questing.QuestRelationResolverImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(value = GuiQuest.class, remap = false)
public abstract class MixinGuiQuest extends GuiScreenCanvas {

    @Shadow
    private IQuest quest;

    @Shadow
    @Final
    private int questID;

    public MixinGuiQuest(GuiScreen parent) {
        super(parent);
    }

    @Inject(method = "initPanel", at = @At(
            value = "INVOKE",
            target = "Lbetterquesting/api2/client/gui/panels/CanvasTextured;addPanel(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V",
            ordinal = 4,
            shift = At.Shift.AFTER
    ))
    private void addViewDepend(CallbackInfo ci, @Local(name = "cvBackground") CanvasTextured cvBackground) {
        int btnOffset = 34;

        if (!bb$getFilteredDependencies().isEmpty()) {
            PanelButton btnDeps = new PanelButton(new GuiTransform(GuiAlign.TOP_LEFT, btnOffset, 10, 16, 16, 0), 9, "");
            btnDeps.setIcon(PresetIcon.ICON_LEFT.getTexture());
            btnDeps.setTooltip(
                    Collections.singletonList(QuestTranslation.translate("betterquesting.btn.view_dependencies")));
            cvBackground.addPanel(btnDeps);
            btnOffset += 18;
        }

        if (!bb$getFilteredDependants().isEmpty()) {
            PanelButton btnDependants = new PanelButton(
                    new GuiTransform(GuiAlign.TOP_LEFT, btnOffset, 10, 16, 16, 0),
                    10,
                    "");
            btnDependants.setIcon(PresetIcon.ICON_RIGHT.getTexture());
            btnDependants.setTooltip(
                    Collections.singletonList(QuestTranslation.translate("betterquesting.btn.view_dependants")));
            cvBackground.addPanel(btnDependants);
        }
    }

    @Inject(method = "onButtonPress", at = @At("TAIL"))
    private void addDependencyPopup(PEventButton event, CallbackInfo ci) {
        IPanelButton btn = event.getButton();
        if (btn == null) {
            return;
        }

        if (btn.getButtonID() == 9) {
            bb$showDependencyPopup(btn, true);
        } else if (btn.getButtonID() == 10) {
            bb$showDependencyPopup(btn, false);
        }
    }

    @Unique
    private void bb$showDependencyPopup(IPanelButton btn, boolean isDependencies) {
        List<DBEntry<IQuest>> questEntries = isDependencies ? bb$getFilteredDependencies() : bb$getFilteredDependants();
        if (questEntries.isEmpty()) {
            return;
        }

        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int maxWidth = 0;
        for (DBEntry<IQuest> entry : questEntries) {
            String name = QuestTranslation.translate(entry.getValue().getProperty(NativeProps.NAME));
            int width = RenderUtils.getStringWidth(name, fr);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }

        IGuiRect btnRect = btn.getTransform();
        PopContextMenu popup = new PopContextMenu(
                new GuiRectangle(btnRect.getX(), btnRect.getY() + btnRect.getHeight(), maxWidth + 12,
                        Math.min(questEntries.size() * 16, 160)),
                true);

        for (DBEntry<IQuest> entry : questEntries) {
            int targetId = entry.getID();
            String name = QuestTranslation.translate(entry.getValue().getProperty(NativeProps.NAME));
            popup.addButton(name, null, () -> bb$navigateToQuest(targetId));
        }
        openPopup(popup);
    }

    @Unique
    private void bb$navigateToQuest(int targetId) {
        closePopup();
        GuiQuestLines questLines = bb$findQuestLinesParent();
        if (questLines == null) {
            questLines = new GuiQuestLines(this.parent);
        }
        mc.displayGuiScreen(questLines);
        new QuestLineNavigationImpl().navigateToQuest(questLines, targetId);
    }

    @Unique
    private GuiQuestLines bb$findQuestLinesParent() {
        GuiScreen screen = this.parent;
        while (screen != null) {
            if (screen instanceof GuiQuestLines) {
                return (GuiQuestLines) screen;
            }
            if (screen instanceof GuiScreenCanvas) {
                screen = ((GuiScreenCanvas) screen).parent;
            } else {
                return null;
            }
        }
        return null;
    }

    @Unique
    private List<DBEntry<IQuest>> bb$getFilteredDependencies() {
        return bb$getRelationResolver().resolveDependencies(questID, bb$getRelationSource());
    }

    @Unique
    private List<DBEntry<IQuest>> bb$getFilteredDependants() {
        return bb$getRelationResolver().resolveDependants(questID, bb$getRelationSource());
    }

    @Unique
    private QuestRelationResolver<DBEntry<IQuest>> bb$getRelationResolver() {
        return new QuestRelationResolverImpl<>();
    }

    @Unique
    private BetterQuestingRelationSource bb$getRelationSource() {
        return new BetterQuestingRelationSource(mc.player);
    }
}

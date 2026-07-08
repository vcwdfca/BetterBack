package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.enums.EnumQuestVisibility;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.controls.PanelButton;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiPadding;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.api2.client.gui.panels.lists.CanvasHoverTray;
import betterquesting.api2.client.gui.panels.lists.CanvasScrolling;
import betterquesting.api2.client.gui.resources.colors.GuiColorStatic;
import betterquesting.api2.client.gui.themes.presets.PresetColor;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.sugar.Local;
import com.vcwdfca.bb.config.DependencyArrowSettings;
import com.vcwdfca.bb.questing.BetterQuestingCompletionSource;
import com.vcwdfca.bb.questing.QuestCompletionCounter;
import com.vcwdfca.bb.questing.QuestCompletionCounterImpl;
import com.vcwdfca.bb.questing.QuestCompletionStats;
import com.vcwdfca.bb.questing.QuestVisibilityCompletion;
import com.vcwdfca.bb.questing.QuestVisibilityCompletionImpl;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Tuple;
import org.lwjgl.util.vector.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mixin(value = GuiQuestLines.class, remap = false)
public abstract class MixinGuiQuestLines extends GuiScreenCanvas {

    @Shadow
    private IQuestLine selectedLine;

    @Shadow
    private List<Tuple<DBEntry<IQuestLine>, Integer>> visChapters;

    @Shadow
    private CanvasHoverTray cvChapterTray;

    @Shadow
    private CanvasScrolling cvLines;

    @Shadow
    private PanelTextBox completionText;

    @Shadow
    public abstract void refreshGui();

    @Unique
    private final QuestCompletionCounter<IQuest> bb$completionCounter = new QuestCompletionCounterImpl<>();

    @Unique
    private final QuestVisibilityCompletion bb$visibilityCompletion = new QuestVisibilityCompletionImpl();

    @Unique
    private PanelTextBox bb$globalCompletionText;

    public MixinGuiQuestLines(GuiScreen parent) {
        super(parent);
    }

    @Inject(
            method = "initPanel",
            at = @At(
                    value = "INVOKE",
                    target = "Lbetterquesting/api2/client/gui/panels/IGuiCanvas;addPanel(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void addGlobalCompletionText(CallbackInfo ci) {
        if (cvLines.getTransform() instanceof GuiTransform) {
            GuiPadding padding = ((GuiTransform) cvLines.getTransform()).getPadding();
            padding.t = Math.max(padding.t, 20);
        }
        bb$globalCompletionText = new PanelTextBox(
                new GuiTransform(new Vector4f(0F, 0F, 1F, 0F), new GuiPadding(8, 8, 16, -20), 0),
                "");
        bb$globalCompletionText.setColor(PresetColor.TEXT_HEADER.getColor());
        cvChapterTray.getCanvasOpen().addPanel(bb$globalCompletionText);
    }

    @Inject(
            method = "initPanel",
            at = @At(
                    value = "INVOKE",
                    target = "Lbetterquesting/api2/client/gui/panels/CanvasTextured;addPanel(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V",
                    ordinal = 15,
                    shift = At.Shift.AFTER
            )
    )
    private void addDependencyArrowButton(CallbackInfo ci, @Local(name = "cvBackground") CanvasTextured cvBackground) {
        PanelButton button = new PanelButton(new GuiTransform(GuiAlign.TOP_LEFT, 8, 120, 32, 16, -2), -1, "");
        bb$updateDependencyArrowButton(button);
        button.setClickAction(clickedButton -> {
            DependencyArrowSettings.setShowDependencyArrows(!DependencyArrowSettings.shouldShowDependencyArrows());
            bb$updateDependencyArrowButton(clickedButton);
            refreshGui();
        });
        cvBackground.addPanel(button);
    }

    @Inject(method = "refreshQuestCompletion", at = @At("HEAD"), cancellable = true)
    private void refreshQuestCompletionWithPercent(CallbackInfo ci) {
        if (selectedLine == null) {
            ci.cancel();
            return;
        }

        QuestCompletionStats stats = bb$completionCounter.countChapter(
                bb$getQuestIds(selectedLine),
                new BetterQuestingCompletionSource(QuestingAPI.getQuestingUUID(mc.player)));
        completionText.setText(QuestTranslation.translate(
                "betterquesting.title.completion",
                stats.getCompleted(),
                stats.getTotal(),
                stats.getPercentText()));
        ci.cancel();
    }

    @Inject(method = "refreshChapterVisibility", at = @At("TAIL"))
    private void refreshGlobalCompletion(CallbackInfo ci) {
        if (bb$globalCompletionText == null) {
            return;
        }

        List<Iterable<Integer>> chapters = new ArrayList<>();
        for (Tuple<DBEntry<IQuestLine>, Integer> visibleChapter : visChapters) {
            chapters.add(bb$getQuestIds(visibleChapter.getFirst().getValue()));
        }
        QuestCompletionStats stats = bb$completionCounter.countGlobal(
                chapters,
                new BetterQuestingCompletionSource(QuestingAPI.getQuestingUUID(mc.player)));
        bb$globalCompletionText.setText(QuestTranslation.translate(
                "betterquesting.title.completion_total",
                stats.getCompleted(),
                stats.getTotal(),
                stats.getPercentText()));
    }

    @Inject(method = "isQuestCompletedForQuestline", at = @At("HEAD"), cancellable = true)
    private void hiddenAndSecretQuestsAreCompleteForQuestLine(UUID playerID, IQuest quest, CallbackInfoReturnable<Boolean> cir) {
        EnumQuestVisibility visibility = quest.getProperty(NativeProps.VISIBILITY);
        if (bb$visibilityCompletion.isAlwaysComplete(BetterQuestingCompletionSource.toVisibilityState(visibility))) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private void bb$updateDependencyArrowButton(PanelButton button) {
        boolean show = DependencyArrowSettings.shouldShowDependencyArrows();
        button.setIcon(
                PresetIcon.ICON_TWO_WAY.getTexture(),
                show ? new GuiColorStatic(0xFFFFFFFF) : new GuiColorStatic(0xFF444444),
                0);
        button.setTooltip(Collections.singletonList(QuestTranslation.translate("betterquesting.btn.show_dependency_arrows")));
    }

    @Unique
    private Iterable<Integer> bb$getQuestIds(IQuestLine questLine) {
        List<Integer> questIds = new ArrayList<>();
        for (DBEntry<IQuestLineEntry> entry : questLine.getEntries()) {
            questIds.add(entry.getID());
        }
        return questIds;
    }
}

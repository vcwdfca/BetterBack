package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api2.client.gui.controls.PanelButtonQuest;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import betterquesting.api2.client.gui.popups.PopContextMenu;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.gui2.GuiQuestLines;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vcwdfca.bb.api2.client.gui.popups.PopContextMenuHoverSub;
import com.vcwdfca.bb.questing.BetterQuestingRelationSource;
import com.vcwdfca.bb.questing.QuestLineNavigationImpl;
import com.vcwdfca.bb.questing.QuestRelationResolver;
import com.vcwdfca.bb.questing.QuestRelationResolverImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(targets = "betterquesting.client.gui2.GuiQuestLines$1", remap = false)
public abstract class MixinGuiQuestLines_1 {

    @Shadow
    @Final
    GuiQuestLines this$0;

    @Shadow
    @Final
    boolean val$canEdit;

    @WrapOperation(
            method = "onMouseClick",
            at = @At(
                    value = "NEW",
                    target = "betterquesting/api2/client/gui/popups/PopContextMenu"
            )
    )
    private PopContextMenu useHoverSubMenu(GuiRectangle rect, boolean autoClose, Operation<PopContextMenu> original) {
        return new PopContextMenuHoverSub(rect, autoClose);
    }

    @Inject(
            method = "onMouseClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lbetterquesting/api2/client/gui/popups/PopContextMenu;addButton(Ljava/lang/String;Lbetterquesting/api2/client/gui/resources/textures/IGuiTexture;Ljava/lang/Runnable;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void addDependPopup(int mx, int my, int click, CallbackInfoReturnable<Boolean> cir,
            @Local(name = "questExistsUnderMouse") boolean questExistsUnderMouse,
            @Local(name = "popup") PopContextMenu popup) {
        if (!questExistsUnderMouse || !(popup instanceof PopContextMenuHoverSub)) {
            return;
        }

        PanelButtonQuest questButton = ((IMixinGuiQuestLines) this$0).getCvQuest().getButtonAt(mx, my);
        if (questButton == null) {
            return;
        }

        int questId = questButton.getStoredValue().getID();
        PopContextMenuHoverSub hoverPopup = (PopContextMenuHoverSub) popup;
        hoverPopup.addSubMenu(
                "betterquesting.btn.view_dependencies",
                bb$getSubMenuEntries(bb$getRelationResolver().resolveDependencies(questId, bb$getRelationSource())));
        hoverPopup.addSubMenu(
                "betterquesting.btn.view_dependants",
                bb$getSubMenuEntries(bb$getRelationResolver().resolveDependants(questId, bb$getRelationSource())));
    }

    @WrapOperation(
            method = "onMouseClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lbetterquesting/client/gui2/GuiQuestLines;openPopup(Lbetterquesting/api2/client/gui/panels/IGuiPanel;)V"
            )
    )
    private void fixPopup(GuiQuestLines instance, IGuiPanel panel, Operation<Void> original,
            @Local(name = "questExistsUnderMouse") boolean questExistsUnderMouse) {
        if (val$canEdit || questExistsUnderMouse) {
            original.call(instance, panel);
        }
    }

    @Unique
    private List<PopContextMenuHoverSub.SubMenuEntry> bb$getSubMenuEntries(List<DBEntry<IQuest>> quests) {
        List<PopContextMenuHoverSub.SubMenuEntry> entries = new ArrayList<>();
        for (DBEntry<IQuest> entry : quests) {
            int targetId = entry.getID();
            String name = QuestTranslation.translate(entry.getValue().getProperty(NativeProps.NAME));
            entries.add(new PopContextMenuHoverSub.SubMenuEntry(name, () -> bb$navigateToQuest(targetId)));
        }
        return entries;
    }

    @Unique
    private void bb$navigateToQuest(int targetId) {
        this$0.closePopup();
        new QuestLineNavigationImpl().navigateToQuest(this$0, targetId);
    }

    @Unique
    private QuestRelationResolver<DBEntry<IQuest>> bb$getRelationResolver() {
        return new QuestRelationResolverImpl<>();
    }

    @Unique
    private BetterQuestingRelationSource bb$getRelationSource() {
        return new BetterQuestingRelationSource(this$0.mc.player);
    }
}

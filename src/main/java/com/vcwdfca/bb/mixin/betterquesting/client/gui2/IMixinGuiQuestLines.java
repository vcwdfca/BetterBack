package com.vcwdfca.bb.mixin.betterquesting.client.gui2;

import betterquesting.api.questing.IQuestLine;
import betterquesting.api2.client.gui.controls.PanelButtonQuest;
import betterquesting.api2.client.gui.panels.lists.CanvasQuestLine;
import betterquesting.api2.storage.DBEntry;
import betterquesting.client.gui2.GuiQuestLines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = GuiQuestLines.class, remap = false)
public interface IMixinGuiQuestLines {

    @Accessor("cvQuest")
    CanvasQuestLine getCvQuest();

    @Invoker("openQuestLine")
    void callOpenQuestLine(DBEntry<IQuestLine> questLine);

    @Invoker("highlightButton")
    void callHighlightButton(PanelButtonQuest panelButtonQuest);
}

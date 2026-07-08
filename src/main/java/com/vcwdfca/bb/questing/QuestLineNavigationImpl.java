package com.vcwdfca.bb.questing;

import betterquesting.api.questing.IQuestLine;
import betterquesting.api2.client.gui.controls.PanelButtonQuest;
import betterquesting.api2.storage.DBEntry;
import betterquesting.client.gui2.GuiQuestLines;
import betterquesting.questing.QuestLineDatabase;
import com.vcwdfca.bb.mixin.betterquesting.client.gui2.IMixinGuiQuestLines;

/**
 * Mixin-backed implementation of {@link QuestLineNavigation}.
 *
 * <p>Better Questing 4.3.2 keeps quest-line opening and highlighting private,
 * so this implementation uses typed mixin invokers instead of reflection.</p>
 */
public class QuestLineNavigationImpl implements QuestLineNavigation {

    @Override
    public boolean navigateToQuest(GuiQuestLines questLines, int targetQuestId) {
        IMixinGuiQuestLines access = (IMixinGuiQuestLines) questLines;
        for (DBEntry<IQuestLine> lineEntry : QuestLineDatabase.INSTANCE.getEntries()) {
            if (lineEntry.getValue().getValue(targetQuestId) != null) {
                access.callOpenQuestLine(lineEntry);
                return highlightQuestButton(access, targetQuestId);
            }
        }
        return false;
    }

    private boolean highlightQuestButton(IMixinGuiQuestLines access, int targetQuestId) {
        for (PanelButtonQuest questButton : access.getCvQuest().getQuestButtons()) {
            if (questButton.getStoredValue().getID() == targetQuestId) {
                access.callHighlightButton(questButton);
                return true;
            }
        }
        return false;
    }
}

package com.vcwdfca.bb.questing;

import betterquesting.client.gui2.GuiQuestLines;

/**
 * Navigates an open Better Questing quest-line GUI to a target quest.
 *
 * <p>The interface exists because the target Better Questing version does not
 * expose a public navigation method. Implementations can use mixin accessors to
 * open the containing quest line and highlight the target quest button without
 * reflection.</p>
 */
public interface QuestLineNavigation {

    /**
     * Opens the quest line containing the target quest and highlights its button.
     *
     * @param questLines quest-line GUI to mutate
     * @param targetQuestId Better Questing quest ID to navigate to
     * @return true when a quest-line button was found and highlighted
     */
    boolean navigateToQuest(GuiQuestLines questLines, int targetQuestId);
}

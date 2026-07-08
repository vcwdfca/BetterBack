package com.vcwdfca.bb.questing;

/**
 * Defines visibility-only completion rules for quest-line visibility checks.
 *
 * <p>Better Questing uses chapter completion to decide chapter badges and
 * visibility. PR #223 makes hidden and secret quests count as already complete
 * for that purpose, while still excluding them from numeric completion totals.</p>
 */
public interface QuestVisibilityCompletion {

    /**
     * Checks whether a visibility state should be treated as complete without
     * inspecting player progress.
     *
     * @param visibilityState quest visibility state
     * @return {@code true} for states that should never block chapter completion
     */
    boolean isAlwaysComplete(QuestVisibilityState visibilityState);
}

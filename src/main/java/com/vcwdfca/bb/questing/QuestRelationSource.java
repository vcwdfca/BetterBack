package com.vcwdfca.bb.questing;

import javax.annotation.Nullable;

/**
 * Provides quest relation data for {@link QuestRelationResolver}.
 *
 * <p>The resolver is intentionally independent from Better Questing runtime
 * classes so the filtering rules can be tested as plain Java logic. An
 * implementation supplies the quest lookup, requirement IDs, ordering source,
 * quest-line membership, and current visibility checks required to build the
 * dependency menus.</p>
 *
 * @param <T> quest entry type returned to callers after filtering
 */
public interface QuestRelationSource<T> {

    /**
     * Looks up a quest entry by ID.
     *
     * <p>This is used when resolving direct dependencies from requirement IDs.
     * Returning {@code null} means the referenced quest no longer exists and
     * must be skipped.</p>
     *
     * @param questId Better Questing quest ID
     * @return quest entry, or {@code null} when the ID is missing
     */
    @Nullable
    T getQuest(int questId);

    /**
     * Returns the requirement IDs declared by a quest entry.
     *
     * <p>The order is meaningful for direct dependencies and is preserved by
     * the resolver.</p>
     *
     * @param quest quest entry whose requirements are being inspected
     * @return requirement quest IDs
     */
    int[] getRequirements(T quest);

    /**
     * Returns every quest entry in the source traversal order.
     *
     * <p>The resolver preserves this order when finding dependants, so Better
     * Questing database order is reflected in the generated menu.</p>
     *
     * @return iterable of all quest entries
     */
    Iterable<T> getAllQuests();

    /**
     * Extracts the Better Questing quest ID from an entry.
     *
     * @param quest quest entry returned by this source
     * @return numeric quest ID
     */
    int getQuestId(T quest);

    /**
     * Checks whether a quest ID is present on at least one quest line.
     *
     * <p>Relations to hidden database-only quests are excluded from navigation
     * menus because there is no quest-line button to navigate to.</p>
     *
     * @param questId Better Questing quest ID
     * @return {@code true} when the quest can be found on a quest line
     */
    boolean isInQuestLine(int questId);

    /**
     * Checks whether the current player can see a quest entry.
     *
     * @param quest quest entry being filtered
     * @return {@code true} when the quest should be displayed to the player
     */
    boolean isShown(T quest);
}

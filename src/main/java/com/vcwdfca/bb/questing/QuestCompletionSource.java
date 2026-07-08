package com.vcwdfca.bb.questing;

import javax.annotation.Nullable;

/**
 * Supplies quest data for {@link QuestCompletionCounter}.
 *
 * <p>The counter only needs lookup, countability, and completion checks. This
 * interface keeps those decisions outside the counting algorithm so Better
 * Questing runtime objects and plain tests can use the same rules.</p>
 *
 * @param <T> quest object type provided by the backing system
 */
public interface QuestCompletionSource<T> {

    /**
     * Looks up a quest by its Better Questing ID.
     *
     * <p>Quest lines may contain stale IDs, so a missing quest is represented
     * as {@code null} and skipped by the counter.</p>
     *
     * @param questId Better Questing quest ID
     * @return quest object, or {@code null} when it no longer exists
     */
    @Nullable
    T getQuest(int questId);

    /**
     * Checks whether a quest belongs in visible completion totals.
     *
     * <p>PR #223 excludes hidden and secret quests from completion counts, so
     * adapters encode that visibility rule here.</p>
     *
     * @param quest quest returned by {@link #getQuest(int)}
     * @return {@code true} when this quest should be counted
     */
    boolean isCounted(T quest);

    /**
     * Checks whether a quest contributes to the completed count.
     *
     * @param quest counted quest being evaluated
     * @return {@code true} when this quest is complete for the current player
     */
    boolean isComplete(T quest);

    /**
     * Adjusts the total count before the quest itself is counted.
     *
     * <p>Better Questing 4.3.2 reduces XOR prerequisite quests by
     * {@code requirements - 1}. Keeping this hook in the source preserves that
     * runtime behavior without coupling the generic counter to BQ enums.</p>
     *
     * @param quest counted quest being evaluated
     * @return signed adjustment applied before adding this quest to the total
     */
    default int getTotalAdjustment(T quest) {
        return 0;
    }
}

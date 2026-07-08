package com.vcwdfca.bb.questing;

/**
 * Counts visible quest completion for quest-line GUI summaries.
 *
 * <p>The GUI needs both per-chapter and global totals. Implementations accept
 * quest IDs instead of GUI widgets so the counting rules stay independent from
 * Better Questing screen classes.</p>
 *
 * @param <T> quest object type supplied by the source adapter
 */
public interface QuestCompletionCounter<T> {

    /**
     * Counts completion for a single chapter.
     *
     * @param questIds quest IDs in the selected quest line
     * @param source quest data adapter
     * @return completed, total, and formatted percentage
     */
    QuestCompletionStats countChapter(Iterable<Integer> questIds, QuestCompletionSource<T> source);

    /**
     * Counts completion across visible chapters.
     *
     * <p>Global totals de-duplicate quests that appear on more than one visible
     * chapter, matching PR #223's global completion behavior.</p>
     *
     * @param chapterQuestIds visible chapter quest ID collections
     * @param source quest data adapter
     * @return completed, total, and formatted percentage
     */
    QuestCompletionStats countGlobal(
            Iterable<? extends Iterable<Integer>> chapterQuestIds,
            QuestCompletionSource<T> source);
}

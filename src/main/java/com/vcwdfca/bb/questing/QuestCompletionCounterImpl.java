package com.vcwdfca.bb.questing;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Default quest completion counter.
 *
 * @param <T> quest object type supplied by the source adapter
 */
public class QuestCompletionCounterImpl<T> implements QuestCompletionCounter<T> {

    @Override
    public QuestCompletionStats countChapter(Iterable<Integer> questIds, QuestCompletionSource<T> source) {
        Objects.requireNonNull(questIds, "questIds");
        Objects.requireNonNull(source, "source");

        int completed = 0;
        int total = 0;
        for (Integer questId : questIds) {
            if (questId == null) {
                continue;
            }
            T quest = source.getQuest(questId);
            if (quest == null || !source.isCounted(quest)) {
                continue;
            }
            total += source.getTotalAdjustment(quest);
            total++;
            if (source.isComplete(quest)) {
                completed++;
            }
        }
        return new QuestCompletionStats(completed, total);
    }

    @Override
    public QuestCompletionStats countGlobal(
            Iterable<? extends Iterable<Integer>> chapterQuestIds,
            QuestCompletionSource<T> source) {
        Objects.requireNonNull(chapterQuestIds, "chapterQuestIds");
        Objects.requireNonNull(source, "source");

        int completed = 0;
        int total = 0;
        Set<Integer> seenQuestIds = new HashSet<>();
        for (Iterable<Integer> questIds : chapterQuestIds) {
            if (questIds == null) {
                continue;
            }
            for (Integer questId : questIds) {
                if (questId == null || !seenQuestIds.add(questId)) {
                    continue;
                }
                T quest = source.getQuest(questId);
                if (quest == null || !source.isCounted(quest)) {
                    continue;
                }
                total++;
                if (source.isComplete(quest)) {
                    completed++;
                }
            }
        }
        return new QuestCompletionStats(completed, total);
    }
}

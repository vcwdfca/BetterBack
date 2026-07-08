package com.vcwdfca.bb.questing;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link QuestRelationResolver}.
 *
 * <p>The implementation applies the shared BetterBack relation-menu rules:
 * missing quests, quests absent from quest lines, and quests hidden from the
 * current player are excluded. Dependencies preserve requirement order, while
 * dependants preserve source traversal order.</p>
 *
 * @param <T> quest entry type returned after filtering
 */
public class QuestRelationResolverImpl<T> implements QuestRelationResolver<T> {

    @Override
    public List<T> resolveDependencies(int targetQuestId, QuestRelationSource<T> source) {
        T targetQuest = source.getQuest(targetQuestId);
        List<T> result = new ArrayList<>();
        if (targetQuest == null) {
            return result;
        }

        for (int requirementId : source.getRequirements(targetQuest)) {
            T dependency = source.getQuest(requirementId);
            if (dependency != null && isNavigableVisibleQuest(requirementId, dependency, source)) {
                result.add(dependency);
            }
        }

        return result;
    }

    @Override
    public List<T> resolveDependants(int targetQuestId, QuestRelationSource<T> source) {
        List<T> result = new ArrayList<>();
        for (T candidate : source.getAllQuests()) {
            int candidateId = source.getQuestId(candidate);
            if (hasRequirement(candidate, targetQuestId, source)
                    && isNavigableVisibleQuest(candidateId, candidate, source)) {
                result.add(candidate);
            }
        }

        return result;
    }

    private boolean hasRequirement(T quest, int targetQuestId, QuestRelationSource<T> source) {
        for (int requirementId : source.getRequirements(quest)) {
            if (requirementId == targetQuestId) {
                return true;
            }
        }

        return false;
    }

    private boolean isNavigableVisibleQuest(int questId, T quest, QuestRelationSource<T> source) {
        return source.isInQuestLine(questId) && source.isShown(quest);
    }
}

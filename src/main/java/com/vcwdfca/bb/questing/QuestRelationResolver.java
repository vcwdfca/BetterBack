package com.vcwdfca.bb.questing;

import java.util.List;

/**
 * Resolves visible quest relations for dependency navigation menus.
 *
 * <p>The interface exists to keep relation filtering separate from Better
 * Questing GUI mixins. Implementations receive all external data through
 * {@link QuestRelationSource}, which makes the behavior testable without
 * Minecraft or Better Questing runtime state.</p>
 *
 * @param <T> quest entry type returned after filtering
 */
public interface QuestRelationResolver<T> {

    /**
     * Resolves direct dependencies of the target quest.
     *
     * <p>Returned entries must exist, be present on a quest line, be visible to
     * the player, and keep the target quest's requirement order.</p>
     *
     * @param targetQuestId quest whose requirements are being displayed
     * @param source relation data source
     * @return filtered dependency entries
     */
    List<T> resolveDependencies(int targetQuestId, QuestRelationSource<T> source);

    /**
     * Resolves direct dependants of the target quest.
     *
     * <p>Returned entries must require the target quest, be present on a quest
     * line, be visible to the player, and keep {@link QuestRelationSource}
     * traversal order.</p>
     *
     * @param targetQuestId quest that other quests may require
     * @param source relation data source
     * @return filtered dependant entries
     */
    List<T> resolveDependants(int targetQuestId, QuestRelationSource<T> source);
}

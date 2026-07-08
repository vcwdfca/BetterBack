package com.vcwdfca.bb.questing;

import betterquesting.api.enums.EnumLogic;
import betterquesting.api.enums.EnumQuestVisibility;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.core.BetterQuesting;
import betterquesting.questing.QuestDatabase;

import java.util.UUID;

/**
 * Better Questing-backed completion data source.
 */
public class BetterQuestingCompletionSource implements QuestCompletionSource<IQuest> {
    private final UUID playerUUID;

    public BetterQuestingCompletionSource(UUID playerUUID) {
        if (playerUUID == null) {
            throw new IllegalArgumentException("playerUUID cannot be null");
        }
        this.playerUUID = playerUUID;
    }

    @Override
    public IQuest getQuest(int questId) {
        IQuest quest = QuestDatabase.INSTANCE.getValue(questId);
        if (quest == null) {
            BetterQuesting.logger.warn("Skipping missing quest with ID {}", questId);
        }
        return quest;
    }

    @Override
    public boolean isCounted(IQuest quest) {
        EnumQuestVisibility visibility = quest.getProperty(NativeProps.VISIBILITY);
        return !isHiddenOrSecret(visibility);
    }

    @Override
    public boolean isComplete(IQuest quest) {
        return quest.isComplete(playerUUID);
    }

    @Override
    public int getTotalAdjustment(IQuest quest) {
        if (quest.getProperty(NativeProps.LOGIC_QUEST) != EnumLogic.XOR) {
            return 0;
        }
        return -Math.max(0, quest.getRequirements().length - 1);
    }

    public static QuestVisibilityState toVisibilityState(EnumQuestVisibility visibility) {
        if (visibility == EnumQuestVisibility.HIDDEN) {
            return QuestVisibilityState.HIDDEN;
        }
        if (visibility != null && "SECRET".equals(visibility.name())) {
            return QuestVisibilityState.SECRET;
        }
        return QuestVisibilityState.NORMAL;
    }

    private static boolean isHiddenOrSecret(EnumQuestVisibility visibility) {
        return visibility == EnumQuestVisibility.HIDDEN || (visibility != null && "SECRET".equals(visibility.name()));
    }
}

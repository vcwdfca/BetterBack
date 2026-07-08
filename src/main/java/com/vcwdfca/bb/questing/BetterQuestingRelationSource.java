package com.vcwdfca.bb.questing;

import betterquesting.api.api.QuestingAPI;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api2.cache.QuestCache;
import betterquesting.api2.storage.DBEntry;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestLineDatabase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Better Questing-backed relation data source.
 *
 * <p>This adapter supplies quest lookups, database traversal, quest-line
 * membership, and player visibility to {@link QuestRelationResolver}. It keeps
 * Better Questing runtime calls out of the resolver so relation filtering stays
 * directly testable.</p>
 */
public class BetterQuestingRelationSource implements QuestRelationSource<DBEntry<IQuest>> {
    private final EntityPlayer player;
    private final UUID playerUUID;

    public BetterQuestingRelationSource(EntityPlayer player) {
        this.player = player;
        this.playerUUID = QuestingAPI.getQuestingUUID(player);
    }

    @Override
    public DBEntry<IQuest> getQuest(int questId) {
        IQuest quest = QuestDatabase.INSTANCE.getValue(questId);
        return quest == null ? null : new DBEntry<>(questId, quest);
    }

    @Override
    public int[] getRequirements(DBEntry<IQuest> quest) {
        return quest.getValue().getRequirements();
    }

    @Override
    public Iterable<DBEntry<IQuest>> getAllQuests() {
        return QuestDatabase.INSTANCE.getEntries();
    }

    @Override
    public int getQuestId(DBEntry<IQuest> quest) {
        return quest.getID();
    }

    @Override
    public boolean isInQuestLine(int questId) {
        for (DBEntry<IQuestLine> lineEntry : QuestLineDatabase.INSTANCE.getEntries()) {
            if (lineEntry.getValue().getValue(questId) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isShown(DBEntry<IQuest> quest) {
        return QuestCache.isQuestShown(quest.getValue(), playerUUID, player);
    }
}

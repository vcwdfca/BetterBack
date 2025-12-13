package com.vcwdfca.bb.mixin.betterquesting.api2.client.gui.panels.lists;

import betterquesting.api2.client.gui.panels.lists.CanvasQuestSearch;
import betterquesting.misc.QuestSearchEntry;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.vcwdfca.bb.BetterBackMod;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayDeque;

@Mixin(value = CanvasQuestSearch.class, remap = false)
public class MixinCanvasQuestSearch {
    @WrapMethod(method = "queryMatches*")
    private void guardQueryMatches(QuestSearchEntry entry, String query, ArrayDeque<QuestSearchEntry> results, Operation<Void> original) {
        try {
            original.call(entry, query, results);
        } catch (Throwable t) {
            BetterBackMod.LOGGER.error("Unexpected exception while checking if quest with ID '{}' from quest line '{}' matches query '{}'!",
                                    entry.getQuest()
                                            .getID(),
                                    entry.getQuestLineEntry()
                                            .getID(),
                                    query
            );
            BetterBackMod.LOGGER.error(t);
            throw new RuntimeException("Unexpected exception while checking if quest matched query", t);
        }
    }
}

package com.vcwdfca.bb.questing;

import java.util.Objects;

/**
 * Default visibility completion rule.
 */
public class QuestVisibilityCompletionImpl implements QuestVisibilityCompletion {

    @Override
    public boolean isAlwaysComplete(QuestVisibilityState visibilityState) {
        Objects.requireNonNull(visibilityState, "visibilityState");
        return visibilityState == QuestVisibilityState.HIDDEN || visibilityState == QuestVisibilityState.SECRET;
    }
}

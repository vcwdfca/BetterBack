package com.vcwdfca.bb.questing;

import java.util.Locale;

/**
 * Immutable completion-count result for quest-line title text.
 */
public final class QuestCompletionStats {
    private final int completed;
    private final int total;

    public QuestCompletionStats(int completed, int total) {
        if (completed < 0) {
            throw new IllegalArgumentException("completed must be non-negative");
        }
        this.completed = completed;
        this.total = total;
    }

    public int getCompleted() {
        return completed;
    }

    public int getTotal() {
        return total;
    }

    public String getPercentText() {
        if (total <= 0) {
            return "0.00";
        }
        return String.format(Locale.ROOT, "%.2f", completed * 100D / total);
    }
}

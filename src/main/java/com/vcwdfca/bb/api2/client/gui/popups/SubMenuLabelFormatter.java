package com.vcwdfca.bb.api2.client.gui.popups;

import javax.annotation.Nonnull;

/**
 * Formats labels for hover sub-menu entries.
 *
 * <p>The hover sub-menu needs to keep translation keys intact until the label
 * is rendered, then append its visual arrow after translation.</p>
 */
public interface SubMenuLabelFormatter {

    /**
     * Translates a sub-menu label key and appends the hover-menu arrow.
     *
     * @param text translation key or literal label supplied by the caller
     * @param translator translation function used by the active GUI language
     * @return rendered sub-menu label
     */
    String formatSubMenuLabel(@Nonnull String text, @Nonnull TextTranslator translator);

    /**
     * Translation function used by {@link #formatSubMenuLabel(String, TextTranslator)}.
     */
    interface TextTranslator {

        /**
         * Converts a translation key or literal string into the displayed text.
         *
         * @param text translation key or literal label
         * @return displayed label text
         */
        String translate(@Nonnull String text);
    }
}

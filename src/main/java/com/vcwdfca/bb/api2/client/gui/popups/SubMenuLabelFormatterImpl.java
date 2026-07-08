package com.vcwdfca.bb.api2.client.gui.popups;

import javax.annotation.Nonnull;

/**
 * Default formatter for hover sub-menu labels.
 */
public class SubMenuLabelFormatterImpl implements SubMenuLabelFormatter {

    private static final String SUB_MENU_ARROW = " ▶";

    @Override
    public String formatSubMenuLabel(@Nonnull String text, @Nonnull TextTranslator translator) {
        return translator.translate(text) + SUB_MENU_ARROW;
    }
}

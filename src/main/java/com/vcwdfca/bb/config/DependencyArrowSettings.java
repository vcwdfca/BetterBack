package com.vcwdfca.bb.config;

import betterquesting.handlers.ConfigHandler;
import net.minecraftforge.common.config.Configuration;

/**
 * Dependency arrow configuration stored in the Better Questing config file.
 */
public final class DependencyArrowSettings {
    public static final String SHOW_PROPERTY = "Show dependency arrows";
    public static final String ANIMATE_PROPERTY = "Animate dependency arrows";

    private static boolean showDependencyArrows = true;
    private static boolean animateDependencyArrows = true;

    private DependencyArrowSettings() {
    }

    public static void load() {
        if (ConfigHandler.config == null) {
            throw new IllegalStateException("Better Questing config is not initialised");
        }
        showDependencyArrows = ConfigHandler.config.getBoolean(
                SHOW_PROPERTY,
                Configuration.CATEGORY_GENERAL,
                true,
                "If true, quest dependency lines will render directional arrows. This property can be changed by the GUI.");
        animateDependencyArrows = ConfigHandler.config.getBoolean(
                ANIMATE_PROPERTY,
                Configuration.CATEGORY_GENERAL,
                true,
                "If true, directional arrows on quest dependency lines will animate on quest hover.");
        ConfigHandler.config.save();
    }

    public static boolean shouldShowDependencyArrows() {
        return showDependencyArrows;
    }

    public static boolean shouldAnimateDependencyArrows() {
        return animateDependencyArrows;
    }

    public static void setShowDependencyArrows(boolean value) {
        showDependencyArrows = value;
        if (ConfigHandler.config == null) {
            throw new IllegalStateException("Better Questing config is not initialised");
        }
        ConfigHandler.config.get(
                Configuration.CATEGORY_GENERAL,
                SHOW_PROPERTY,
                true,
                "If true, quest dependency lines will render directional arrows. This property can be changed by the GUI.")
                .set(value);
        ConfigHandler.config.save();
    }
}

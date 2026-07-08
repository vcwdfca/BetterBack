package com.vcwdfca.bb.api2.client.gui.resources.lines;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * Provides directional quest line overrides for Better Questing resource themes.
 */
public interface DirectionalQuestLineTheme {

    /**
     * Returns directional line specs that should be applied to a loaded theme.
     *
     * @param themeId loaded resource theme id
     * @return quest line specs for built-in Better Questing themes, or an empty list for third-party themes
     */
    List<DirectionalQuestLineSpec> getQuestLineSpecs(ResourceLocation themeId);
}

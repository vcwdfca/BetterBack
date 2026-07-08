package com.vcwdfca.bb.api2.client.gui.resources.lines;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Upstream-compatible directional line overrides for built-in Better Questing themes.
 */
public class DirectionalQuestLineThemeImpl implements DirectionalQuestLineTheme {
    private static final String BETTER_QUESTING_NAMESPACE = "betterquesting";
    private static final String BQ_STANDARD_NAMESPACE = "bq_standard";

    private static final List<DirectionalQuestLineSpec> QUEST_LINE_SPECS = createQuestLineSpecs();

    @Override
    public List<DirectionalQuestLineSpec> getQuestLineSpecs(ResourceLocation themeId) {
        Objects.requireNonNull(themeId, "themeId");
        if (!isBuiltInBetterQuestingTheme(themeId)) {
            return Collections.emptyList();
        }
        return QUEST_LINE_SPECS;
    }

    private boolean isBuiltInBetterQuestingTheme(ResourceLocation themeId) {
        String namespace = themeId.getNamespace();
        return BETTER_QUESTING_NAMESPACE.equals(namespace) || BQ_STANDARD_NAMESPACE.equals(namespace);
    }

    private static List<DirectionalQuestLineSpec> createQuestLineSpecs() {
        List<DirectionalQuestLineSpec> specs = new ArrayList<>();
        specs.add(createSpec("quest_locked", 0.4F));
        specs.add(createSpec("quest_unlocked", 0.2F));
        specs.add(createSpec("quest_pending", 0.2F));
        specs.add(createSpec("quest_complete", 0.15F));
        specs.add(createSpec("quest_repeatable", 0.2F));
        return Collections.unmodifiableList(specs);
    }

    private static DirectionalQuestLineSpec createSpec(String path, float arrowOpacity) {
        return new DirectionalQuestLineSpec(
                new ResourceLocation(BETTER_QUESTING_NAMESPACE, path),
                new DependencyArrowSpec(
                        DependencyArrowSpec.DEFAULT_ARROW_WIDTH,
                        DependencyArrowSpec.DEFAULT_ARROW_SIZE,
                        arrowOpacity));
    }
}

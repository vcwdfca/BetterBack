package com.vcwdfca.bb.api2.client.gui.popups;

import betterquesting.api.utils.RenderUtils;
import betterquesting.api2.client.gui.SceneController;
import betterquesting.api2.client.gui.controls.PanelButton;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.CanvasEmpty;
import betterquesting.api2.client.gui.panels.CanvasResizeable;
import betterquesting.api2.client.gui.panels.bars.PanelVScrollBar;
import betterquesting.api2.client.gui.panels.content.PanelGeneric;
import betterquesting.api2.client.gui.panels.lists.CanvasScrolling;
import betterquesting.api2.client.gui.popups.PopContextMenu;
import betterquesting.api2.client.gui.resources.textures.IGuiTexture;
import betterquesting.api2.client.gui.themes.presets.PresetTexture;
import betterquesting.api2.utils.QuestTranslation;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PopContextMenuHoverSub extends PopContextMenu {

    private static final int ITEM_HEIGHT = 16;
    private static final int SCROLL_BAR_WIDTH = 8;
    private static final int MAX_MENU_HEIGHT = 160;
    private static final int TEXT_PADDING = 12;

    private final List<MenuEntry> entries = new ArrayList<>();
    private final GuiRectangle rect;
    private final boolean autoClose;
    private final SubMenuLabelFormatter subMenuLabelFormatter = new SubMenuLabelFormatterImpl();

    private int activeSubIndex = -1;
    private CanvasEmpty subPanel;
    private GuiRectangle subRect;

    public PopContextMenuHoverSub(GuiRectangle rect, boolean autoClose) {
        super(rect, autoClose);
        this.rect = rect;
        this.autoClose = autoClose;
    }

    @Override
    public void addButton(@Nonnull String text, @Nullable IGuiTexture icon, @Nullable Runnable action) {
        entries.add(new MenuEntry(text, icon, action, null));
    }

    public void addSubMenu(@Nonnull String text, @Nonnull List<SubMenuEntry> subEntries) {
        entries.add(new MenuEntry(text, null, null, subEntries));
    }

    @Override
    public void initPanel() {
        super.initPanel();
        resetCanvas();

        rect.w = Math.max(rect.w, getMainMenuWidth());
        rect.h = Math.min(entries.size() * ITEM_HEIGHT, MAX_MENU_HEIGHT);
        if (rect.h <= 0) {
            rect.h = ITEM_HEIGHT;
        }

        int listH = rect.h;
        boolean needsScroll = entries.size() * ITEM_HEIGHT > listH;
        int scrollBarW = needsScroll ? SCROLL_BAR_WIDTH : 0;
        int buttonWidth = rect.w - scrollBarW;

        moveInsideParent(listH);
        CanvasScrolling scroll = addEntryPanels(this, entries, buttonWidth, listH);

        PanelVScrollBar scrollBar = new PanelVScrollBar(
                new GuiRectangle(rect.w - SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH, listH, 0));
        addPanel(scrollBar);
        scroll.setScrollDriverY(scrollBar);
        scrollBar.setEnabled(scroll.getScrollBounds().getHeight() > 0);
    }

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        super.drawPanel(mx, my, partialTick);
        updateSubMenu(mx, my);
        if (subPanel != null && subPanel.isEnabled()) {
            subPanel.drawPanel(mx, my, partialTick);
        }
    }

    @Override
    public boolean onMouseClick(int mx, int my, int click) {
        if (subPanel != null && subPanel.isEnabled() && subRect != null && subRect.contains(mx, my)) {
            return subPanel.onMouseClick(mx, my, click);
        }

        boolean used = super.onMouseClick(mx, my, click);
        if (autoClose && !used && !rect.contains(mx, my)
                && (subRect == null || !subRect.contains(mx, my))
                && SceneController.getActiveScene() != null) {
            SceneController.getActiveScene().closePopup();
            return true;
        }
        return used;
    }

    @Override
    public boolean onMouseRelease(int mx, int my, int click) {
        if (subPanel != null && subPanel.isEnabled() && subRect != null && subRect.contains(mx, my)) {
            return subPanel.onMouseRelease(mx, my, click);
        }
        return super.onMouseRelease(mx, my, click);
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        if (subPanel != null && subPanel.isEnabled() && subRect != null && subRect.contains(mx, my)) {
            return subPanel.onMouseScroll(mx, my, scroll);
        }
        return super.onMouseScroll(mx, my, scroll);
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        if (subPanel != null && subPanel.isEnabled() && subRect != null && subRect.contains(mx, my)) {
            List<String> tooltip = subPanel.getTooltip(mx, my);
            if (tooltip != null) {
                return tooltip;
            }
        }
        return super.getTooltip(mx, my);
    }

    private int getMainMenuWidth() {
        int maxWidth = rect.w;
        for (MenuEntry entry : entries) {
            int iconWidth = entry.icon == null ? 0 : ITEM_HEIGHT;
            int width = iconWidth + RenderUtils.getStringWidth(
                    getEntryLabel(entry),
                    Minecraft.getMinecraft().fontRenderer) + TEXT_PADDING;
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    private void moveInsideParent(int listH) {
        if (getTransform().getParent() == null) {
            return;
        }

        IGuiRect parent = getTransform().getParent();
        rect.x += Math.min(0, parent.getX() + parent.getWidth() - (rect.getX() + rect.getWidth()));
        rect.y += Math.min(0, parent.getY() + parent.getHeight() - (rect.getY() + listH));
    }

    private CanvasScrolling addEntryPanels(CanvasEmpty panel, List<? extends MenuEntry> menuEntries, int buttonWidth,
            int listH) {
        CanvasResizeable background = new CanvasResizeable(
                new GuiRectangle(0, 0, 0, 0, 0),
                PresetTexture.PANEL_INNER.getTexture());
        panel.addPanel(background);
        background.lerpToRect(new GuiRectangle(0, 0, buttonWidth, listH, 0), 100, true);

        CanvasScrolling scroll = new CanvasScrolling(new GuiTransform(GuiAlign.FULL_BOX));
        background.addPanel(scroll);

        for (int i = 0; i < menuEntries.size(); i++) {
            final int entryIndex = i;
            MenuEntry entry = menuEntries.get(i);
            if (entry.icon != null) {
                scroll.addPanel(new PanelGeneric(
                        new GuiRectangle(0, i * ITEM_HEIGHT, ITEM_HEIGHT, ITEM_HEIGHT, 0),
                        entry.icon));
            }

            int textX = entry.icon == null ? 0 : ITEM_HEIGHT;
            int textW = buttonWidth - textX;
            PanelButton button = new PanelButton(
                    new GuiRectangle(textX, i * ITEM_HEIGHT, textW, ITEM_HEIGHT, 0),
                    -1,
                    getEntryLabel(entry));
            if (entry.action != null) {
                button.setClickAction(b -> entry.action.run());
            } else if (entry.subEntries == null || entry.subEntries.isEmpty()) {
                button.setActive(false);
            } else {
                button.setClickAction(b -> updateSubMenu(
                        rect.getX() + 1,
                        rect.getY() + entryIndex * ITEM_HEIGHT + 1));
            }
            scroll.addPanel(button);
        }

        return scroll;
    }

    private void updateSubMenu(int mx, int my) {
        int newActiveIndex = getHoveredSubMenuIndex(mx, my);
        if (newActiveIndex == -1 && activeSubIndex >= 0 && subRect != null && subRect.contains(mx, my)) {
            newActiveIndex = activeSubIndex;
        }

        if (newActiveIndex == activeSubIndex) {
            return;
        }

        closeSubMenu();
        if (newActiveIndex >= 0) {
            showSubMenu(newActiveIndex);
        }
        activeSubIndex = newActiveIndex;
    }

    private int getHoveredSubMenuIndex(int mx, int my) {
        if (!rect.contains(mx, my)) {
            return -1;
        }

        int index = (my - rect.getY()) / ITEM_HEIGHT;
        if (index < 0 || index >= entries.size()) {
            return -1;
        }

        MenuEntry entry = entries.get(index);
        return entry.subEntries == null || entry.subEntries.isEmpty() ? -1 : index;
    }

    private void closeSubMenu() {
        if (subPanel != null) {
            getChildren().remove(subPanel);
            subPanel = null;
            subRect = null;
        }
    }

    private void showSubMenu(int index) {
        MenuEntry entry = entries.get(index);
        if (entry.subEntries == null || entry.subEntries.isEmpty()) {
            return;
        }

        int subW = getSubMenuWidth(entry.subEntries);
        int subH = Math.min(entry.subEntries.size() * ITEM_HEIGHT, MAX_MENU_HEIGHT);
        int subX = rect.getX() + rect.getWidth();
        int subY = rect.getY() + index * ITEM_HEIGHT;

        if (getTransform().getParent() != null) {
            IGuiRect parent = getTransform().getParent();
            int screenRight = parent.getX() + parent.getWidth();
            int screenBottom = parent.getY() + parent.getHeight();
            if (subX + subW > screenRight) {
                subX = rect.getX() - subW;
            }
            if (subY + subH > screenBottom) {
                subY = screenBottom - subH;
            }
        }

        subRect = new GuiRectangle(subX, subY, subW, subH, 0);
        subRect.setParent(getTransform().getParent());
        subPanel = new CanvasEmpty(subRect);
        CanvasScrolling scroll = addEntryPanels(subPanel, entry.subEntries, subW - SCROLL_BAR_WIDTH, subH);

        PanelVScrollBar scrollBar = new PanelVScrollBar(
                new GuiRectangle(subW - SCROLL_BAR_WIDTH, 0, SCROLL_BAR_WIDTH, subH, 0));
        subPanel.addPanel(scrollBar);
        scroll.setScrollDriverY(scrollBar);
        scrollBar.setEnabled(scroll.getScrollBounds().getHeight() > 0);
    }

    private int getSubMenuWidth(List<SubMenuEntry> subEntries) {
        int maxWidth = rect.w;
        for (SubMenuEntry entry : subEntries) {
            int width = RenderUtils.getStringWidth(
                    QuestTranslation.translate(entry.text),
                    Minecraft.getMinecraft().fontRenderer) + TEXT_PADDING;
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth + SCROLL_BAR_WIDTH;
    }

    private String getEntryLabel(MenuEntry entry) {
        if (entry.subEntries == null) {
            return QuestTranslation.translate(entry.text);
        }
        return subMenuLabelFormatter.formatSubMenuLabel(entry.text, QuestTranslation::translate);
    }

    private static class MenuEntry {
        protected final String text;
        protected final IGuiTexture icon;
        protected final Runnable action;
        protected final List<SubMenuEntry> subEntries;

        private MenuEntry(String text, IGuiTexture icon, Runnable action, List<SubMenuEntry> subEntries) {
            this.text = text;
            this.icon = icon;
            this.action = action;
            this.subEntries = subEntries;
        }
    }

    public static class SubMenuEntry extends MenuEntry {

        public SubMenuEntry(@Nonnull String text, @Nullable Runnable action) {
            super(text, null, action, null);
        }
    }
}

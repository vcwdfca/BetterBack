package com.vcwdfca.bb.api2.client.gui.resources.lines;

import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.resources.colors.IGuiColor;
import betterquesting.api2.client.gui.resources.lines.IGuiLine;
import com.vcwdfca.bb.config.DependencyArrowSettings;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

/**
 * Quest dependency line that draws the line body and directional arrows with the same line width.
 */
public class DirectionalLine implements IGuiLine {
    public static final DependencyArrowSpec DEFAULT_SPEC = new DependencyArrowSpec(
            DependencyArrowSpec.DEFAULT_ARROW_WIDTH,
            DependencyArrowSpec.DEFAULT_ARROW_SIZE,
            DependencyArrowSpec.DEFAULT_ARROW_OPACITY);

    private final DependencyArrowSpec spec;
    private final DependencyArrowLayout arrowLayout;

    public DirectionalLine() {
        this(DEFAULT_SPEC, new DependencyArrowLayoutImpl());
    }

    public DirectionalLine(DependencyArrowSpec spec) {
        this(spec, new DependencyArrowLayoutImpl());
    }

    public DirectionalLine(DependencyArrowSpec spec, DependencyArrowLayout arrowLayout) {
        this.spec = Objects.requireNonNull(spec, "spec");
        this.arrowLayout = Objects.requireNonNull(arrowLayout, "arrowLayout");
    }

    public DirectionalLine(IGuiLine baseLine) {
        this(Objects.requireNonNull(baseLine, "baseLine"), DEFAULT_SPEC, new DependencyArrowLayoutImpl());
    }

    public DirectionalLine(IGuiLine baseLine, DependencyArrowSpec spec, DependencyArrowLayout arrowLayout) {
        this(spec, arrowLayout);
        Objects.requireNonNull(baseLine, "baseLine");
    }

    public DependencyArrowSpec getSpec() {
        return spec;
    }

    @Override
    public void drawLine(IGuiRect start, IGuiRect end, int width, IGuiColor color, float partialTick) {
        drawLine(start, end, width, color, partialTick, false);
    }

    public void drawLine(IGuiRect start, IGuiRect end, int width, IGuiColor color, float partialTick, boolean animate) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        LineGeometry geometry = new LineGeometry(
                start.getX() + start.getWidth() / 2F,
                start.getY() + start.getHeight() / 2F,
                end.getX() + end.getWidth() / 2F,
                end.getY() + end.getHeight() / 2F);
        List<DependencyArrow> arrows = arrowLayout.buildArrows(
                geometry,
                spec,
                width,
                DependencyArrowSettings.shouldShowDependencyArrows(),
                animate && DependencyArrowSettings.shouldAnimateDependencyArrows(),
                System.currentTimeMillis());

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.translate(geometry.getStartX(), geometry.getStartY(), 1F);
        GlStateManager.rotate((float) Math.toDegrees(geometry.getAngleRadians()), 0F, 0F, 1F);
        color.applyGlColor();
        GL11.glBegin(GL11.GL_QUADS);
        drawLineBody(geometry.getLength(), width);
        if (!arrows.isEmpty()) {
            GL11.glColor4f(0F, 0F, 0F, spec.getArrowOpacity() * color.getAlpha());
            for (DependencyArrow arrow : arrows) {
                drawArrow(arrow, width);
            }
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();
    }

    private void drawLineBody(float length, int lineWidth) {
        float halfLine = lineWidth / 2F;

        GL11.glVertex2f(0F, halfLine);
        GL11.glVertex2f(length, halfLine);
        GL11.glVertex2f(length, -halfLine);
        GL11.glVertex2f(0F, -halfLine);
    }

    private void drawArrow(DependencyArrow arrow, int lineWidth) {
        float arrowX = arrow.getCenterX();
        float arrowWidth = arrow.getWidth();
        float arrowSize = arrow.getSize();
        float halfLine = lineWidth / 2F;

        GL11.glVertex2f(arrowX - arrowWidth / 2F, halfLine);
        GL11.glVertex2f(arrowX + arrowWidth / 2F, halfLine);
        GL11.glVertex2f(arrowX + arrowSize + arrowWidth / 2F, 0F);
        GL11.glVertex2f(arrowX + arrowSize - arrowWidth / 2F, 0F);

        GL11.glVertex2f(arrowX - arrowWidth / 2F, -halfLine);
        GL11.glVertex2f(arrowX + arrowSize - arrowWidth / 2F, 0F);
        GL11.glVertex2f(arrowX + arrowSize + arrowWidth / 2F, 0F);
        GL11.glVertex2f(arrowX + arrowWidth / 2F, -halfLine);
    }
}

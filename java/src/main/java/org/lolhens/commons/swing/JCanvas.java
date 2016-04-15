package org.lolhens.commons.swing;

import org.lolhens.commons.swing.renderer.ExceptionRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by u016595 on 01.04.2016.
 */
public class JCanvas extends JComponent {
    private CachedGraphics cachedGraphics = null;
    private org.lolhens.commons.swing.renderer.Renderer renderer;
    private int lastWidth, lastHeight;

    public JCanvas() {
    }

    public JCanvas draw(org.lolhens.commons.swing.renderer.Renderer renderer) {
        this.renderer = renderer;
        this.cachedGraphics = null;
        repaint();

        return this;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int width = getWidth();
        int height = getHeight();

        if (renderer != null) {
            graphics.setColor(Color.BLACK);

            if (cachedGraphics == null || lastWidth != width || lastHeight != height) {
                CachedGraphics cachedGraphics = new CachedGraphics(graphics.getColor(), graphics.getFont(), graphics::getFontMetrics);

                renderer.withFallback(ExceptionRenderer::new).render(cachedGraphics, 0, 0, width - 1, height - 1);

                this.cachedGraphics = cachedGraphics;
            }

            cachedGraphics.apply(graphics);
        }

        lastWidth = width;
        lastHeight = height;
    }

    @Override
    public void repaint() {
        super.repaint();
    }
}

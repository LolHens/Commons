package org.lolhens.commons.swing.renderer.primitives.util;

import org.lolhens.commons.swing.renderer.primitives.Text;

import java.awt.*;

/**
 * Created by u016595 on 07.04.2016.
 */
public class CenteredText extends Text {
    //0 = center, 1 = left, 2 = right
    private final int alignmentX;
    //0 = center, 1 = top, 2 = bottom
    private final int alignmentY;

    public CenteredText(String text, int alignmentX, int alignmentY) {
        super(text);
        this.alignmentX = alignmentX;
        this.alignmentY = alignmentY;
    }

    public CenteredText(String text) {
        this(text, 0, 0);
    }

    @Override
    public void render(Graphics graphics, double x, double y, double width, double height) {
        if (text != null) {
            FontMetrics fontMetrics = graphics.getFontMetrics();

            String[] lines = text.split("\n");

            int maxWidth = 0;
            int maxHeight = fontMetrics.getHeight() * lines.length;

            for (String line : lines)
                maxWidth = Math.max(maxWidth, fontMetrics.stringWidth(line));

            double tX = 0;
            if (alignmentX == 0)
                tX = x + width / 2 - maxWidth / 2;
            else if (alignmentX == 1)
                tX = x;
            else if (alignmentX == 2)
                tX = x + width - maxWidth;

            double tY = 0;
            if (alignmentY == 0)
                tY = y + height / 2 - maxHeight / 2;
            else if (alignmentY == 1)
                tY = y;
            else if (alignmentY == 2)
                tY = y + height - maxHeight;

            super.render(graphics, tX, tY, 0, 0);
        }
    }
}

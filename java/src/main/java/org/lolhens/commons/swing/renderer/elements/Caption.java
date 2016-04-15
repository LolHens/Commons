package org.lolhens.commons.swing.renderer.elements;

import org.lolhens.commons.swing.renderer.Renderer;
import org.lolhens.commons.swing.renderer.primitives.GetFontMetrics;
import org.lolhens.commons.swing.renderer.primitives.util.CenteredText;

import java.awt.*;

/**
 * Created by u016595 on 08.04.2016.
 */
public class Caption extends Anchored {
    private final String text;

    public Caption(String text, Renderer renderer) {
        super(new CenteredText(text), 0, 0, renderer);
        this.text = text;
    }

    @Override
    public Renderer render(Renderer renderer) {
        GetFontMetrics getFontMetrics = new GetFontMetrics();

        return getFontMetrics.with(Renderer.empty().transform((x, y, width, height) -> {
            FontMetrics fontMetrics = getFontMetrics.get();

            String[] lines = text.split("\n");

            int maxWidth = 0;
            int maxHeight = fontMetrics.getHeight() * lines.length;

            for (String line : lines)
                maxWidth = Math.max(maxWidth, fontMetrics.stringWidth(line));

            this.width = (double) maxWidth / width;
            this.height = (double) maxHeight / height;

            return null;
        }), super.render(renderer));
    }
}

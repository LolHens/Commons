package org.lolhens.commons.swing.renderer.primitives;

import java.awt.*;
import java.text.AttributedCharacterIterator;

/**
 * Created by u016595 on 05.04.2016.
 */
public class Text extends Drawable {
    protected final String text;

    private final AttributedCharacterIterator charIterator;

    public Text(String text) {
        this.text = text;

        this.charIterator = null;
    }

    public Text(AttributedCharacterIterator charIterator) {
        this.text = null;

        this.charIterator = charIterator;
    }

    @Override
    public void render(Graphics graphics, double x, double y, double width, double height) {
        if (text != null) {
            int charHeight = graphics.getFontMetrics().getHeight();

            int i = 1;
            for (String line : text.split("\n"))
                graphics.drawString(line, (int) Math.round(x), (int) Math.round(y) + charHeight * i++);
        } else if (charIterator != null)
            graphics.drawString(charIterator, (int) Math.round(x), (int) Math.round(y));
    }
}

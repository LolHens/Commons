package org.lolhens.commons.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class GetFontMetrics extends Primitive<FontMetrics> {
    private final Font font;

    public GetFontMetrics(Font font) {
        this.font = font;
    }

    public GetFontMetrics() {
        this(null);
    }

    @Override
    public FontMetrics apply(Graphics graphics) {
        return font != null ? graphics.getFontMetrics(font) : graphics.getFontMetrics();
    }
}

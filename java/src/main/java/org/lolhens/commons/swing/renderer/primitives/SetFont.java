package org.lolhens.commons.swing.renderer.primitives;

import org.lolhens.commons.future.Future;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class SetFont extends Setter<Font> {
    public SetFont(Future<Font> future) {
        super(future);
    }

    public SetFont(Font font) {
        super(font);
    }

    @Override
    public void apply(Graphics graphics, Font value) {
        graphics.setFont(value);
    }
}

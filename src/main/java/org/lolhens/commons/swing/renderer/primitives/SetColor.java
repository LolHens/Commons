package org.lolhens.commons.swing.renderer.primitives;

import org.lolhens.commons.future.Future;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class SetColor extends Setter<Color> {
    public SetColor(Future<Color> future) {
        super(future);
    }

    public SetColor(Color color) {
        super(color);
    }

    @Override
    public void apply(Graphics graphics, Color value) {
        graphics.setColor(value);
    }
}

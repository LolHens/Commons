package org.lolhens.commons.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class GetColor extends Primitive<Color> {
    @Override
    public Color apply(Graphics graphics) {
        return graphics.getColor();
    }
}

package org.lolhens.commons.swing.renderer;

import org.lolhens.commons.swing.renderer.primitives.Rect;
import org.lolhens.commons.swing.renderer.primitives.SetColor;

import java.awt.*;

/**
 * Created by u016595 on 04.04.2016.
 */
public class BackgroundRenderer implements Renderer {
    public final Color color;

    public BackgroundRenderer(Color color) {
        this.color = color;
    }

    @Override
    public Renderer render(Renderer renderer) {
        return new SetColor(color).with(new Rect(true));
    }
}

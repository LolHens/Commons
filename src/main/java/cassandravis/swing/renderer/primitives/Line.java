package cassandravis.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class Line extends Drawable {
    @Override
    public void render(Graphics graphics, double x, double y, double width, double height) {
        graphics.drawLine((int) Math.round(x), (int) Math.round(y), (int) Math.round(x + width), (int) Math.round(y + height));
    }
}

package cassandravis.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class Rect extends Drawable {
    private final boolean filled;

    public Rect(boolean filled) {
        this.filled = filled;
    }

    @Override
    public void render(Graphics graphics, double x, double y, double width, double height) {
        if (width < 0) {
            x += width;
            width *= -1;
        }

        if (height < 0) {
            y += height;
            height *= -1;
        }

        if (filled)
            graphics.fillRect((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
        else
            graphics.drawRect((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
    }
}

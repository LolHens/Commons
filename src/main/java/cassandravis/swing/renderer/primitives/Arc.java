package cassandravis.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class Arc extends Drawable {
    private final boolean filled;
    private final int startAngle, arcAngle;

    public Arc(boolean filled, double startAngle, double arcAngle) {
        this.filled = filled;
        this.startAngle = (int) Math.round(startAngle);
        this.arcAngle = (int) Math.round(arcAngle);
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
            graphics.fillArc((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height), startAngle, arcAngle - startAngle);
        else
            graphics.drawArc((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height), startAngle, arcAngle - startAngle);
    }
}

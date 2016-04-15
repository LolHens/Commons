package cassandravis.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public abstract class Drawable extends Primitive<Void> {
    @Override
    public abstract void render(Graphics graphics, double x, double y, double width, double height);

    @Override
    public Void apply(Graphics graphics) {
        return null;
    }
}

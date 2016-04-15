package cassandravis.swing.renderer.primitives;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class GetFont extends Primitive<Font> {
    @Override
    public Font apply(Graphics graphics) {
        return graphics.getFont();
    }
}

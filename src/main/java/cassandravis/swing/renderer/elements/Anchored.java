package cassandravis.swing.renderer.elements;

import cassandravis.swing.renderer.Renderer;

/**
 * Created by u016595 on 08.04.2016.
 */
public class Anchored implements Renderer {
    protected volatile Renderer caption;
    protected volatile double width, height;
    protected final Renderer renderer;

    // 0 = above, 1 = below, 2 = left, 3 = right
    protected volatile int position;

    public Anchored(Renderer caption, double width, double height, Renderer renderer) {
        this.caption = caption;
        this.width = width;
        this.height = height;
        this.renderer = renderer;
    }

    public Anchored above() {
        position = 0;
        return this;
    }

    public Anchored below() {
        position = 1;
        return this;
    }

    public Anchored left() {
        position = 2;
        return this;
    }

    public Anchored right() {
        position = 3;
        return this;
    }

    @Override
    public Renderer render(Renderer renderer) {
        Renderer.Builder builder = Renderer.builder();

        return builder.build().transform((x, y, w, h) -> {
            double tX = position == 3 ? 1 - width : 0;
            double tY = position == 1 ? 1 - height : 0;
            double tW = position == 2 || position == 3 ? width : 1;
            double tH = position == 0 || position == 1 ? height : 1;
            double rX = position == 2 ? width : 0;
            double rY = position == 0 ? height : 0;
            double rW = position == 2 || position == 3 ? 1 - width : 1;
            double rH = position == 0 || position == 1 ? 1 - height : 1;

            builder.append(caption.transform(tX, tY, tW, tH));
            builder.append(Anchored.this.renderer.transform(rX, rY, rW, rH));

            return null;
        });
    }
}

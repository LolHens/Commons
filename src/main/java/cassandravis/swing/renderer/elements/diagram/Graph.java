package cassandravis.swing.renderer.elements.diagram;

import cassandravis.swing.renderer.Renderer;
import cassandravis.swing.renderer.primitives.GetFontMetrics;
import cassandravis.swing.renderer.primitives.Line;
import cassandravis.swing.renderer.primitives.SetColor;
import cassandravis.swing.renderer.primitives.util.CenteredText;
import cassandravis.util.math.Math2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Created by u016595 on 07.04.2016.
 */
public abstract class Graph extends Chart {
    private final double minX;
    private final double maxX;
    private final double minY;
    private final double maxY;

    public Graph(Double minX, Double maxX, Double minY, Double maxY, List<ChartVal> values) {
        super(values);

        if (minX == null || maxX == null || minY == null || maxY == null) {
            double newMinX = Double.POSITIVE_INFINITY;
            double newMaxX = Double.NEGATIVE_INFINITY;
            double newMinY = Double.POSITIVE_INFINITY;
            double newMaxY = Double.NEGATIVE_INFINITY;

            for (ChartVal value : getValues()) {
                double x = value.getX();
                double y = value.getY();

                if (x < newMinX) newMinX = x;
                if (x > newMaxX) newMaxX = x;
                if (y < newMinY) newMinY = y;
                if (y > newMaxY) newMaxY = y;
            }

            if (minX == null) minX = newMinX;
            if (maxX == null) maxX = newMaxX;
            if (minY == null) minY = Math.min(newMinY, 0);
            if (maxY == null) maxY = Math.max(newMaxY, 0);
        }

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public Graph(List<ChartVal> values) {
        this(null, null, null, null, values);
    }

    public Graph(double minX, double maxX, Double minY, Double maxY, double resolution, DoubleFunction<ChartVal> function) {
        this(minX, maxX, minY, maxY, calculate(minX, maxX, resolution, function));
    }

    public Graph(double maxX, double resolution, DoubleFunction<ChartVal> function) {
        this(0, maxX, null, null, resolution, function);
    }


    protected double getMinX() {
        return minX;
    }

    protected double getMaxX() {
        return maxX;
    }

    protected double getMinY() {
        return minY;
    }

    protected double getMaxY() {
        return maxY;
    }

    protected double getY0() {
        return -getMinY() / (getMaxY() - getMinY());
    }


    static List<ChartVal> calculate(double minX, double maxX, double resolution, DoubleFunction<ChartVal> function) {
        List<ChartVal> values = new ArrayList<>();
        int max = (int) ((maxX - minX) / resolution);

        for (int i = 0; i < max; i++) {
            double x = ((double) i) * resolution;
            ChartVal value = function.apply(x);
            values.add(value);
        }

        return values;
    }


    public XAxisRenderer xAxisRenderer(Color color) {
        return new XAxisRenderer(color);
    }

    public XAxisRenderer xAxisRenderer() {
        return xAxisRenderer(null);
    }

    public class XAxisRenderer implements Renderer {
        private final Color color;

        public XAxisRenderer(Color color) {
            this.color = color;
        }

        @Override
        public Renderer render(Renderer renderer) {
            Renderer.Builder builder = Renderer.builder();

            if (color != null) builder.append(new SetColor(color));
            builder.append(new Line().transform(0, getY0(), 1, 0));

            return builder.build().mirror(false, true);
        }
    }

    public final TextRenderers textRenderers = new TextRenderers();

    public class TextRenderers extends Chart.TextRenderers {
        public AtYAxis atYAxis(double scale, boolean autoMirror) {
            return new AtYAxis(scale, autoMirror);
        }

        public AtYAxis atYAxis() {
            return atYAxis(1, false);
        }

        public class AtYAxis implements Renderer {
            private final double scale;
            private final boolean autoMirror;

            public AtYAxis(double scale, boolean autoMirror) {
                this.scale = scale;
                this.autoMirror = autoMirror;
            }

            @Override
            public Renderer render(Renderer renderer) {
                Renderer.Builder builder = Renderer.builder();

                return builder.build().transform((oldX, oldY, oldWidth, oldHeight) -> {
                    double spaceWidth = 1 / size();

                    for (ChartVal value : getValues()) {
                        double x = spaceWidth / 2 + (spaceWidth) * value.getX();
                        double y = (value.getY() - getMinY()) / (getMaxY() - getMinY());

                        builder.append(new SetColor(value.getColor() == null ? null : value.getColor().darker()));

                        GetFontMetrics fontMetrics = new GetFontMetrics();
                        builder.append(fontMetrics);
                        builder.append(new CenteredText(value.getName(), 0, !autoMirror ? 2 : 0).transform(x, getY0(), 0, 0).transform((x1, y1, width1, height1) ->
                                new cassandravis.swing.renderer.Rectangle(x1, y1 + (fontMetrics.get().getHeight() * (autoMirror ? y - getY0() <= 0 ? 1 : -1 : 1) * (scale - 0.5)) * Math2.abs1(oldHeight) - 4, width1, height1)));
                    }

                    return null;
                }).mirror(false, true);
            }
        }
    }
}

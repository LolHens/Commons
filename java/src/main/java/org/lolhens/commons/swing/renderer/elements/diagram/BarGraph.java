package org.lolhens.commons.swing.renderer.elements.diagram;

import org.lolhens.commons.collection.Collections2;
import org.lolhens.commons.math.Math2;
import org.lolhens.commons.swing.renderer.Rectangle;
import org.lolhens.commons.swing.renderer.Renderer;
import org.lolhens.commons.swing.renderer.primitives.GetFontMetrics;
import org.lolhens.commons.swing.renderer.primitives.Rect;
import org.lolhens.commons.swing.renderer.primitives.SetColor;
import org.lolhens.commons.swing.renderer.primitives.util.CenteredText;

import java.awt.*;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.stream.Collectors;

/**
 * Created by u016595 on 04.04.2016.
 */
public class BarGraph extends Graph {
    private double spaceShare = 0.1d;
    private Double maxBarWidth = null;


    public BarGraph(Double minY, Double maxY, List<ChartVal> values) {
        super(0d, 0d, minY, maxY, values);
    }

    public BarGraph(List<ChartVal> values) {
        super(values);
    }

    public BarGraph(double minX, double maxX, Double minY, Double maxY, double resolution, DoubleFunction<ChartVal> function) {
        super(minX, maxX, minY, maxY, resolution, function);
    }

    public BarGraph(double maxX, double resolution, DoubleFunction<ChartVal> function) {
        super(maxX, resolution, function);
    }

    @Override
    protected List<ChartVal> normalize(List<ChartVal> values) {
        List<ChartVal> sorted = super.normalize(values).stream()
                .sorted((a, b) -> (int) Math.round(a.getX() - b.getX()))
                .collect(Collectors.toList());
        List<ChartVal> normalized = Collections2.List();

        int i = 0;
        for (ChartVal value : sorted)
            normalized.add(value.copy(null, (double) i++, null, null));

        return normalized;
    }


    public BarGraph spaceShare(double spaceShare) {
        this.spaceShare = spaceShare;
        return this;
    }

    public BarGraph maxBarWidth(Double maxBarWidth) {
        this.maxBarWidth = maxBarWidth;
        return this;
    }


    public BarsRenderer barsRenderer() {
        return new BarsRenderer();
    }

    public class BarsRenderer implements Renderer {
        @Override
        public Renderer render(Renderer renderer) {
            Renderer.Builder builder = Renderer.builder();

            return builder.build().transform((oldX, oldY, oldWidth, oldHeight) -> {
                double barWidth = (1 - spaceShare) / size();

                if (maxBarWidth != null) barWidth = Math.min(barWidth, maxBarWidth / oldWidth);

                double spaceWidth = (1 - barWidth * size()) / (size() + 1);


                int i = 0;
                for (ChartVal value : getValues()) {
                    double x = spaceWidth + (barWidth + spaceWidth) * (int) value.getX();
                    double y = (value.getY() - getMinY()) / (getMaxY() - getMinY());

                    builder.append(new SetColor(value.getColor()));
                    builder.append(new Rect(true).transform(x, getY0(), barWidth, y - getY0()));

                    i++;
                }

                return null;
            }).mirror(false, true);
        }
    }


    public GraphRenderer graphRenderer(Color xAxis) {
        return new GraphRenderer(xAxis);
    }

    public GraphRenderer graphRenderer() {
        return graphRenderer(null);
    }

    public class GraphRenderer implements Renderer {
        private final BarsRenderer barsRenderer;
        private final XAxisRenderer xAxisRenderer;

        public GraphRenderer(Color xAxis) {
            this.barsRenderer = barsRenderer();
            this.xAxisRenderer = xAxisRenderer(xAxis);
        }

        @Override
        public Renderer render(Renderer renderer) {
            return barsRenderer.with(xAxisRenderer).with(textRenderers.aboveBars());
        }
    }


    public final TextRenderers textRenderers = new TextRenderers();

    public class TextRenderers extends Graph.TextRenderers {
        public AboveBars aboveBars(double offset) {
            return new AboveBars(offset);
        }

        public AboveBars aboveBars() {
            return aboveBars(1);
        }

        public class AboveBars implements Renderer {
            private final double offset;

            public AboveBars(double offset) {
                this.offset = offset;
            }

            @Override
            public Renderer render(Renderer renderer) {
                Renderer.Builder builder = Renderer.builder();

                return builder.build().transform((oldX, oldY, oldWidth, oldHeight) -> {
                    double barWidth = (1 - spaceShare) / size();

                    if (maxBarWidth != null) barWidth = Math.min(barWidth, maxBarWidth / oldWidth);

                    double spaceWidth = (1 - barWidth * size()) / (size() + 1);

                    for (ChartVal value : getValues()) {
                        double x = spaceWidth + (barWidth + spaceWidth) * value.getX();
                        double y = (value.getY() - getMinY()) / (getMaxY() - getMinY());

                        builder.append(new SetColor(value.getColor() == null ? null : value.getColor().darker()));

                        GetFontMetrics fontMetrics = new GetFontMetrics();
                        builder.append(fontMetrics);
                        builder.append(new CenteredText(value.getName()).transform(x, y, barWidth, 0).transform((x1, y1, width1, height1) ->
                                new Rectangle(x1, y1 + (fontMetrics.get().getHeight() * (y - getY0() <= 0 ? 1 : -1) * (offset - 0.5)) * Math2.abs1(oldHeight) - 4, width1, height1)));
                    }

                    return null;
                }).mirror(false, true);
            }
        }
    }
}

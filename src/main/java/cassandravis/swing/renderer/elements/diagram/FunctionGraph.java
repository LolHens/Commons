package cassandravis.swing.renderer.elements.diagram;

import cassandravis.swing.renderer.Renderer;
import cassandravis.swing.renderer.primitives.Line;
import cassandravis.swing.renderer.primitives.SetColor;

import java.awt.*;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Created by u016595 on 04.04.2016.
 */
public class FunctionGraph extends Graph {
    public FunctionGraph(Double minX, Double maxX, Double minY, Double maxY, List<ChartVal> values) {
        super(minX, maxX, minY, maxY, values);
    }

    public FunctionGraph(List<ChartVal> values) {
        super(values);
    }

    public FunctionGraph(double minX, double maxX, Double minY, Double maxY, double resolution, DoubleFunction<ChartVal> function) {
        super(minX, maxX, minY, maxY, resolution, function);
    }

    public FunctionGraph(double maxX, double resolution, DoubleFunction<ChartVal> function) {
        super(maxX, resolution, function);
    }


    public LineRenderer lineRenderer() {
        return new LineRenderer();
    }

    public class LineRenderer implements Renderer {
        @Override
        public Renderer render(Renderer renderer) {
            Renderer.Builder builder = Renderer.builder();

            boolean first = true;
            double lastX = 0;
            double lastY = 0;
            Color lastColor = null;

            for (ChartVal value : getValues()) {
                double x = (value.getX() - getMinX()) / (getMaxX() - getMinX());
                double y = (value.getY() - getMinY()) / (getMaxY() - getMinY());

                if (first)
                    first = false;
                else {
                    if (lastColor != null) builder.append(new SetColor(lastColor));
                    builder.append(new Line().transform(lastX, lastY, x - lastX, y - lastY));
                }

                lastX = x;
                lastY = y;
                lastColor = value.getColor();
            }

            return builder.build().mirror(false, true);
        }
    }


    public GraphRenderer graphRenderer(Color xAxis) {
        return new GraphRenderer(xAxis);
    }

    public GraphRenderer graphRenderer() {
        return graphRenderer(null);
    }

    public class GraphRenderer implements Renderer {
        private final Color xAxis;

        public GraphRenderer(Color xAxis) {
            this.xAxis = xAxis;
        }

        @Override
        public Renderer render(Renderer renderer) {
            return lineRenderer().with(xAxisRenderer(xAxis));
        }
    }
}

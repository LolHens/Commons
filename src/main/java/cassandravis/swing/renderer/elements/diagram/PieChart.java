package cassandravis.swing.renderer.elements.diagram;

import cassandravis.swing.renderer.Rectangle;
import cassandravis.swing.renderer.Renderer;
import cassandravis.swing.renderer.primitives.Arc;
import cassandravis.swing.renderer.primitives.SetColor;
import cassandravis.swing.renderer.primitives.util.CenteredText;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by u016595 on 04.04.2016.
 */
public class PieChart extends Chart {
    public PieChart(List<ChartVal> values) {
        super(values);
    }

    @Override
    protected List<ChartVal> normalize(List<ChartVal> values) {
        List<ChartVal> superNormalized = super.normalize(values);

        final double sum = superNormalized.stream()
                .map(ChartVal::getY)
                .reduce(0d, (last, e) -> last + e);

        return superNormalized.stream()
                .map((e) -> e.copy(null, null, (double) Math.round(e.getY() / sum * 360d), null))
                .collect(Collectors.toList());
    }


    private static final Renderer aspectRatio(Renderer renderer) {
        return renderer.transform((x, y, width, height) -> {
            double size = Math.min(width, height);
            double newX = (width - size) / 2;
            double newY = (height - size) / 2;
            return new Rectangle(newX, newY, size, size);
        });
    }


    public PieceRenderer pieceRenderer(int index) {
        return index >= size() ? null : new PieceRenderer(index);
    }

    public class PieceRenderer implements Renderer {
        private final int index;

        public PieceRenderer(int index) {
            this.index = index;
        }

        @Override
        public Renderer render(Renderer renderer) {
            double sum = 0;

            Iterator<ChartVal> iterator = getValues().iterator();
            ChartVal value = null;

            int i = 0;
            while (i++ <= index) {
                if (!iterator.hasNext()) return null;
                value = iterator.next();
                if (i <= index) sum += value.getY();
            }

            Renderer.Builder builder = Renderer.builder();

            double y = iterator.hasNext() ? sum + value.getY() : 360d;

            builder.append(new SetColor(value.getColor()));
            builder.append(new Arc(true, sum, y));

            builder.append(new SetColor(value.getColor() != null ? value.getColor().darker() : null));
            builder.append(new Arc(false, sum, y));

            return aspectRatio(builder.build());
        }
    }


    public ChartRenderer chartRenderer() {
        return new ChartRenderer();
    }

    public class ChartRenderer implements Renderer {
        @Override
        public Renderer render(Renderer renderer) {
            Renderer.Builder builder = Renderer.builder();

            for (int i = 0; i < size(); i++)
                builder.append(pieceRenderer(i));

            return builder.build().with(textRenderers.insidePieces());
        }
    }


    public final TextRenderers textRenderers = new TextRenderers();

    public class TextRenderers extends Chart.TextRenderers {
        public InsidePieces insidePieces() {
            return new InsidePieces();
        }

        public class InsidePieces implements Renderer {
            @Override
            public Renderer render(Renderer renderer) {
                Renderer.Builder builder = Renderer.builder();

                double angle = 0, lastAngle;
                for (ChartVal value : getValues()) {
                    lastAngle = angle;
                    angle += value.getY();

                    double rad = Math.toRadians(lastAngle + (angle - lastAngle) / 2d + 90);

                    builder.append(new CenteredText(value.getName()).transform(0.5d + Math.sin(rad) * 0.35d, 0.5d + Math.cos(rad) * 0.35d, 0, 0));
                }

                return aspectRatio(builder.build());
            }
        }
    }
}

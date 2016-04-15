package org.lolhens.commons.swing.renderer.elements.diagram;

import org.lolhens.commons.collection.Collections2;
import org.lolhens.commons.hash.HashBuilder;
import org.lolhens.commons.schema.ColorSchema;
import org.lolhens.commons.swing.renderer.Renderer;
import org.lolhens.commons.swing.renderer.primitives.GetFontMetrics;
import org.lolhens.commons.swing.renderer.primitives.Rect;
import org.lolhens.commons.swing.renderer.primitives.SetColor;
import org.lolhens.commons.swing.renderer.primitives.util.CenteredText;

import java.awt.*;
import java.util.List;

/**
 * Created by u016595 on 07.04.2016.
 */
public abstract class Chart {
    private final List<ChartVal> values;
    private final ColorSchema colorSchema;

    public Chart(List<ChartVal> values) {
        this.colorSchema = ColorSchema.random();
        this.values = normalize(values);
    }

    protected List<ChartVal> normalize(List<ChartVal> values) {
        List<ChartVal> chartVals = Collections2.List();

        int i = 0;
        for (ChartVal value : values)
            if (value.getColor() == null)
                chartVals.add(value.copy(null, null, null, colorSchema.get(i++)));
            else
                chartVals.add(value);

        return chartVals;
    }

    protected List<ChartVal> getValues() {
        return values;
    }

    public double size() {
        return values.size();
    }

    @Override
    public int hashCode() {
        HashBuilder builder = new HashBuilder();

        for (ChartVal value : values)
            builder.append(value.hashCode());

        return builder.build();
    }


    public final TextRenderers textRenderers = new TextRenderers();

    public class TextRenderers {
        public GraphCaption graphCaption() {
            return new GraphCaption();
        }

        public class GraphCaption implements Renderer {
            @Override
            public Renderer render(Renderer renderer) {
                Builder builder = Renderer.builder();
                //skalierung der Legende


                double startx = 0;
                double starty = 0;
                double startb = 0.25;
                double starth = 1;
                builder.append(new SetColor(Color.white));
                builder.append(new Rect(true).transform(startx, starty, 1, starth).transformAbsolute(null, null, 150d, null));
                double anzahl = (1d / (double) size());
                double x = 0;
                double y = 0;

                int i = 0;
                for (ChartVal value : getValues()) {
                    if (value.getColor() == null) {
                        builder.append(new SetColor(Color.blue));

                    } else {
                        builder.append(new SetColor(value.getColor()));
                    }
                    builder.append(new Rect(true).transform(x, y, 1, anzahl - 0.01).transformAbsolute(null, null, 10d, null));//(x,y,Breite,höhe)

                    builder.append(new SetColor(Color.BLACK));
                    builder.append(new CenteredText(value.getName(), 1, 0).transform(0, y, 0.1, anzahl - 0.01).transformAbsolute(15d, null));
                    y = y + anzahl;

                    i++;
                }

                GetFontMetrics getFontMetrics = new GetFontMetrics();

                return getFontMetrics.with(builder.build().transform((x1, y1, width, height) ->
                        new org.lolhens.commons.swing.renderer.Rectangle(x1, y1, width, getFontMetrics.get().getHeight() * (values.size()))));
            }
        }
    }
}

package org.lolhens.commons.swing.renderer;

import org.lolhens.commons.collection.Collections2;
import org.lolhens.commons.math.Math2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by u016595 on 04.04.2016.
 */
@FunctionalInterface
public interface Renderer {
    default void render(Graphics graphics, double x, double y, double width, double height) {
        Renderer renderer = render(this);
        if (renderer != null) renderer.render(graphics, x, y, width, height);
    }

    Renderer render(Renderer renderer);

    default Renderer transform(Transformer transformer) {
        return new TransformationRenderer(this, transformer);
    }

    default Renderer transform(double x, double y, double width, double height) {
        return transform((oldX, oldY, oldWidth, oldHeight) -> new Rectangle(oldX + oldWidth * x, oldY + oldHeight * y, width * oldWidth, height * oldHeight));
    }

    default Renderer transformAbsolute(Double x, Double y, Double width, Double height) {
        return transform((oldX, oldY, oldWidth, oldHeight) -> new Rectangle(
                x != null ? oldX + x * Math2.abs1(oldWidth) : oldX,
                y != null ? oldY + y * Math2.abs1(oldHeight) : oldY,
                width != null ? width * Math2.abs1(oldWidth) : oldWidth,
                height != null ? height * Math2.abs1(oldHeight) : oldHeight));
    }

    default Renderer transformAbsolute(Double x, Double y) {
        return transformAbsolute(x, y, null, null);
    }

    default Renderer mirror(boolean x, boolean y) {
        return transform(x ? 1 : 0, y ? 1 : 0, x ? -1 : 1, y ? -1 : 1);
    }

    default Renderer with(Renderer... renderers) {
        Renderer[] newRenderers = new Renderer[renderers.length + 1];

        newRenderers[0] = this;

        System.arraycopy(renderers, 0, newRenderers, 1, renderers.length);

        return new CompoundRenderer(newRenderers);
    }

    default Renderer withFallback(Function<Exception, Renderer> renderer) {
        return new FallbackRenderer(this, renderer);
    }


    class TransformationRenderer implements Renderer {
        private final Renderer renderer;
        private final Transformer transformer;

        public TransformationRenderer(Renderer renderer, Transformer transformer) {
            this.renderer = renderer;
            this.transformer = transformer;
        }

        @Override
        public void render(Graphics graphics, double x, double y, double width, double height) {
            Rectangle rectangle = transformer.transform(x, y, width, height);

            if (rectangle != null)
                renderer.render(graphics, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
            else
                renderer.render(graphics, x, y, width, height);
        }

        @Override
        public Renderer render(Renderer renderer) {
            return this;
        }
    }


    class CompoundRenderer implements Renderer {
        private final List<Renderer> rendererList;

        private CompoundRenderer(List<Renderer> rendererList) {
            this.rendererList = rendererList;
        }

        public CompoundRenderer(Renderer... renderer) {
            this(Collections2.List(renderer));
        }

        @Override
        public void render(Graphics graphics, double x, double y, double width, double height) {
            rendererList.stream().filter(renderer -> renderer != null).forEachOrdered(renderer -> renderer.render(graphics, x, y, width, height));
        }

        @Override
        public Renderer render(Renderer renderer) {
            return renderer;
        }
    }


    class FallbackRenderer implements Renderer {
        private final Renderer renderer;
        private final Function<Exception, Renderer> fallback;

        public FallbackRenderer(Renderer renderer, Function<Exception, Renderer> fallback) {
            this.renderer = renderer;
            this.fallback = fallback;
        }

        @Override
        public void render(Graphics graphics, double x, double y, double width, double height) {
            try {
                renderer.render(graphics, x, y, width, height);
            } catch (Exception exception) {
                fallback.apply(exception).render(graphics, x, y, width, height);
            }
        }

        @Override
        public Renderer render(Renderer renderer) {
            return this;
        }
    }


    static Renderer empty() {
        return new Empty();
    }

    class Empty implements Renderer {
        @Override
        public Renderer render(Renderer renderer) {
            return null;
        }
    }


    static Builder builder() {
        return new Builder();
    }

    class Builder {
        private final List<Renderer> rendererList = new ArrayList<>();

        public Builder append(Renderer renderer) {
            rendererList.add(renderer);
            return this;
        }

        public Renderer build() {
            return new CompoundRenderer(rendererList);
        }
    }
}

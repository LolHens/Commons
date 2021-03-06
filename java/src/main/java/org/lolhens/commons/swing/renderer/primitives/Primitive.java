package org.lolhens.commons.swing.renderer.primitives;

import org.lolhens.commons.future.Future;
import org.lolhens.commons.future.Promise;
import org.lolhens.commons.swing.renderer.Renderer;

import java.awt.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public abstract class Primitive<T> implements Renderer, Future<T> {
    private Promise<T> promise = new Promise<>();

    @Override
    public void render(Graphics graphics, double x, double y, double width, double height) {
        T result = apply(graphics);

        promise.complete(result);
    }

    @Override
    public Renderer render(Renderer renderer) {
        return this;
    }

    public abstract T apply(Graphics graphics);

    public T get() {
        return promise.get();
    }

    public static Primitive<Void> empty() {
        return new Empty();
    }

    public static class Empty extends Primitive<Void> {
        @Override
        public Void apply(Graphics graphics) {
            return null;
        }
    }
}

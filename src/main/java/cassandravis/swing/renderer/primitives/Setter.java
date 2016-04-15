package cassandravis.swing.renderer.primitives;

import cassandravis.util.future.Future;
import cassandravis.util.future.Promise;

import java.awt.*;

/**
 * Created by u016595 on 07.04.2016.
 */
public abstract class Setter<T> extends Primitive<Void> {
    private final Future<T> future;

    public Setter(Future<T> future) {
        this.future = future;
    }

    public Setter(T value) {
        this(new Promise<>(value));
    }

    @Override
    public Void apply(Graphics graphics) {
        T value = future.get();

        if (value != null)
            apply(graphics, value);

        return null;
    }

    public abstract void apply(Graphics graphics, T value);
}

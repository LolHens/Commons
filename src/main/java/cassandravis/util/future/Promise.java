package cassandravis.util.future;

/**
 * Created by u016595 on 07.04.2016.
 */
public class Promise<T> implements Future<T> {
    private volatile T value;

    public Promise(T value) {
        this.value = value;
    }

    public Promise() {
        this(null);
    }

    public void complete(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}

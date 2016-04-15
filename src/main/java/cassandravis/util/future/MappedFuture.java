package cassandravis.util.future;

import java.util.function.Function;

/**
 * Created by u016595 on 07.04.2016.
 */
class MappedFuture<T, R> implements Future<R> {
    private final Future<T> future;
    private final Function<T, R> map;

    public MappedFuture(Future<T> future, Function<T, R> map) {
        this.map = map;
        this.future = future;
    }

    @Override
    public R get() {
        return map.apply(future.get());
    }
}

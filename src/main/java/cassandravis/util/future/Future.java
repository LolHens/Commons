package cassandravis.util.future;

import java.util.function.Function;

/**
 * Created by u016595 on 07.04.2016.
 */
public interface Future<T> {
    T get();

    default <R> Future<R> map(Function<T, R> function) {
        return new MappedFuture<>(this, function);
    }
}

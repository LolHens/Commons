package cassandravis.util.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by u016595 on 04.04.2016.
 */
public class MethodCache<E> {
    private List<Function<? super E, ?>> methods = new ArrayList<>();

    public MethodCache() {
    }

    public void add(Function<? super E, ?> method) {
        methods.add(method);
    }

    public void add(Consumer<? super E> method, Object result) {
        methods.add((instance) -> {
            method.accept(instance);
            return result;
        });
    }

    public List<Object> apply(E instance) {
        return methods.stream().map(method -> method.apply(instance)).collect(Collectors.toList());
    }
}

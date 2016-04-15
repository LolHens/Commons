package org.lolhens.commons.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Created by u016595 on 06.04.2016.
 */
public class Schema<E> {
    private Map<Integer, SchemaValue<E>> elements = new HashMap<>();
    private BiFunction<Schema<E>, Integer, E> subst;

    private Schema<E> parent = null;
    private Map<String, Schema<E>> children = new HashMap<>();

    @SafeVarargs
    public Schema(BiFunction<Schema<E>, Integer, E> subst, E... elems) {
        for (int i = 0; i < elems.length; i++)
            elements.put(i, new SchemaValue<>(elems[i]));

        this.subst = subst;
    }

    public Schema(E elem) {
        this((schema, i) -> schema.get(i - 1), elem);
    }

    public Schema set(int index, E elem) {
        elements.put(index, new SchemaValue<>(elem));
        return this;
    }

    public E get(int index) {
        SchemaValue<E> value = elements.get(index);

        if (value == null)
            elements.put(index, new SchemaValue<>(() -> subst.apply(this, index)));

        return elements.get(index).getValue();
    }

    public E get() {
        return get(0);
    }

    public Schema<E> setChild(String name, Schema<E> schema) {
        children.put(name, schema);
        schema.parent = this;
        return this;
    }

    public Schema<E> getChild(String name) {
        return children.get(name);
    }

    public Schema<E> getParent() {
        return parent;
    }
}

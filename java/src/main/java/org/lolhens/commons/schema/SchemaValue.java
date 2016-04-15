package org.lolhens.commons.schema;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lolhens.commons.function.Producer;

import java.util.UUID;

/**
 * Created by u016595 on 06.04.2016.
 */
class SchemaValue<E> {
    private UUID uuid;

    private SchemaValue<E> dependent;
    private UUID dependentUUID;
    private Producer<E> producer;

    private E value;

    private static final ThreadLocal<SchemaValue<?>> lastGet = new ThreadLocal<>();

    private SchemaValue(Producer<E> producer, Pair<E, SchemaValue<E>> pair) {
        this.dependent = pair.getRight();
        this.producer = producer;
        setValue(pair.getLeft());
    }

    public SchemaValue(Producer<E> producer, SchemaValue<E> dependent) {
        this(producer, new ImmutablePair<>(producer.produce(), dependent));
    }

    public SchemaValue(Producer<E> producer) {
        this(producer, getDependent(producer));
    }

    private static <E> Pair<E, SchemaValue<E>> getDependent(Producer<E> producer) {
        lastGet.remove();
        E value = producer.produce();
        return new ImmutablePair<>(value, (SchemaValue<E>) lastGet.get());
    }

    public SchemaValue(E value) {
        setValue(value);
    }

    public E getValue() {
        lastGet.set(this);

        if (dependent != null && !dependentUUID.equals(dependent.uuid))
            value = producer.produce();

        return value;
    }

    public void setValue(E value) {
        this.value = value;
        if (dependent != null) dependentUUID = dependent.uuid;
        invalidate();
    }

    public SchemaValue<E> getDependent() {
        return dependent;
    }

    private void invalidate() {
        uuid = UUID.randomUUID();
    }
}

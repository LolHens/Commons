package org.lolhens.commons.tuple;

import java.util.List;

public interface Tuple extends List<Object> {
    @Override
    Tuple subList(int paramInt1, int paramInt2);

    <T> T get(int index, Class<T> cast) throws IndexOutOfBoundsException;

    <T> List<T> getType(Class<T> type);

    <T> T[] getTypeArray(Class<T> type);

    <T> T getType(Class<T> type, int index);

    <T> T getFirst(Class<T> type);

    boolean containsType(Class<?> type);
}

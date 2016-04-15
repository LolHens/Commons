package cassandravis.util.collection;

import java.util.*;

/**
 * Created by u016595 on 05.04.2016.
 */
public class Collections2 {
    public static <K, V> Map<K, V> Map(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> Map(List<K> keys, List<V> values) {
        Map<K, V> map = new HashMap<>();
        Iterator<K> iKey = keys.iterator();
        Iterator<V> iValue = values.iterator();
        while (iKey.hasNext() && iValue.hasNext())
            map.put(iKey.next(), iValue.next());
        return map;
    }

    @SafeVarargs
    public static <E> List<E> List(E... elems) {
        return LinkedList(elems);
    }

    @SafeVarargs
    public static <E> List<E> LinkedList(E... elems) {
        List<E> list = new LinkedList<>();
        java.util.Collections.addAll(list, elems);
        return list;
    }

    @SafeVarargs
    public static <E> List<E> ArrayList(E... elems) {
        List<E> list = new ArrayList<>();
        java.util.Collections.addAll(list, elems);
        return list;
    }
}

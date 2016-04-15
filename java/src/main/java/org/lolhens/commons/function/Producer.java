package org.lolhens.commons.function;

/**
 * Created by u016595 on 06.04.2016.
 */
@FunctionalInterface
public interface Producer<R> {
    R produce();
}

package org.lolhens.commons.schema;


import java.util.function.BiFunction;

/**
 * Created by u016595 on 06.04.2016.
 */
public class StringSchema extends Schema<String> {
    public StringSchema(BiFunction<Schema<String>, Integer, String> subst, String... elems) {
        super(subst, elems);
    }
}

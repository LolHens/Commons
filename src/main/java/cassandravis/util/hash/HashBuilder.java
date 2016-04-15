package cassandravis.util.hash;

/**
 * Created by u016595 on 05.04.2016.
 */
public class HashBuilder {
    private final StringBuilder builder = new StringBuilder();

    public HashBuilder(Object... objects) {
        appendAll(objects);
    }

    public HashBuilder append(Object object) {
        builder.append(object != null ? object.toString() : "null");
        builder.append(";\n");
        return this;
    }

    public HashBuilder appendAll(Iterable<?> objects) {
        for (Object object : objects)
            append(object);
        return this;
    }

    public HashBuilder appendAll(Object... objects) {
        for (Object object : objects)
            append(object);
        return this;
    }

    public int build() {
        return builder.toString().hashCode();
    }

    @Override
    public int hashCode() {
        return build();
    }
}

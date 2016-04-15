package cassandravis.util.schema;


import java.awt.*;
import java.util.Random;
import java.util.function.BiFunction;

/**
 * Created by u016595 on 06.04.2016.
 */
public class ColorSchema extends Schema<Color> {
    public ColorSchema(BiFunction<Schema<Color>, Integer, Color> subst, Color... elems) {
        super(subst, elems);
    }

    public ColorSchema(Color elem) {
        super(elem);
    }

    private static final Random rnd = new Random();

    private static final ColorSchema random = new ColorSchema((schema, index) -> {
        return Color.getHSBColor(rnd.nextFloat(), 1, 0.95f);
    });

    public static ColorSchema random() {
        return random;
    }
}

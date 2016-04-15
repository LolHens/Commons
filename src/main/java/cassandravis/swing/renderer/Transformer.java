package cassandravis.swing.renderer;

/**
 * Created by u016595 on 06.04.2016.
 */
@FunctionalInterface
public interface Transformer {
    Rectangle transform(double x, double y, double width, double height);
}

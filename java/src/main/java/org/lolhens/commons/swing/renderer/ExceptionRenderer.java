package org.lolhens.commons.swing.renderer;

import org.lolhens.commons.swing.renderer.primitives.SetColor;
import org.lolhens.commons.swing.renderer.primitives.Text;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created by u016595 on 04.04.2016.
 */
public class ExceptionRenderer implements Renderer {
    private final Exception exception;

    public ExceptionRenderer(Exception exception) {
        this.exception = exception;
    }

    private String stackTrace() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteStream);
        exception.printStackTrace(printStream);
        return byteStream.toString();
    }

    @Override
    public Renderer render(Renderer renderer) {
        Renderer.Builder builder = Renderer.builder();

        String stackTrace = stackTrace();

        builder.append(new BackgroundRenderer(Color.BLACK));
        builder.append(new SetColor(Color.RED));
        builder.append(new Text(stackTrace).transform((x, y, width, height) -> new org.lolhens.commons.swing.renderer.Rectangle(x + 10, y, width, height)));

        return builder.build();
    }
}

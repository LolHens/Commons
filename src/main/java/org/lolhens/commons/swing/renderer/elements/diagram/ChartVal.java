package org.lolhens.commons.swing.renderer.elements.diagram;

import org.lolhens.commons.hash.HashBuilder;

import java.awt.*;
import java.util.function.DoubleFunction;

/**
 * Created by u016595 on 06.04.2016.
 */
public class ChartVal {
    private String name;
    private double x, y;
    private Color color;

    public ChartVal(String name, double x, double y, Color color) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public ChartVal(String name, double x, DoubleFunction<Double> y, Color color) {
        this(name, x, y.apply(x), color);
    }

    public ChartVal(String name, double x, double y) {
        this(name, x, y, null);
    }

    public ChartVal(String name, double x, DoubleFunction<Double> y) {
        this(name, x, y.apply(x));
    }

    public ChartVal(double x, double y, Color color) {
        this(((Float) (float) x).toString(), x, y, color);
    }

    public ChartVal(double x, DoubleFunction<Double> y, Color color) {
        this(x, y.apply(x), color);
    }

    public ChartVal(double x, double y) {
        this(x, y, null);
    }

    public ChartVal(double x, DoubleFunction<Double> y) {
        this(x, y.apply(x));
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public ChartVal copy(String name, Double x, Double y, Color color) {
        return new ChartVal(
                name != null ? name : this.name,
                x != null ? x : this.x,
                y != null ? y : this.y,
                color != null ? color : this.color);
    }

    @Override
    public int hashCode() {
        HashBuilder builder = new HashBuilder();

        builder.appendAll(name, x, y, color);

        return builder.build();
    }
}

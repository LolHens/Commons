package org.lolhens.commons.swing.renderer;

/**
 * Created by u016595 on 06.04.2016.
 */
public class Rectangle {
    private double x, y, width, height;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    private static Rectangle empty = new Rectangle(0, 0, 1, 1);

    public static Rectangle empty() {
        return empty;
    }
}

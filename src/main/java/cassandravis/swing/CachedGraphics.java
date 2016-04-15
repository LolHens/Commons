package cassandravis.swing;

import cassandravis.util.function.MethodCache;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.function.Function;

/**
 * Created by u016595 on 04.04.2016.
 */
public class CachedGraphics extends Graphics {
    private final MethodCache<Graphics> methodCache = new MethodCache<>();

    private Color color;
    private Font font;
    private Function<Font, FontMetrics> getFontMetrics;

    public CachedGraphics(Color color, Font font, Function<Font, FontMetrics> getFontMetrics) {
        this.color = color;
        this.font = font;
        this.getFontMetrics = getFontMetrics;
    }

    public CachedGraphics() {
        this(null, null, null);
    }

    public void apply(Graphics graphics) {
        methodCache.apply(graphics);
    }

    @Override
    public Graphics create() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void translate(int i, int i1) {
        methodCache.add((graphics) -> graphics.translate(i, i1), null);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        methodCache.add((graphics) -> graphics.setColor(color), null);
        this.color = color;
    }

    @Override
    public void setPaintMode() {
        methodCache.add(Graphics::setPaintMode, null);
    }

    @Override
    public void setXORMode(Color color) {
        methodCache.add((graphics) -> graphics.setXORMode(color), null);
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setFont(Font font) {
        methodCache.add((graphics) -> graphics.setFont(font), null);
        this.font = font;
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        if (getFontMetrics == null)
            throw new UnsupportedOperationException();

        return getFontMetrics.apply(font);
    }

    @Override
    public Rectangle getClipBounds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clipRect(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.clipRect(i, i1, i2, i3), null);
    }

    @Override
    public void setClip(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.setClip(i, i1, i2, i3), null);
    }

    @Override
    public Shape getClip() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClip(Shape shape) {
        methodCache.add((graphics) -> graphics.setClip(shape), null);
    }

    @Override
    public void copyArea(int i, int i1, int i2, int i3, int i4, int i5) {
        methodCache.add((graphics) -> graphics.copyArea(i, i1, i2, i3, i4, i5), null);
    }

    @Override
    public void drawLine(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.drawLine(i, i1, i2, i3), null);
    }

    @Override
    public void fillRect(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.fillRect(i, i1, i2, i3), null);
    }

    @Override
    public void clearRect(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.clearRect(i, i1, i2, i3), null);
    }

    @Override
    public void drawRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
        methodCache.add((graphics) -> graphics.drawRoundRect(i, i1, i2, i3, i4, i5), null);
    }

    @Override
    public void fillRoundRect(int i, int i1, int i2, int i3, int i4, int i5) {
        methodCache.add((graphics) -> graphics.fillRoundRect(i, i1, i2, i3, i4, i5), null);
    }

    @Override
    public void drawOval(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.drawOval(i, i1, i2, i3), null);
    }

    @Override
    public void fillOval(int i, int i1, int i2, int i3) {
        methodCache.add((graphics) -> graphics.fillOval(i, i1, i2, i3), null);
    }

    @Override
    public void drawArc(int i, int i1, int i2, int i3, int i4, int i5) {
        methodCache.add((graphics) -> graphics.drawArc(i, i1, i2, i3, i4, i5), null);
    }

    @Override
    public void fillArc(int i, int i1, int i2, int i3, int i4, int i5) {
        methodCache.add((graphics) -> graphics.fillArc(i, i1, i2, i3, i4, i5), null);
    }

    @Override
    public void drawPolyline(int[] ints, int[] ints1, int i) {
        methodCache.add((graphics) -> graphics.drawPolyline(ints, ints1, i), null);
    }

    @Override
    public void drawPolygon(int[] ints, int[] ints1, int i) {
        methodCache.add((graphics) -> graphics.drawPolygon(ints, ints1, i), null);
    }

    @Override
    public void fillPolygon(int[] ints, int[] ints1, int i) {
        methodCache.add((graphics) -> graphics.fillPolygon(ints, ints1, i), null);
    }

    @Override
    public void drawString(String s, int i, int i1) {
        methodCache.add((graphics) -> graphics.drawString(s, i, i1), null);
    }

    @Override
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i, int i1) {
        methodCache.add((graphics) -> graphics.drawString(attributedCharacterIterator, i, i1), null);
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, imageObserver), null);
        return false;
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, int i2, int i3, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, i2, i3, imageObserver), null);
        return false;
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, Color color, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, color, imageObserver), null);
        return false;
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, int i2, int i3, Color color, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, i2, i3, color, imageObserver), null);
        return false;
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, i2, i3, i4, i5, i6, i7, imageObserver), null);
        return false;
    }

    @Override
    public boolean drawImage(Image image, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
        methodCache.add((graphics) -> graphics.drawImage(image, i, i1, i2, i3, i4, i5, i6, i7, color, imageObserver), null);
        return false;
    }

    @Override
    public void dispose() {
        methodCache.add(Graphics::dispose, null);
    }
}

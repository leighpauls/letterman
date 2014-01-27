package ca.teamdave.letterman.robotcomponents.camera;

/**
 * Rectangle, in units of pixels
 */
public class BoundingRectangle {
    public final double x, y;
    public final double width, height;

    public BoundingRectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return "(" + x + ", " + y + ") " + width + "x" + height;
    }
}

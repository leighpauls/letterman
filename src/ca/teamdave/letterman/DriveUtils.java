package ca.teamdave.letterman;

/**
 * Utilities for common math needed for driving control
 */
public class DriveUtils {

    /**
     * @param angle An angle in degrees
     * @return the angle in it's -180 to +180 degree representation
     */
    public static double normalizeAngle(double angle) {
        angle = angle % 360.0;
        if (angle > 180.0) {
            return angle - 360.0;
        } else if (angle < -180.0) {
            return angle + 360.0;
        }
        return angle;
    }
}

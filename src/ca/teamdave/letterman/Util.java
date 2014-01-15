package ca.teamdave.letterman;

/**
 * Static utility functions that don't exist in J4ME
 */
public class Util {
    /**
     * @param a
     * @return The sign of a, as +1, -1, or 0
     */
    public static double sign(double a) {
        if (a > 0.0) {
            return 1.0;
        } else if (a < 0.0) {
            return -1.0;
        }
        return 0.0;
    }
}

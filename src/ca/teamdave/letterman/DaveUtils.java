package ca.teamdave.letterman;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Utilities for common math
 */
public class DaveUtils {

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

    public static double jsonDouble(JSONObject obj, String key) throws JSONException {
        return Double.parseDouble(obj.getString(key));
    }
}

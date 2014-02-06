package ca.teamdave.letterman;

import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.Victor;
import org.json.me.JSONArray;
import org.json.me.JSONException;

/**
 * Utilities for common functions not included in this ridiculous distribution of Java
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

    /**
     * @return The current time off the FPGA in seconds
     */
    public static double systemTimeSeconds() {
        return (double)Utility.getFPGATime() * 1.0e-6;
    }

    /**
     * @param jsonArray
     * @return the int[] equivalent of jsonArray
     * @throws JSONException
     */
    public static int[] toIntArray(JSONArray jsonArray) throws JSONException {
        int[] res = new int[jsonArray.length()];
        for (int i = 0; i < res.length; ++i) {
            res[i] = ((Integer)jsonArray.get(i)).intValue();
        }
        return res;
    }

    /**
     * @param channels
     * @return An array of victors corresponding to channels
     */
    public static Victor[] getVictorSet(int[] channels) {
        Victor[] res = new Victor[channels.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = new Victor(channels[i]);
        }
        return res;
    }
}

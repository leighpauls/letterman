package ca.teamdave.letterman.config.component;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration of a {@link ca.teamdave.letterman.robotcomponents.Shooter}
 */
public class ShooterConfig {
    public final int[] victors;
    public final int encoderA;
    public final int encoderB;
    public final boolean inputInverted;
    public final double ticksPerDegree;
    public final int limitSwitch;
    public final double postFirePause;
    public final double lostReadynessTime;

    public ShooterConfig(JSONObject json) throws JSONException {
        JSONArray jsonVictors = json.getJSONArray("victors");
        victors = new int[jsonVictors.length()];
        for (int i = 0; i < victors.length; ++i) {
            victors[i] = ((Integer)jsonVictors.get(i)).intValue();
        }

        encoderA = json.getInt("encoderA");
        encoderB = json.getInt("encoderB");
        inputInverted = json.getBoolean("inputInverted");
        ticksPerDegree = json.getDouble("ticksPerDegree");

        limitSwitch = json.getInt("limitSwitch");

        postFirePause = json.getDouble("poseFirePause");
        lostReadynessTime = json.getDouble("lostReadynessTime");

    }
}

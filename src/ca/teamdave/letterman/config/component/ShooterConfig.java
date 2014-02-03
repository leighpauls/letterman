package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.DaveUtils;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration of a {@link ca.teamdave.letterman.robotcomponents.Shooter}
 */
public class ShooterConfig {
    public final int[] victors;
    public final boolean outputInverted;
    public final int limitSwitch;
    public final double postFirePause;
    public final double lostReadinessTime;

    public ShooterConfig(JSONObject json) throws JSONException {
        victors = DaveUtils.toIntArray(json.getJSONArray("victors"));
        outputInverted = json.getBoolean("outputInverted");

        limitSwitch = json.getInt("limitSwitch");

        postFirePause = json.getDouble("postFirePause");
        lostReadinessTime = json.getDouble("lostReadinessTime");

    }
}

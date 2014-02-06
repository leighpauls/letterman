package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.DaveUtils;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for {@link WheelSetConfig}
*/
public class WheelSetConfig {
    public final int[] victorChannels;
    public final boolean outputInverted;

    public final int encoderA;
    public final int encoderB;
    public final boolean inputInverted;
    public final double ticksPerFoot;

    public WheelSetConfig(JSONObject json) throws JSONException {
        victorChannels = DaveUtils.toIntArray(json.getJSONArray("victorChannels"));
        outputInverted = json.getBoolean("outputInverted");
        encoderA = json.getInt("encoderA");
        encoderB = json.getInt("encoderB");
        inputInverted = json.getBoolean("inputInverted");
        ticksPerFoot = json.getDouble("ticksPerFoot");
    }
}

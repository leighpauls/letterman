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

    public WheelSetConfig(
            int[] victorChannels,
            boolean outputInverted,
            int encoderA,
            int encoderB,
            boolean inputInverted,
            double ticksPerFoot) {
        this.victorChannels = victorChannels;
        this.encoderA = encoderA;
        this.encoderB = encoderB;
        this.inputInverted = inputInverted;
        this.outputInverted = outputInverted;
        this.ticksPerFoot = ticksPerFoot;
    }

    public WheelSetConfig(JSONObject json) throws JSONException {
        JSONArray victors = json.getJSONArray("victorChannels");
        victorChannels = new int[victors.length()];
        for (int i = 0; i < victors.length(); ++i) {
            victorChannels[i] = Integer.parseInt(victors.getString(i));
        }
        outputInverted = json.getBoolean("outputInverted");
        encoderA = json.getInt("encoderA");
        encoderB = json.getInt("encoderB");
        inputInverted = json.getBoolean("inputInverted");
        ticksPerFoot = DaveUtils.jsonDouble(json, "ticksPerFoot");
    }
}

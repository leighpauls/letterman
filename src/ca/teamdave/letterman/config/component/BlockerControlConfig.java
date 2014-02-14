package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.config.control.PidControllerConfig;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Describes the control configuration of the blocker
 */
public class BlockerControlConfig {
    public final double degreesPerVolt;
    public final double offsetDegrees;
    public final boolean invertOutput;
    public final PidControllerConfig pidConfig;

    public final double blockPosition;
    public final double catchPosition;
    public final double travelPosition;
    public final double shotClearancePosition;

    public BlockerControlConfig(JSONObject json) throws JSONException {
        degreesPerVolt = json.getDouble("degreesPerVolt");
        offsetDegrees = json.getDouble("offsetDegrees");
        invertOutput = json.getBoolean("invertOutput");
        pidConfig = new PidControllerConfig(json.getJSONObject("pidConfig"));

        blockPosition = json.getDouble("blockPosition");
        catchPosition = json.getDouble("catchPosition");
        travelPosition = json.getDouble("travelPosition");
        shotClearancePosition = json.getDouble("shotClearancePosition");
    }
}

package ca.teamdave.letterman.config.component;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for the blocker
 */
public class BlockerConfig {
    public final BlockerHardwareConfig hardwareConfig;
    public final BlockerControlConfig controlConfig;

    public BlockerConfig(JSONObject json) throws JSONException {
        hardwareConfig = new BlockerHardwareConfig(json.getJSONObject("hardware"));
        controlConfig = new BlockerControlConfig(json.getJSONObject("control"));
    }
}

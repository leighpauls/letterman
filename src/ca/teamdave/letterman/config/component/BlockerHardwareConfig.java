package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.DaveUtils;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Describes the hardware configuration of the blocker
 */
public class BlockerHardwareConfig {
    public final int[] victors;
    public final int potChannel;

    public BlockerHardwareConfig(JSONObject json) throws JSONException {
        victors = DaveUtils.toIntArray(json.getJSONArray("victors"));
        potChannel = json.getInt("potChannel");
    }
}

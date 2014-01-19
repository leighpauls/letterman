package ca.teamdave.letterman.config.command;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Config object for {@link ca.teamdave.letterman.auto.commands.drive.WaitForRegion}
 */
public class WaitForRegionConfig {
    public final double x1;
    public final double y1;
    public final double x2;
    public final double y2;

    public WaitForRegionConfig(JSONObject json) throws JSONException {
        x1 = json.getDouble("x1");
        y1 = json.getDouble("y1");
        x2 = json.getDouble("x2");
        y2 = json.getDouble("y2");
    }
}

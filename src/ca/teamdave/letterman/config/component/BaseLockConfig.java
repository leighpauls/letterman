package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.config.control.PidControllerConfig;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for {@link ca.teamdave.letterman.robotcomponents.BaseLock}
 */
public class BaseLockConfig {
    public final PidControllerConfig forwardControl;
    public final PidControllerConfig turnControl;

    public BaseLockConfig(JSONObject json) throws JSONException {
        forwardControl = new PidControllerConfig(json.getJSONObject("forwardControl"));
        turnControl = new PidControllerConfig(json.getJSONObject("turnControl"));
    }
}

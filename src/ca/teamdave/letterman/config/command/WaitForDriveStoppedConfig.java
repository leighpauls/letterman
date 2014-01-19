package ca.teamdave.letterman.config.command;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Config for a RobotStopped command
 */
public class WaitForDriveStoppedConfig {
    public final double forwardStopSpeed; // in feet/second
    public final double turnStopSpeed; // in deg/second

    public WaitForDriveStoppedConfig(JSONObject json) throws JSONException {
        forwardStopSpeed = json.getDouble("forwardStopSpeed");
        turnStopSpeed = json.getDouble("turnStopSpeed");
    }
}

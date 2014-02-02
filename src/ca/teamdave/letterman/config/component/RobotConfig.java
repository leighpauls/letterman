package ca.teamdave.letterman.config.component;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Describes the hardware configuration of the robot (sensor pins, pwm channels, scaling factors, etc)
 */
public class RobotConfig {
    public final DriveBaseConfig driveConfig;
    public final ShooterConfig shooterConfig;

    public RobotConfig(JSONObject json) throws JSONException {
        driveConfig = new DriveBaseConfig(json.getJSONObject("driveBaseConfig"));
        shooterConfig = new ShooterConfig(json.getJSONObject("shooterConfig"));
    }
}

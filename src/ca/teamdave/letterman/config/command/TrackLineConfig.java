package ca.teamdave.letterman.config.command;

import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPosition;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for a {@link ca.teamdave.letterman.auto.commands.drive.TrackLine} command
 */
public class TrackLineConfig {
    public final RobotPosition lineOrigin;
    public final double lineDirection;

    public final double degreesPerFootError;
    public final PidControllerConfig dynamicTurnController;

    public final double forwardSpeed;
    public final PidControllerConfig speedController;

    public TrackLineConfig(
            JSONObject json,
            PidControllerConfig dynamicTurnController,
            PidControllerConfig speedController) throws JSONException {
        this.lineOrigin = new RobotPosition(json.getJSONObject("lineOrigin"));
        this.lineDirection = json.getDouble("lineDirection");

        this.degreesPerFootError = json.getDouble("degreesPerFootError");
        this.dynamicTurnController = dynamicTurnController;

        this.forwardSpeed = json.getDouble("forwardSpeed");
        this.speedController = speedController;
    }
}

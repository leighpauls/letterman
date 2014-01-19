package ca.teamdave.letterman.config.command;

import ca.teamdave.letterman.config.control.PidControllerConfig;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for {@link ca.teamdave.letterman.auto.commands.drive.TurnToHeading}
 */
public class TurnToHeadingConfig {
    public final double heading;
    public final double completionErrorAngle;
    public final PidControllerConfig staticTurnControl;

    public TurnToHeadingConfig(
            JSONObject json,
            PidControllerConfig staticTurnControl) throws JSONException {
        heading = json.getDouble("heading");
        completionErrorAngle = json.getDouble("completionErrorAngle");
        this.staticTurnControl = staticTurnControl;
    }
}

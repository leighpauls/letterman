package ca.teamdave.letterman.config.command;

import ca.teamdave.letterman.config.control.PidControllerConfig;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration object for {@link ca.teamdave.letterman.auto.commands.drive.DriveToDist}
 */
public class DriveToDistConfig {
    public final PidControllerConfig driveController;
    public final double destDistance;
    public final double completionError;

    public DriveToDistConfig(JSONObject json, PidControllerConfig driveController)
            throws JSONException {
        this.driveController = driveController;
        destDistance = json.getDouble("destDistance");
        completionError = json.getDouble("completionError");
    }
}

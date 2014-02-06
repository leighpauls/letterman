package ca.teamdave.letterman.config.component;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for an {@link ca.teamdave.letterman.robotcomponents.Intake}
 */
public class IntakeConfig {
    public final int victorChannel;
    public final int armInRelay;
    public final int armOutRelay;
    public final double retractionTime;
    public final double extensionTime;

    public IntakeConfig(JSONObject json) throws JSONException {
        victorChannel = json.getInt("victorChannel");
        armInRelay = json.getInt("armInRelay");
        armOutRelay = json.getInt("armOutRelay");
        retractionTime = json.getDouble("retractionTime");
        extensionTime = json.getDouble("extensionTime");
    }
}

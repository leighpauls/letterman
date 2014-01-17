package ca.teamdave.letterman.config.control;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Configuration for a {@link ca.teamdave.letterman.PidController}
 */
public class PidControllerConfig {
    public final double p;
    public final double i;
    public final double d;

    public PidControllerConfig(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public PidControllerConfig(JSONObject json) throws JSONException {
        p = Double.parseDouble(json.getString("p"));
        i = Double.parseDouble(json.getString("i"));
        d = Double.parseDouble(json.getString("d"));
    }
}

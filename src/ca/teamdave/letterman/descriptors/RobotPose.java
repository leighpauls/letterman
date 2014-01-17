package ca.teamdave.letterman.descriptors;

import ca.teamdave.letterman.DaveUtils;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Container class for a robot's position and orientation
 */
public class RobotPose {
    private final RobotPosition mPosition;
    private final double mHeading;

    public RobotPose(RobotPosition position, double heading) {
        mPosition = position;
        mHeading = heading;
    }

    public RobotPose(JSONObject json) throws JSONException {
        mPosition = new RobotPosition(json.getJSONObject("position"));
        mHeading = DaveUtils.jsonDouble(json, "heading");
    }

    public RobotPosition getPosition() {
        return mPosition;
    }

    public double getHeading() {
        return mHeading;
    }


}

package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.descriptors.RobotPose;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Config for a drive base
 */
public class DriveBaseConfig {
    public final int gyroChannel;
    public final double gyroVoltSecondsPerDegree;
    public final WheelSetConfig leftWheelSet;
    public final WheelSetConfig rightWheelSet;
    public final RobotPose initialPose;


    public DriveBaseConfig(
            int gyroChannel,
            double gyroVoltSecondsPerDegree,
            WheelSetConfig leftWheelSet,
            WheelSetConfig rightWheelSet,
            RobotPose initialPose) {
        this.gyroChannel = gyroChannel;
        this.gyroVoltSecondsPerDegree = gyroVoltSecondsPerDegree;
        this.leftWheelSet = leftWheelSet;
        this.rightWheelSet = rightWheelSet;
        this.initialPose = initialPose;
    }

    public DriveBaseConfig(JSONObject json) throws JSONException {
        gyroChannel = json.getInt("gyroChannel");
        gyroVoltSecondsPerDegree = json.getDouble("gyroVoltSecondsPerDegree");
        leftWheelSet = new WheelSetConfig(json.getJSONObject("leftWheelSet"));
        rightWheelSet = new WheelSetConfig(json.getJSONObject("rightWheelSet"));
        initialPose = new RobotPose(json.getJSONObject("initialPose"));
    }
}

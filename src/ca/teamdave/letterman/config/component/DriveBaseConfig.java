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
    public final int highGearSolenoid;
    public final int lowGearSolenoid;

    public DriveBaseConfig(JSONObject json) throws JSONException {
        gyroChannel = json.getInt("gyroChannel");
        gyroVoltSecondsPerDegree = json.getDouble("gyroVoltSecondsPerDegree");
        leftWheelSet = new WheelSetConfig(json.getJSONObject("leftWheelSet"));
        rightWheelSet = new WheelSetConfig(json.getJSONObject("rightWheelSet"));
        initialPose = new RobotPose(json.getJSONObject("initialPose"));
        highGearSolenoid = json.getInt("highGearSolenoid");
        lowGearSolenoid = json.getInt("lowGearSolenoid");
    }
}

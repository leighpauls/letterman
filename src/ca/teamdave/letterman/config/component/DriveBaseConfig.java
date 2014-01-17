package ca.teamdave.letterman.config.component;

import ca.teamdave.letterman.descriptors.RobotPose;

/**
 * Config for a drive base
 */
public class DriveBaseConfig {
    public final int gyroChannel;
    public final double gyroVoltsSecondsPerDegree;
    public final WheelSetConfig left;
    public final WheelSetConfig right;
    public final RobotPose initialPose;


    public DriveBaseConfig(
            int gyroChannel,
            double gyroVoltsSecondsPerDegree,
            WheelSetConfig left,
            WheelSetConfig right,
            RobotPose initialPose) {
        this.gyroChannel = gyroChannel;
        this.gyroVoltsSecondsPerDegree = gyroVoltsSecondsPerDegree;
        this.left = left;
        this.right = right;
        this.initialPose = initialPose;
    }
}

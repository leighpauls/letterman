package ca.teamdave.letterman.robotconfig;

import ca.teamdave.letterman.RobotPose;

/**
 * Describes the hardware configuration of the robot (sensor pins, pwm channels, scaling factors, etc)
 */
public class RobotConfig {
    public final DriveBaseConfig driveConfig;

    public RobotConfig(DriveBaseConfig driveConfig) {
        this.driveConfig = driveConfig;
    }

    // TODO: drive this from a json file
    public static final RobotConfig CONFIG = new RobotConfig(new DriveBaseConfig(
            1,
            0.007,
            new WheelSetConfig(new int[] {1, 2}, false, 1, 2, false, 318.0),
            new WheelSetConfig(new int[] {3, 4}, true, 3, 4, false, 318.0),
            new RobotPose(0, 0, 0)));
}

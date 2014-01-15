package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.config.component.RobotConfig;

/**
 * Top level component class
 */
public class Robot {
    private final DriveBase mDriveBase;

    public Robot(RobotConfig config) {
        mDriveBase = new DriveBase(config.driveConfig);
    }

    public DriveBase getDriveBase() {
        return mDriveBase;
    }
}

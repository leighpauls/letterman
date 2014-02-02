package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.config.component.RobotConfig;

/**
 * Top level component class
 */
public class Robot {
    private final DriveBase mDriveBase;
    private final HotnessTracker mHotnessTracker;
    private final Shooter mShooter;

    public Robot(RobotConfig config) {
        mDriveBase = new DriveBase(config.driveConfig);
        mHotnessTracker = new HotnessTracker();
        mShooter = new Shooter(config.shooterConfig);
    }

    public DriveBase getDriveBase() {
        return mDriveBase;
    }

    public HotnessTracker getHotnessTracker() {
        return mHotnessTracker;
    }

    public Shooter getShooter() { return mShooter; }
}

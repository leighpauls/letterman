package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.config.component.RobotConfig;

/**
 * Top level component class
 */
public class Robot {
    private final DriveBase mDriveBase;
    private final HotnessTracker mHotnessTracker;
    private final Shooter mShooter;
    private final Blocker mBlocker;

    public Robot(RobotConfig config) {
        mDriveBase = new DriveBase(config.driveConfig);
        mHotnessTracker = new HotnessTracker();
        mShooter = new Shooter(config.shooterConfig);
        mBlocker = new Blocker(config.blockerConfig);
    }

    public DriveBase getDriveBase() {
        return mDriveBase;
    }

    public HotnessTracker getHotnessTracker() {
        return mHotnessTracker;
    }

    public Shooter getShooter() { return mShooter; }

    public Blocker getBlocker() {
        return mBlocker;
    }
}

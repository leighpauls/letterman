package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.config.component.BaseLockConfig;

/**
 * Logic for controlling base lock in teleop mode
 */
public class BaseLock {
    private final PidController mForwardControl;
    private final PidController mTurnControl;
    private final DriveBase mDriveBase;

    private boolean mActivated;
    private double mEncoderSetpoint;
    private double mGyroSetpoint;

    public BaseLock(BaseLockConfig config, DriveBase driveBase) {
        mForwardControl = new PidController(config.forwardControl);
        mTurnControl = new PidController(config.turnControl);
        mDriveBase = driveBase;

        mActivated = false;
    }

    /**
     * Stop trying to base lock
     */
    public void deactivate() {
        mActivated = false;
    }

    /**
     * Update the base lock control loop, setting the current position to the set point if
     * "deactivate" was called since the last call to "update"
     * @param deltaTime Time that's passed in the last loop
     */
    public void update(double deltaTime) {
        double encoderPostion = mDriveBase.getEncoderPosition();
        double gyroPosition = mDriveBase.getPose().getHeading();

        if (!mActivated) {
            mEncoderSetpoint = encoderPostion;
            mGyroSetpoint = gyroPosition;

            // set this state as the new set point
            mForwardControl.reset(mEncoderSetpoint, mEncoderSetpoint);
            mTurnControl.reset(mGyroSetpoint, mGyroSetpoint);
            mActivated = true;
        }

        double forwardPower = mForwardControl.update(deltaTime, mEncoderSetpoint, encoderPostion);
        double turnPower = mTurnControl.update(deltaTime, mGyroSetpoint, gyroPosition);

        mDriveBase.setArcade(forwardPower, turnPower);
    }
}

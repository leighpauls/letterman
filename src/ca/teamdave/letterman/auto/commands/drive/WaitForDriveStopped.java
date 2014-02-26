package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Waits for the robot to come to a stop before finishing
 */
public class WaitForDriveStopped implements AutoCommand {
    private final DriveBase mDriveBase;
    private final double mForwardStopSpeed;
    private final double mTurnStopSpeed;

    public WaitForDriveStopped(WaitForDriveStoppedConfig config, DriveBase driveBase) {
        mForwardStopSpeed = config.forwardStopSpeed;
        mTurnStopSpeed = config.turnStopSpeed;
        mDriveBase = driveBase;
    }

    public void firstStep() { }

    public Completion runStep(double deltaTime) {
        if (Math.abs(mDriveBase.getForwardVelocity()) > mForwardStopSpeed ||
                Math.abs(mDriveBase.getTurnVelocity()) > mTurnStopSpeed) {
            return Completion.RUNNING;
        }
        System.out.println("Drive stopped");
        return Completion.FINISHED;
    }
}

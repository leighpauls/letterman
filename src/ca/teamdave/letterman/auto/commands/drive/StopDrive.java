package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Stops the drive motors, useful to keep the robot steady
 */
public class StopDrive implements AutoCommand {
    private final DriveBase mDriveBase;

    public StopDrive(DriveBase driveBase) {
        mDriveBase = driveBase;
    }

    public void firstStep() {
        mDriveBase.setArcade(0, 0);
    }

    public Completion runStep(double deltaTime) {
        return Completion.FINISHED;
    }
}

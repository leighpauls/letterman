package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.DriveToDistConfig;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Drives forward to a distance, without any concern for turning control
 */
public class DriveToDist implements AutoCommand {
    private final PidController mDriveController;
    private final DriveBase mDriveBase;

    private final double mDestDistance;
    private final double mCompletionError;

    private RobotPosition mInitialPosition;

    public DriveToDist(DriveToDistConfig config, DriveBase driveBase) {
        mDriveBase = driveBase;
        mDriveController = new PidController(config.driveController);
        mDestDistance = config.destDistance;
        mCompletionError = config.completionError;
    }

    public void firstStep() {
        mDriveController.reset(mDestDistance, 0);
        mInitialPosition = mDriveBase.getPose().getPosition();
    }

    public Completion runStep(double deltaTime) {
        double curDistance = mDriveBase.getPose().getPosition().distanceTo(mInitialPosition);
        mDriveBase.setArcade(mDriveController.update(deltaTime, mDestDistance, curDistance), 0);

        return Math.abs(curDistance - mDestDistance) > mCompletionError
                ? Completion.RUNNING
                : Completion.FINISHED;
    }
}

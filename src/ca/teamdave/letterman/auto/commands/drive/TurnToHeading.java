package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Command for turning to a particular heading
 */
public class TurnToHeading implements AutoCommand {
    private final double mDestHeading;
    private final double mCompletionErrorAngle;
    private final PidController mTurnPid;
    private final DriveBase mDriveBase;

    public TurnToHeading(TurnToHeadingConfig config, DriveBase driveBase) {
        mDestHeading = config.heading;
        mCompletionErrorAngle = config.completionErrorAngle;
        mTurnPid = new PidController(config.turnControl);
        mDriveBase = driveBase;
    }

    public void firstStep() {
        mTurnPid.reset(mDestHeading, mDriveBase.getPose().getHeading());
    }

    public Completion runStep(double deltaTime) {
        double curHeading = mDriveBase.getPose().getHeading();
        double turnPower = mTurnPid.update(deltaTime, mDestHeading, curHeading);
        mDriveBase.setArcade(0, turnPower);

        if (Math.abs(mDestHeading - curHeading) <= mCompletionErrorAngle) {
            System.out.println("Finished with:" + curHeading + " going to: " + mDestHeading);
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

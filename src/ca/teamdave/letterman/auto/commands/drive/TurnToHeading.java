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
        mTurnPid = new PidController(config.staticTurnControl);
        mDriveBase = driveBase;
    }

    protected double getDestHeading() {
        return mDestHeading;
    }

    public void firstStep() {
        mTurnPid.reset(getDestHeading(), mDriveBase.getPose().getHeading());
    }

    public Completion runStep(double deltaTime) {
        double curHeading = mDriveBase.getPose().getHeading();
        double destHeading = getDestHeading();
        double turnPower = mTurnPid.update(deltaTime, destHeading, curHeading);
        mDriveBase.setArcade(0, turnPower);

        if (Math.abs(destHeading - curHeading) <= mCompletionErrorAngle) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

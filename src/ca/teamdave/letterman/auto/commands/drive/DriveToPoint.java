package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.*;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.DriveToPointConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Drive the robot to a point on the field
 */
public class DriveToPoint implements AutoCommand {
    private final RobotPosition mEndPosition;
    private final double mTurnLockDistance;
    private final double mCompleteDistance;

    private final DriveBase mDriveBase;
    private final PidController mTurnController;
    private final PidController mDriveController;

    public DriveToPoint(DriveToPointConfig config, DriveBase driveBase) {
        mEndPosition = config.endPosition;
        mTurnLockDistance = config.turnLockDistance;
        mCompleteDistance = config.completeDistance;

        mTurnController = new PidController(config.turnControl);
        mDriveController = new PidController(config.driveControl);

        mDriveBase = driveBase;
    }

    private class Error {
        double headingError;
        double distanceError;
    }

    private Error getError() {
        RobotPose curPose = mDriveBase.getPose();
        RobotPosition curPosition = curPose.getPosition();

        double requiredHeading = curPosition.getHeadingToPosition(mEndPosition);
        Error result = new Error();
        result.headingError = DaveUtils.normalizeAngle(requiredHeading - curPose.getHeading());

        double absoluteDistanceError = curPosition.distanceTo(mEndPosition);
        if (Math.abs(result.headingError) < 90) {
            result.distanceError = absoluteDistanceError;
        } else {
            // drive backwards to the target
            result.distanceError = -absoluteDistanceError;

            result.headingError -= 180 * DaveUtils.sign(result.headingError);
        }
        return result;
    }

    public void firstStep() {
        Error error = getError();
        mTurnController.reset(error.headingError, 0);
        mDriveController.reset(error.distanceError, 0);
    }

    public Completion runStep(double deltaTime) {
        Error error = getError();
        double turnPower, forwardPower, effectiveDistError;

        if (Math.abs(error.distanceError) > mTurnLockDistance) {
            turnPower = mTurnController.update(deltaTime, error.headingError, 0);
            forwardPower = mDriveController.update(deltaTime, error.distanceError, 0);
            // scale forward power based on the the heading
            forwardPower *= Math.cos(error.headingError * Math.PI / 180.0);
            effectiveDistError = error.distanceError;
        } else {
            // don't try to turn, just, get horizontal with the point
            turnPower = 0;
            effectiveDistError = error.distanceError * Math.cos(error.headingError);
            forwardPower = mDriveController.update(deltaTime, 0, effectiveDistError);
        }

        mDriveBase.setArcade(forwardPower, turnPower);

        if (Math.abs(effectiveDistError) <= mCompleteDistance) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

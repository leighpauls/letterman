package ca.teamdave.letterman.auto;

import ca.teamdave.letterman.DriveUtils;
import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.RobotPose;
import ca.teamdave.letterman.config.command.DriveToPointConfig;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.RobotPosition;

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
        result.headingError = DriveUtils.normalizeAngle(requiredHeading - curPose.getHeading());

        double absoluteDistanceError = curPosition.distanceTo(mEndPosition);
        if (Math.abs(result.headingError) < 90) {
            result.distanceError = absoluteDistanceError;
        } else {
            // drive backwards to the target
            result.distanceError = -absoluteDistanceError;
            result.headingError -= 180 * Math.signum(result.headingError);
        }
        return result;
    }

    public void firstStep() {
        Error error = getError();
        mTurnController.reset(0, error.headingError);
        mDriveController.reset(0, error.distanceError);
    }

    public Completion runStep(double deltaTime) {
        Error error = getError();
        double turnPower, forwardPower;

        if (error.distanceError > mTurnLockDistance) {
            turnPower = mTurnController.update(deltaTime, 0, error.headingError);
            forwardPower = mDriveController.update(deltaTime, 0, error.distanceError);

            // scale forward power based on the the heading
            forwardPower *= Math.cos(error.headingError * Math.PI / 180.0);
        } else {
            // don't try to turn, just, get horizontal with the point
            turnPower = 0;
            double projectedError = error.distanceError * Math.cos(error.headingError);
            forwardPower = mDriveController.update(deltaTime, 0, projectedError);
        }

        mDriveBase.setArcade(forwardPower, turnPower);

        if (error.distanceError <= mCompleteDistance) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}
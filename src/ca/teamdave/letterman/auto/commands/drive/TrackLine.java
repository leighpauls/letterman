package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.DaveUtils;
import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Command that tried to follow a defined line at a set forward speed
 * This command has no set goal, so it always returns complete
 */
public class TrackLine implements AutoCommand {
    private final RobotPosition mLineOrigin;
    private final double mLineDirection;

    private final double mDegreesPerFootError;
    private final PidController mTurnController;

    private final double mForwardSpeed;
    private final PidController mSpeedController;

    private final DriveBase mDriveBase;

    public TrackLine(TrackLineConfig config, DriveBase driveBase) {
        mLineOrigin = config.lineOrigin;
        mLineDirection = config.lineDirection;
        mDegreesPerFootError = config.degreesPerFootError;
        mTurnController = new PidController(config.dynamicTurnController);
        mForwardSpeed = config.forwardSpeed;
        mSpeedController = new PidController(config.speedController);

        mDriveBase = driveBase;
    }

    private double getDesiredHeading() {
        // find my perpendicular distance to the line
        RobotPosition robotPosition = mDriveBase.getPose().getPosition();
        double originDistToRobot = robotPosition.distanceTo(mLineOrigin);
        double originBearingToRobot = mLineOrigin.getBearingToPosition(robotPosition);
        double innerAngle = mLineDirection - originBearingToRobot;
        double distToLine = originDistToRobot * Math.sin(innerAngle * Math.PI / 180.0);

        // turn that distance into an angle to drive at
        double desiredHeadingOffset = distToLine * mDegreesPerFootError;
        if (Math.abs(desiredHeadingOffset) > 90) {
            // don't try to go backwards
            desiredHeadingOffset = 90 * DaveUtils.sign(desiredHeadingOffset);
        }
        if (mForwardSpeed < 0) {
            // the robot is driving backwards, so teh offset should be inverted
            desiredHeadingOffset *= -1;
        }
        double res = mLineDirection + desiredHeadingOffset;
        System.out.println("Desired heading: " + res);
        return res;
    }

    public void firstStep() {
        mTurnController.reset(
                0,
                DaveUtils.normalizeAngle(mDriveBase.getPose().getHeading() - getDesiredHeading()));
        mSpeedController.reset(mForwardSpeed, mDriveBase.getForwardVelocity());
    }

    public Completion runStep(double deltaTime) {
        double forwardPower = mSpeedController.update(
                deltaTime,
                mForwardSpeed,
                mDriveBase.getForwardVelocity());
        double turnPower = mTurnController.update(
                deltaTime,
                0,
                DaveUtils.normalizeAngle(mDriveBase.getPose().getHeading() - getDesiredHeading()));
        mDriveBase.setArcade(forwardPower, turnPower);
        return Completion.FINISHED;
    }
}

package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.RobotPose;
import ca.teamdave.letterman.RobotPosition;
import ca.teamdave.letterman.auto.commands.*;
import ca.teamdave.letterman.auto.commands.dummy.DummyShoot;
import ca.teamdave.letterman.config.command.DriveToPointConfig;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * Auto mode that does the driving pattern for scoring two balls
 */
public class ScoreTwoDriving implements AutoMode {

    private final DriveBase mDriveBase;

    public ScoreTwoDriving(DriveBase driveBase) {
        mDriveBase = driveBase;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() {

        PidControllerConfig turnPidConfig = new PidControllerConfig(0.01, 0, 0);
        PidControllerConfig drivePidConfig = new PidControllerConfig(0.1, 0, 0);


        RobotPosition shootPosition = new RobotPosition(8, 0);
        RobotPosition pickupPosition = new RobotPosition(0, 0);

        DriveToPointConfig driveOut = new DriveToPointConfig(
                shootPosition,
                1,
                0.5,
                turnPidConfig,
                drivePidConfig);
        DriveToPointConfig driveBackToBall = new DriveToPointConfig(
                pickupPosition,
                1,
                0.25,
                turnPidConfig,
                drivePidConfig);

        return new Series(new AutoCommand[] {
                // TODO: drive this construction nicely from somewhere
                new DriveToPoint(driveOut, mDriveBase),
                new TurnToHeading(new TurnToHeadingConfig(20, 2, turnPidConfig), mDriveBase),
                new DummyShoot(),

                new TurnToHeading(new TurnToHeadingConfig(0, 2, turnPidConfig), mDriveBase),
                new DriveToPoint(driveBackToBall, mDriveBase),
                new Pause(0.5),

                new DriveToPoint(driveOut, mDriveBase),
                new TurnToHeading(new TurnToHeadingConfig(-20, 2, turnPidConfig), mDriveBase),
                new DummyShoot()
        });
    }
}

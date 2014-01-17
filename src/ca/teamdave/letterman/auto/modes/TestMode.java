package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.NoOp;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TurnToHeading;
import ca.teamdave.letterman.auto.commands.drive.WaitForDriveStopped;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;

/**
 * This class is just for testing individual commands
 */
public class TestMode implements AutoMode {
    private final DriveBase mDriveBase;

    public TestMode(DriveBase driveBase) {
        mDriveBase = driveBase;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() {
        return new Series(new AutoCommand[]{
                new Latch(new AutoCommand[]{
                        new TurnToHeading(
                                new TurnToHeadingConfig(
                                        45,
                                        2,
                                        new PidControllerConfig(0.01, 0.01, 0)),
                                mDriveBase),
                        new WaitForDriveStopped(new WaitForDriveStoppedConfig(0.1, 5), mDriveBase)
                }),
                new StopDrive(mDriveBase),
                new NoOp()
        });
    }
}

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
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * This class is just for testing individual commands
 */
public class TestMode implements AutoMode {
    private final DriveBase mDriveBase;
    private final JSONObject mAutoConfig;

    public TestMode(DriveBase driveBase, JSONObject config) {
        mDriveBase = driveBase;
        mAutoConfig = config;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() throws JSONException {
        PidControllerConfig turnController = new PidControllerConfig(
                mAutoConfig.getJSONObject("turnPid"));

        /* PidControllerConfig speedController = new PidControllerConfig(
                mAutoConfig.getJSONObject("speedPid")); */

        JSONObject modeConfig = mAutoConfig.getJSONObject("testMode");

        /* TrackLineConfig trackLineConfig = new TrackLineConfig(
                modeConfig.getJSONObject("trackLine"),
                turnController,
                speedController); */

        return new Series(new AutoCommand[]{
                new Latch(new AutoCommand[]{
                        new TurnToHeading(
                                new TurnToHeadingConfig(
                                        modeConfig.getJSONObject("turn"),
                                        turnController),
                                mDriveBase),
                        new WaitForDriveStopped(
                                new WaitForDriveStoppedConfig(modeConfig.getJSONObject("stop")),
                                mDriveBase)
                }),
                new StopDrive(mDriveBase),
                new NoOp()
        });
    }
}

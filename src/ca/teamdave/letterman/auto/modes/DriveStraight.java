package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TrackLine;
import ca.teamdave.letterman.auto.commands.drive.WaitForRegion;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.auto.commands.shoot.StartRetract;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.config.command.WaitForRegionConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.Robot;
import org.json.me.JSONObject;

/**
 * Just drives forward in auto mode
 */
public class DriveStraight implements AutoMode {
    private final JSONObject mAutoConfig;
    private final Robot mRobot;

    public DriveStraight(Robot robot, JSONObject autoConfig) {
        mAutoConfig = autoConfig;
        mRobot = robot;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() throws Exception {
        PidControllerConfig dynamicTurnController =
                new PidControllerConfig(mAutoConfig.getJSONObject("dynamicTurnPid"));
        PidControllerConfig speedController =
                new PidControllerConfig(mAutoConfig.getJSONObject("speedPid"));

        JSONObject modeConfig = mAutoConfig.getJSONObject("driveStraight");

        return new Series(new AutoCommand[]{
                // load the catapult
                new StartRetract(mRobot.getShooter()),
                new Latch(new AutoCommand[] {
                        new TrackLine(
                                new TrackLineConfig(
                                        modeConfig.getJSONObject("trackLine"),
                                        dynamicTurnController,
                                        speedController),
                                mRobot.getDriveBase()),
                        new WaitForRegion(
                                new WaitForRegionConfig(
                                        modeConfig.getJSONObject("waitForRegion")),
                                mRobot.getDriveBase())
                }),
                new StopDrive(mRobot.getDriveBase())
        });
    }

    public String getVisibleName() {
        return "Drive Forward";
    }
}

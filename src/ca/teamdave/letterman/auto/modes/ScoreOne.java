package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.shoot.FireHelper;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TrackLine;
import ca.teamdave.letterman.auto.commands.drive.WaitForDriveStopped;
import ca.teamdave.letterman.auto.commands.drive.WaitForRegion;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.config.command.WaitForRegionConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.Robot;
import org.json.me.JSONObject;

/**
 * Drive forward and score in the goal ahead of you immediately
 */
public class ScoreOne implements AutoMode {
    private final JSONObject mAutoConfig;
    private final Robot mRobot;

    public ScoreOne(JSONObject autoConfig, Robot robot) {
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

        JSONObject modeConfig = mAutoConfig.getJSONObject("scoreOne");

        return new Series(new AutoCommand[]{
                new Latch(new AutoCommand[] {
                        new TrackLine(
                                new TrackLineConfig(
                                        modeConfig.getJSONObject("trackLine"),
                                        dynamicTurnController,
                                        speedController),
                                mRobot.getDriveBase()),
                        new WaitForRegion(new WaitForRegionConfig(
                                modeConfig.getJSONObject("waitForRegion")),
                                mRobot.getDriveBase())
                }),
                new StopDrive(mRobot.getDriveBase()),
                new WaitForDriveStopped(
                        new WaitForDriveStoppedConfig(modeConfig.getJSONObject("stop")),
                        mRobot.getDriveBase()),

                FireHelper.getFireCommand(
                        mRobot.getBlocker(),
                        mRobot.getIntake(),
                        mRobot.getShooter())

        });
    }

    public String getVisibleName() {
        return "Score One Immediately";
    }
}

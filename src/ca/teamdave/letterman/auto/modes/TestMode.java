package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.NoOp;
import ca.teamdave.letterman.auto.commands.drive.*;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.auto.commands.shoot.FireAndRetract;
import ca.teamdave.letterman.config.command.DriveToDistConfig;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.robotcomponents.Robot;
import ca.teamdave.letterman.robotcomponents.Shooter;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * This class is just for testing individual commands
 */
public class TestMode implements AutoMode {
    private final Robot mRobot;
    private final JSONObject mAutoConfig;

    public TestMode(Robot robot, JSONObject config) {
        mRobot = robot;
        mAutoConfig = config;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() throws JSONException {
        PidControllerConfig dynamicTurnController = new PidControllerConfig(
                mAutoConfig.getJSONObject("dynamicTurnPid"));
        PidControllerConfig staticTurnController = new PidControllerConfig(
                mAutoConfig.getJSONObject("staticTurnPid"));
        PidControllerConfig driveController = new PidControllerConfig(
                mAutoConfig.getJSONObject("drivePid"));
        PidControllerConfig speedController = new PidControllerConfig(
                mAutoConfig.getJSONObject("speedPid"));

        return new Series(new AutoCommand[]{
                new FireAndRetract(mRobot.getShooter()),
                new NoOp()
        });
    }

    public String getVisibleName() {
        return "Test Mode";
    }
}

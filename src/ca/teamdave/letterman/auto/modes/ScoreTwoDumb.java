package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.blocker.BlockerTravelPosition;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TrackLine;
import ca.teamdave.letterman.auto.commands.drive.WaitForDriveStopped;
import ca.teamdave.letterman.auto.commands.drive.WaitForRegion;
import ca.teamdave.letterman.auto.commands.hotness.TurnToHotness;
import ca.teamdave.letterman.auto.commands.intake.IntakeLatchIn;
import ca.teamdave.letterman.auto.commands.intake.IntakeRunPickup;
import ca.teamdave.letterman.auto.commands.intake.IntakeShootPosition;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.auto.commands.shoot.FireHelper;
import ca.teamdave.letterman.auto.commands.shoot.StartRetract;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.config.command.WaitForRegionConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.robotcomponents.Robot;
import org.json.me.JSONObject;

/**
 * Score two balls in the same goal during different hotness periods
 */
public class ScoreTwoDumb implements AutoMode {

    private final JSONObject mAutoConfig;
    private final Robot mRobot;

    public ScoreTwoDumb(Robot robot, JSONObject autoConfig) {
        mRobot = robot;
        mAutoConfig = autoConfig;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() throws Exception {
        // Common PID configurations
        PidControllerConfig dynamicTurnControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("dynamicTurnPid"));
        PidControllerConfig staticTurnControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("staticTurnPid"));
        PidControllerConfig driveControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("drivePid"));
        PidControllerConfig speedControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("speedPid"));

        JSONObject modeConfig = mAutoConfig.getJSONObject("scoreTwoDumb");

        // Parameters for driving to the shoot positions
        JSONObject driveOutJson = modeConfig.getJSONObject("driveOut");
        TrackLineConfig driveOutConfig = new TrackLineConfig(
                driveOutJson.getJSONObject("trackLine"),
                dynamicTurnControl,
                speedControl);
        WaitForRegionConfig driveOutRegionConfig = new WaitForRegionConfig(
                driveOutJson.getJSONObject("waitForRegion"));

        WaitForDriveStoppedConfig aimingStopConfig = new WaitForDriveStoppedConfig(
                modeConfig.getJSONObject("stop"));

        // Parameters for driving back to the ball for pickup
        JSONObject driveBackJson = modeConfig.getJSONObject("driveBack");
        TrackLineConfig driveBackConfig = new TrackLineConfig(
                driveBackJson.getJSONObject("trackLine"),
                dynamicTurnControl,
                speedControl);
        WaitForRegionConfig driveBackRegionConfig = new WaitForRegionConfig(
                driveBackJson.getJSONObject("waitForRegion"));

        DriveBase driveBase = mRobot.getDriveBase();

        return new Series(new AutoCommand[] {
                // trap the ball
                new StartRetract(mRobot.getShooter()),

                new Latch(new AutoCommand[]{
                        new Series(new AutoCommand[]{
                                new BlockerTravelPosition(mRobot.getBlocker()),
                                new IntakeShootPosition(mRobot.getIntake()),
                                new Pause(1.5),
                                new IntakeLatchIn(mRobot.getIntake()),
                                new Pause(0.75)
                        }),
                        new Series(new AutoCommand[] {
                                new Latch(new AutoCommand[]{
                                        new TrackLine(driveOutConfig, driveBase),
                                        new WaitForRegion(driveOutRegionConfig, driveBase)
                                }),
                                new StopDrive(driveBase)
                        }),
                }),

                new WaitForDriveStopped(aimingStopConfig, driveBase),

                new StopDrive(driveBase),
                FireHelper.getFireCommand(
                        mRobot.getBlocker(),
                        mRobot.getIntake(),
                        mRobot.getShooter()),

                new Latch(new AutoCommand[]{
                        new BlockerTravelPosition(mRobot.getBlocker()),
                        new IntakeRunPickup(mRobot.getIntake()),
                        new TrackLine(driveBackConfig, driveBase),
                        new WaitForRegion(driveBackRegionConfig, driveBase)
                }),

                new Latch(new AutoCommand[]{
                        new TrackLine(driveOutConfig, driveBase),
                        new WaitForRegion(driveOutRegionConfig, driveBase)
                }),

                new StopDrive(driveBase),
                new WaitForDriveStopped(aimingStopConfig, driveBase),

                new StopDrive(driveBase),
                FireHelper.getFireCommand(
                        mRobot.getBlocker(),
                        mRobot.getIntake(),
                        mRobot.getShooter())
        });
    }

    public String getVisibleName() {
        return "Score Two Dumb";
    }
}

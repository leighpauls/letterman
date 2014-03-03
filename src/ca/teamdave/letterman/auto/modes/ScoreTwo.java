package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.NoOp;
import ca.teamdave.letterman.auto.commands.blocker.BlockerTravelPosition;
import ca.teamdave.letterman.auto.commands.hotness.TriggerHotnessDecision;
import ca.teamdave.letterman.auto.commands.drive.*;
import ca.teamdave.letterman.auto.commands.dummy.DummyShoot;
import ca.teamdave.letterman.auto.commands.hotness.TurnToHotness;
import ca.teamdave.letterman.auto.commands.intake.IntakeLatchIn;
import ca.teamdave.letterman.auto.commands.intake.IntakeRunPickup;
import ca.teamdave.letterman.auto.commands.intake.IntakeShootPosition;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.auto.commands.shoot.FireHelper;
import ca.teamdave.letterman.auto.commands.shoot.StartRetract;
import ca.teamdave.letterman.config.command.*;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.robotcomponents.HotnessTracker;
import ca.teamdave.letterman.robotcomponents.Robot;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Auto mode that does the driving pattern for scoring two balls
 */
public class ScoreTwo implements AutoMode {

    private final JSONObject mAutoConfig;
    private final Robot mRobot;

    public ScoreTwo(Robot robot, JSONObject config) {
        mRobot = robot;
        mAutoConfig = config;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public String getVisibleName() {
        return "Score Two";
    }

    public AutoCommand getRootCommand() throws JSONException {
        // Common PID configurations
        PidControllerConfig dynamicTurnControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("dynamicTurnPid"));
        PidControllerConfig staticTurnControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("staticTurnPid"));
        PidControllerConfig driveControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("drivePid"));
        PidControllerConfig speedControl = new PidControllerConfig(
                mAutoConfig.getJSONObject("speedPid"));

        JSONObject modeConfig = mAutoConfig.getJSONObject("scoreTwo");

        // Parameters for driving to the shoot positions
        JSONObject driveOutJson = modeConfig.getJSONObject("driveOut");
        TrackLineConfig driveOutConfig = new TrackLineConfig(
                driveOutJson.getJSONObject("trackLine"),
                dynamicTurnControl,
                speedControl);
        WaitForRegionConfig driveOutRegionConfig = new WaitForRegionConfig(
                driveOutJson.getJSONObject("waitForRegion"));

        // Parameters for aiming at the goal
        JSONObject aimingJson = modeConfig.getJSONObject("aiming");
        TurnToHeadingConfig aimingTurnConfig = new TurnToHeadingConfig(
                aimingJson.getJSONObject("turn"),
                staticTurnControl);
        WaitForDriveStoppedConfig aimingStopConfig = new WaitForDriveStoppedConfig(
                aimingJson.getJSONObject("stop"));

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
                                new Pause(1.0),
                                new IntakeLatchIn(mRobot.getIntake()),
                                new TriggerHotnessDecision(mRobot.getHotnessTracker()),
                                new Pause(1.0)
                        }),
                        new Series(new AutoCommand[] {
                                new Latch(new AutoCommand[]{
                                        new TrackLine(driveOutConfig, driveBase),
                                        new WaitForRegion(driveOutRegionConfig, driveBase)
                                }),
                                new StopDrive(driveBase)
                        }),
                }),

                new Latch(new AutoCommand[]{
                        new TurnToHotness(aimingTurnConfig, mRobot),
                        new WaitForDriveStopped(aimingStopConfig, driveBase)
                }),

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

                new Latch(new AutoCommand[]{
                        new TurnToHotness(aimingTurnConfig, mRobot),
                        new WaitForDriveStopped(aimingStopConfig, driveBase)
                }),
                new StopDrive(driveBase),
                FireHelper.getFireCommand(
                        mRobot.getBlocker(),
                        mRobot.getIntake(),
                        mRobot.getShooter())
        });
    }
}
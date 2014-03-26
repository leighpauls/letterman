package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.NoOp;
import ca.teamdave.letterman.auto.commands.blocker.BlockerTravelPosition;
import ca.teamdave.letterman.auto.commands.hotness.TriggerHotnessDecision;
import ca.teamdave.letterman.auto.commands.hotness.WaitForHotness;
import ca.teamdave.letterman.auto.commands.intake.IntakeLatchIn;
import ca.teamdave.letterman.auto.commands.intake.IntakeShootPosition;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.shoot.FireHelper;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TrackLine;
import ca.teamdave.letterman.auto.commands.drive.WaitForDriveStopped;
import ca.teamdave.letterman.auto.commands.drive.WaitForRegion;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.auto.commands.shoot.StartRetract;
import ca.teamdave.letterman.config.command.TrackLineConfig;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.config.command.WaitForRegionConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.GoalHotnessState;
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
    private final SideSelection mSideSelection;

    public static class SideSelection extends EnumerationClass {
        protected SideSelection(String name) {
            super(name);
        }

        public static SideSelection LEFT = new SideSelection("Left");
        public static SideSelection RIGHT = new SideSelection("Right");
        public static SideSelection IMMEDIATE = new SideSelection("Immidiate");
    }

    public ScoreOne(Robot robot, JSONObject autoConfig, SideSelection sideSelection) {
        mAutoConfig = autoConfig;
        mRobot = robot;
        mSideSelection = sideSelection;
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
                // trap the ball
                new StartRetract(mRobot.getShooter()),

                new Latch(new AutoCommand[]{
                        new Series(new AutoCommand[] {
                                new BlockerTravelPosition(mRobot.getBlocker()),
                                new IntakeShootPosition(mRobot.getIntake()),
                                new Pause(1.5),
                                new IntakeLatchIn(mRobot.getIntake()),
                                new TriggerHotnessDecision(mRobot.getHotnessTracker()),
                                new Pause(0.75)
                        }),
                        new Series(new AutoCommand[] {
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

                        })
                }),
                new WaitForDriveStopped(
                        new WaitForDriveStoppedConfig(modeConfig.getJSONObject("stop")),
                        mRobot.getDriveBase()),

                (mSideSelection == SideSelection.IMMEDIATE
                        ? (AutoCommand) new NoOp()
                        : new WaitForHotness(
                        mRobot.getHotnessTracker(),
                        (mSideSelection == SideSelection.LEFT
                                ? GoalHotnessState.LEFT_HOT
                                : GoalHotnessState.RIGHT_HOT))),

                FireHelper.getFireCommand(
                        mRobot.getBlocker(),
                        mRobot.getIntake(),
                        mRobot.getShooter())
        });
    }

    public String getVisibleName() {
        return "ScoreOne " + mSideSelection;
    }
}

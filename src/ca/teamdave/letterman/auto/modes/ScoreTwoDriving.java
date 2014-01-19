package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.drive.*;
import ca.teamdave.letterman.auto.commands.dummy.DummyShoot;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.config.command.*;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Auto mode that does the driving pattern for scoring two balls
 */                           
public class ScoreTwoDriving implements AutoMode {

    private final DriveBase mDriveBase;
    private final JSONObject mAutoConfig;

    public ScoreTwoDriving(DriveBase driveBase, JSONObject confg) {
        mDriveBase = driveBase;
        mAutoConfig = confg;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() {
        try {
            // Common PID configurations
            PidControllerConfig dynamicTurnControl = new PidControllerConfig(
                    mAutoConfig.getJSONObject("dynamicTurnPid"));
            PidControllerConfig staticTurnControl = new PidControllerConfig(
                    mAutoConfig.getJSONObject("staticTurnPid"));
            PidControllerConfig driveControl = new PidControllerConfig(
                    mAutoConfig.getJSONObject("drivePid"));
            PidControllerConfig speedControl = new PidControllerConfig(
                    mAutoConfig.getJSONObject("speedPid"));

            JSONObject modeConfig = mAutoConfig.getJSONObject("scoreTwoDriving");
            // Special field positions
            RobotPosition shootPosition = new RobotPosition(
                    modeConfig.getJSONObject("shootPosition"));
            RobotPosition pickupPosition = new RobotPosition(
                    modeConfig.getJSONObject("pickupPosition"));

            // Parameters for driving to the shoot positions
            JSONObject driveOutJson = modeConfig.getJSONObject("driveOut");
            DriveToPointConfig driveOut = new DriveToPointConfig(
                    shootPosition,
                    driveOutJson.getDouble("turnLockDistance"),
                    driveOutJson.getDouble("completeDistance"),
                    dynamicTurnControl,
                    driveControl);

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
            WaitForRegionConfig waitForRegionConfig = new WaitForRegionConfig(
                    driveBackJson.getJSONObject("waitForRegion"));

            return new Series(new AutoCommand[] {
                    new DriveToPoint(driveOut, mDriveBase),
                    new Latch(new AutoCommand[]{
                            new TurnToHeading(aimingTurnConfig, mDriveBase),
                            new WaitForDriveStopped(aimingStopConfig, mDriveBase)
                    }),
                    new StopDrive(mDriveBase),
                    new DummyShoot(),

                    // TODO: run pickup here
                    new Latch(new AutoCommand[] {
                            new TrackLine(driveBackConfig, mDriveBase),
                            new WaitForRegion(waitForRegionConfig, mDriveBase)
                    }),

                    // TODO: stop running pickup here

                    new DriveToPoint(driveOut, mDriveBase),
                    new Latch(new AutoCommand[]{
                            new TurnToHeading(aimingTurnConfig, mDriveBase),
                            new WaitForDriveStopped(aimingStopConfig, mDriveBase)
                    }),
                    new StopDrive(mDriveBase),
                    new DummyShoot()
            });
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to make ScoreTwoDriving");
        }
    }
}
package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.DaveUtils;
import ca.teamdave.letterman.auto.commands.drive.DriveToPoint;
import ca.teamdave.letterman.auto.commands.drive.StopDrive;
import ca.teamdave.letterman.auto.commands.drive.TurnToHeading;
import ca.teamdave.letterman.auto.commands.drive.WaitForDriveStopped;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.config.command.WaitForDriveStoppedConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.auto.commands.*;
import ca.teamdave.letterman.auto.commands.dummy.DummyShoot;
import ca.teamdave.letterman.auto.commands.meta.Pause;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.config.command.DriveToPointConfig;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.config.control.PidControllerConfig;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Auto mode that does the driving pattern for scoring two balls
 */                           
public class ScoreTwoDriving implements AutoMode {

    private final DriveBase mDriveBase;
    private final JSONObject mConfig;

    public ScoreTwoDriving(DriveBase driveBase, JSONObject confg) {
        mDriveBase = driveBase;
        mConfig = confg;
    }

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() {
        try {
            // Common PID configurations
            PidControllerConfig turnPidConfig = new PidControllerConfig(
                    mConfig.getJSONObject("turnPid"));
            PidControllerConfig drivePidConfig = new PidControllerConfig(
                    mConfig.getJSONObject("drivePid"));

            // Special field positions
            RobotPosition shootPosition = new RobotPosition(
                    mConfig.getJSONObject("shootPosition"));
            RobotPosition pickupPosition = new RobotPosition(
                    mConfig.getJSONObject("pickupPosition"));

            // Parameters for driving to the shoot positions
            JSONObject driveOutJson = mConfig.getJSONObject("driveOut");
            DriveToPointConfig driveOut = new DriveToPointConfig(
                    shootPosition,
                    DaveUtils.jsonDouble(driveOutJson, "turnLockDistance"),
                    DaveUtils.jsonDouble(driveOutJson, "completeDistance"),
                    turnPidConfig,
                    drivePidConfig);

            // Parameters for aiming at the goal
            JSONObject aimingTurnJson = mConfig.getJSONObject("aimingTurn");
            double heading = DaveUtils.jsonDouble(aimingTurnJson, "heading");
            double completionErrorAngle = DaveUtils.jsonDouble(
                    aimingTurnJson,
                    "completionErrorAngle");
            double forwardStopSpeed = DaveUtils.jsonDouble(aimingTurnJson, "forwardStopSpeed");
            double turnStopSpeed = DaveUtils.jsonDouble(aimingTurnJson, "turnStopSpeed");

            // Parameters for driving back to the ball for pickup
            JSONObject driveBackJson = mConfig.getJSONObject("driveBack");
            DriveToPointConfig driveBackToBall = new DriveToPointConfig(
                    pickupPosition,
                    DaveUtils.jsonDouble(driveBackJson, "turnLockDistance"),
                    DaveUtils.jsonDouble(driveBackJson, "completeDistance"),
                    turnPidConfig,
                    drivePidConfig);
            double pickupPause = DaveUtils.jsonDouble(mConfig, "pickupPause");

            return new Series(new AutoCommand[] {
                new DriveToPoint(driveOut, mDriveBase),
                new Latch(new AutoCommand[]{
                    new TurnToHeading(
                            new TurnToHeadingConfig(heading, completionErrorAngle, turnPidConfig),
                            mDriveBase),
                    new WaitForDriveStopped(
                            new WaitForDriveStoppedConfig(forwardStopSpeed, turnStopSpeed),
                            mDriveBase)
                }),
                new StopDrive(mDriveBase),
                new DummyShoot(),

                // TODO: run pickup here
                new DriveToPoint(driveBackToBall, mDriveBase),
                new Pause(pickupPause),
                // TODO: stop running pickup here

                new DriveToPoint(driveOut, mDriveBase),
                new Latch(new AutoCommand[]{
                    new TurnToHeading(
                            new TurnToHeadingConfig(-heading, completionErrorAngle, turnPidConfig),
                            mDriveBase),
                    new WaitForDriveStopped(
                            new WaitForDriveStoppedConfig(forwardStopSpeed, turnStopSpeed),
                            mDriveBase)
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
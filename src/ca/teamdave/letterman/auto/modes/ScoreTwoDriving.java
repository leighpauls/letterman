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
            PidControllerConfig turnPidConfig = new PidControllerConfig(mConfig.getJSONObject("turnPid"));
            PidControllerConfig drivePidConfig = new PidControllerConfig(mConfig.getJSONObject("drivePid"));
            
            double turnLockDistanceOut = DaveUtils.jsonDouble(mConfig, "turnLockDistance");
            double completeDistanceOut = DaveUtils.jsonDouble(mConfig, "completeDistance");
            double turnLockDistanceBack = DaveUtils.jsonDouble(mConfig, "turnLockDistanceBack");
            double completeDistanceBack = DaveUtils.jsonDouble(mConfig, "completeDistanceBack");
            double heading = DaveUtils.jsonDouble(mConfig, "heading");
            double completionErrorAngle = DaveUtils.jsonDouble(mConfig, "completionErrorAngle");
            double pause = DaveUtils.jsonDouble(mConfig, "pause");
            double forwardStopSpeed = DaveUtils.jsonDouble(mConfig, "forwardStopSpeed");
            double turnStopSpeed = DaveUtils.jsonDouble(mConfig, "turnStopSpeed");
            
            RobotPosition shootPosition = new RobotPosition(mConfig.getJSONObject("shootPosition"));
            RobotPosition pickupPosition = new RobotPosition (mConfig.getJSONObject("pickupPosition"));
            
            DriveToPointConfig driveOut = new DriveToPointConfig(
                    shootPosition,
                    turnLockDistanceOut,
                    completeDistanceOut,
                    turnPidConfig,
                    drivePidConfig);
            DriveToPointConfig driveBackToBall = new DriveToPointConfig(
                    pickupPosition,
                    turnLockDistanceBack,
                    completeDistanceBack,
                    turnPidConfig,
                    drivePidConfig);
            
            return new Series(new AutoCommand[] {
                // TODO: drive this construction nicely from somewhere
                new DriveToPoint(driveOut, mDriveBase),
                new Latch(new AutoCommand[]{
                    new TurnToHeading(
                            new TurnToHeadingConfig(heading, completionErrorAngle, turnPidConfig),
                            mDriveBase),
                    new WaitForDriveStopped(new WaitForDriveStoppedConfig(0.1, 5), mDriveBase)
                }),
                new StopDrive(mDriveBase),
                new DummyShoot(),

                // TODO: run pickup here
                new DriveToPoint(driveBackToBall, mDriveBase),
                new Pause(pause),
                // TODO: stop running pickup here

                new DriveToPoint(driveOut, mDriveBase),
                new Latch(new AutoCommand[]{
                    new TurnToHeading(
                            new TurnToHeadingConfig(heading, completionErrorAngle, turnPidConfig),
                            mDriveBase),
                    new WaitForDriveStopped(new WaitForDriveStoppedConfig(forwardStopSpeed, turnStopSpeed), mDriveBase)
                }),
                new StopDrive(mDriveBase),
                new DummyShoot()
            });
        } catch (JSONException ex) {
            ex.printStackTrace();
            throw new RuntimeException("FIled to make ScoreTwoDriving");
        }
    }
}
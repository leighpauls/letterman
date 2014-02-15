/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.teamdave.letterman;


import ca.teamdave.letterman.auto.AutoModeRunner;
import ca.teamdave.letterman.auto.AutoModeSelector;
import ca.teamdave.letterman.auto.modes.AutoMode;
import ca.teamdave.letterman.auto.modes.ScoreTwo;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.ConfigLoader;
import ca.teamdave.letterman.config.component.BlockerControlConfig;
import ca.teamdave.letterman.config.component.RobotConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.robotcomponents.Robot;
import ca.teamdave.letterman.robotcomponents.Shooter;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class LettermanMain extends IterativeRobot {
    private Robot mRobot;
    private XboxGamePad mController;
    private AutoModeRunner mAutoModeRunner;
    private AutoModeSelector mAutoModeSelector;
    private DriverStationLCD mDriverStationLCD;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("LETTERMAN INITIALIZING!!!");
        mController = new XboxGamePad(1);
        mDriverStationLCD = DriverStationLCD.getInstance();

        ConfigLoader.getInstance().loadConfigFromFile();
        try {
            mRobot = new Robot(
                    new RobotConfig(ConfigLoader.getInstance().getConfigObject("robotConfig")));
            mAutoModeSelector = new AutoModeSelector(
                    mRobot,
                    ConfigLoader.getInstance().getConfigObject("auto"),
                    mController,
                    mDriverStationLCD);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse robot config");
        }

        mRobot.getShooter().latchStop();
    }


    /** Called once at the start of auto */
    public void autonomousInit() {
        System.out.println("Auto Init running");

        // TODO: before competition; only load the config file on disabled-init
        // get the latest config file version
        ConfigLoader.getInstance().loadConfigFromFile();
        try {
            mAutoModeSelector.resetModes(
                    mRobot,
                    ConfigLoader.getInstance().getConfigObject("auto"));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse robot config");
        }

        // set up the new auto mode
        AutoMode mode = mAutoModeSelector.getSelectedMode();
        mRobot.getDriveBase().reset(mode.getInitialPose());
        mAutoModeRunner = new AutoModeRunner(mode);

        mRobot.getShooter().latchStop();
        mRobot.getIntake().latchIn();
    }
    /** called every 20ms in auto */
    public void autonomousPeriodic() {
        double cycleTime = BackgroundUpdateManager.getInstance().runUpdates(RobotMode.AUTO);
        mRobot.getShooter().latchStop();
        mAutoModeRunner.runCycle(cycleTime);
    }


    /** Called once at the start of teleop */
    public void teleopInit() {
        mRobot.getDriveBase().reset(new RobotPose(new RobotPosition(0, 0), 0));

        // reload the control config
        ConfigLoader.getInstance().loadConfigFromFile();
        try {
            JSONObject blockerControlJson = ConfigLoader.getInstance()
                    .getConfigObject("robotConfig")
                    .getJSONObject("blockerConfig")
                    .getJSONObject("control");
            mRobot.getBlocker().setControlConfig(new BlockerControlConfig(blockerControlJson));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse blocker control config");
        }

        mRobot.getShooter().latchStop();
        mRobot.getIntake().latchIn();
    }
    /** Called every 20ms in teleop */
    public void teleopPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates(RobotMode.TELEOP);

        // drive control
        mRobot.getDriveBase().setArcade(-mController.getYLeft(), mController.getXLeft());
        mRobot.getDriveBase().setGearState(mController.getLeftStickButton()
                ? DriveBase.GearState.LOW_GEAR
                : DriveBase.GearState.HIGH_GEAR);

        // Shooter control
        boolean rightBumper = mController.getRightBumper();
        if (mController.getBackButton()) {
            mRobot.getShooter().latchStop();
        } else if (mController.getStartButton()) {
            mRobot.getShooter().forceFeed();
        } else if (rightBumper) {
            if (mRobot.getIntake().tryShooting()) {
                mRobot.getShooter().tryFiring();
            } else {
                mRobot.getShooter().tryRetracting(true);
            }
        } else {
            mRobot.getShooter().tryRetracting(false);
        }

        // blocker bar control
        double rightStickPosition = mController.getYRight();
        if (mController.getXButton()) {
            mRobot.getBlocker().setTravelPosition();
        } else if (mController.getYButton()) {
            mRobot.getBlocker().setCatchPosition();
        } else if (mController.getBButton()) {
            mRobot.getBlocker().setBlockPosition();
        } else if (Math.abs(rightStickPosition) > 0.3
                || mRobot.getBlocker().isManualControl()) {
            mRobot.getBlocker().setManualControl(rightStickPosition);
        }

        // intake control
        mRobot.getIntake().setRollerAdjustment(-mController.getTriggerDifference());
        if (!rightBumper) {
            if (mController.getDPadLeft()) {
                mRobot.getIntake().latchOut();
            } else if (mController.getDPadRight()) {
                mRobot.getIntake().latchIn();
            } else if (mController.getAButton() || mController.getRightStickButton()) {
                mRobot.getIntake().pickup();
            }
        }

    }


    public void disabledInit() {
        mAutoModeRunner = null;
        mRobot.getShooter().latchStop();
    }
    /** Called every 20ms in disabled */
    public void disabledPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates(RobotMode.DISABLED);
    }
}

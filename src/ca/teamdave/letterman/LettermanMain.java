/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.teamdave.letterman;


import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import ca.teamdave.letterman.config.component.RobotConfig;
import ca.teamdave.letterman.robotcomponents.Robot;
import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class LettermanMain extends IterativeRobot {
    private Robot mRobot;
    private Joystick mController;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        mRobot = new Robot(RobotConfig.CONFIG);
        mController = new Joystick(1);
    }

    /** Called once at the start of auto */
    public void autonomousInit() {
        mRobot.getDriveBase().reset(new RobotPose(new RobotPosition(0, 0), 0));
    }
    /** called every 20ms in auto */
    public void autonomousPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
        mRobot.getDriveBase().setArcade(0, 0);
    }


    /** Called once at the start of teleop */
    public void teleopInit() {
        mRobot.getDriveBase().reset(new RobotPose(new RobotPosition(0, 0), 0));
    }
    /** Called every 20ms in teleop */
    public void teleopPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
        mRobot.getDriveBase().setArcade(-mController.getY(), mController.getX());
    }


    /** Called every 20ms in disabled */
    public void disabledPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
    }
}

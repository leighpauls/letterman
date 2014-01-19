/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package ca.teamdave.letterman;


import ca.teamdave.letterman.auto.AutoModeRunner;
import ca.teamdave.letterman.auto.modes.AutoMode;
import ca.teamdave.letterman.auto.modes.ScoreTwoDriving;
import ca.teamdave.letterman.auto.modes.TestMode;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.config.ConfigLoader;
import ca.teamdave.letterman.config.component.RobotConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.Robot;
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
    private AutoMode[] mAutoModeOptions;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        ConfigLoader.getInstance().loadConfigFromFile();
        try {
            mRobot = new Robot(new RobotConfig(ConfigLoader.getInstance().getConfigObject("robotConfig")));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse robot config");
        }
        mController = new XboxGamePad(1);
    }


    /** Called once at the start of auto */
    public void autonomousInit() {
        System.out.println("Auto Init running");
        ConfigLoader.getInstance().loadConfigFromFile();
        AutoMode mode;
        try{
            mode = new TestMode(
                    mRobot.getDriveBase(),
                    ConfigLoader.getInstance().getConfigObject("auto"));
        
        }
        catch(JSONException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to parse a robot config");
        }
            // TODO: pick this mode from a list
            // AutoMode mode = new TestMode(mRobot.getDriveBase());
        mRobot.getDriveBase().reset(mode.getInitialPose());
        mAutoModeRunner = new AutoModeRunner(mode);
    }
    /** called every 20ms in auto */
    public void autonomousPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
        // TODO: actually measure deltaTime, rather than assume it's right
        mAutoModeRunner.runCycle(0.02);
    }


    /** Called once at the start of teleop */
    public void teleopInit() {
        mRobot.getDriveBase().reset(new RobotPose(new RobotPosition(0, 0), 0));
    }
    /** Called every 20ms in teleop */
    public void teleopPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
        mRobot.getDriveBase().setArcade(-mController.getYLeft(), mController.getXLeft());
    }


    public void disabledInit() {
        mAutoModeRunner = null;
        ConfigLoader.getInstance().loadConfigFromFile();
        // TODO: can I create a new robot here without WPILibJ going TU?
        try {
            // reload auto mode configs
            JSONObject autoConfig = ConfigLoader.getInstance().getConfigObject("auto");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Filed to parse auto mode config");
        }
    }
    /** Called every 20ms in disabled */
    public void disabledPeriodic() {
        BackgroundUpdateManager.getInstance().runUpdates();
    }
}

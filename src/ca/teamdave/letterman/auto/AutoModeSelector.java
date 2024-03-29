package ca.teamdave.letterman.auto;

import ca.teamdave.letterman.XboxGamePad;
import ca.teamdave.letterman.auto.modes.*;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.robotcomponents.Robot;
import edu.wpi.first.wpilibj.DriverStationLCD;
import org.json.me.JSONObject;

/**
 * Selects an auto mode
 */
public class AutoModeSelector implements BackgroundUpdatingComponent {
    private final XboxGamePad mGamePad;
    private final DriverStationLCD mDriverStationLCD;
    private AutoMode[] mAutoModes;
    private int mCurMode;
    private boolean mWasButtonDown;

    public AutoModeSelector(
            Robot robot,
            JSONObject autoConfig,
            XboxGamePad gamePad,
            DriverStationLCD driverStationLCD) {
        mGamePad = gamePad;
        mDriverStationLCD = driverStationLCD;

        resetModes(robot, autoConfig);

        mCurMode = 0;
        mWasButtonDown = false;
        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void resetModes(Robot robot, JSONObject autoConfig) {
        mAutoModes = new AutoMode[] {
                new ScoreTwoDumb(robot, autoConfig),
                new ScoreOne(robot, autoConfig, ScoreOne.SideSelection.IMMEDIATE),
                new ScoreOne(robot, autoConfig, ScoreOne.SideSelection.LEFT),
                new ScoreOne(robot, autoConfig, ScoreOne.SideSelection.RIGHT),
                new DriveStraight(robot, autoConfig),
                new TestMode(robot, autoConfig),
        };
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        // print the mode to the driver station LCD
        mDriverStationLCD.println(
                DriverStationLCD.Line.kUser1,
                1,
                "Mode " + mCurMode + ": " + getSelectedMode().getVisibleName() + "               ");
        mDriverStationLCD.updateLCD();

        if (mode != RobotMode.DISABLED) {
            // only select modes while disabled
            return;
        }

        if (mWasButtonDown) {
            if (!(mGamePad.getDPadLeft() || mGamePad.getDPadRight())) {
                mWasButtonDown = false;
            }
            return;
        }

        if (mGamePad.getDPadLeft()) {
            mCurMode--;
            if (mCurMode < 0) {
                mCurMode = mAutoModes.length - 1;
            }
            mWasButtonDown = true;
        } else if (mGamePad.getDPadRight()) {
            mCurMode++;
            if (mCurMode >= mAutoModes.length) {
                mCurMode = 0;
            }
            mWasButtonDown = true;
        }
    }

    public AutoMode getSelectedMode() {
        return mAutoModes[mCurMode];
    }
}

package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.descriptors.GoalHotnessState;
import ca.teamdave.letterman.robotcomponents.camera.CameraThread;
import ca.teamdave.letterman.robotcomponents.camera.PairTargetResult;
import ca.teamdave.letterman.robotcomponents.camera.StaticTargetResult;
import ca.teamdave.letterman.robotcomponents.camera.TargetSide;
import edu.wpi.first.wpilibj.DriverStationLCD;

/**
 * Handles the detection of the hot goal target
 */
public class HotnessTracker implements BackgroundUpdatingComponent {

    private final CameraThread.TargetInfoDelegate mTargetInfoDelegate;
    private GoalHotnessState mVisibleGoalHotness;
    private GoalHotnessState mInferredGoalHotness;
    private double mPrevModeTime;

    // TODO: move into config file
    /** Time interval into auto mode when the initial goal hotness should be declared */
    public static final double HOTNESS_TRIGGER_TIME = 1.0;

    public HotnessTracker() {
        CameraThread cameraThread = new CameraThread();
        mTargetInfoDelegate = cameraThread.getTargetInfoDelegate();
        mVisibleGoalHotness = GoalHotnessState.UNCERTAIN;
        mInferredGoalHotness = GoalHotnessState.UNCERTAIN;
        mPrevModeTime = 0;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    boolean firstSignal = true;

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        // get the latest target info from the camera thread
        PairTargetResult pairResult = mTargetInfoDelegate.getLatestTargetPairInfo();
        StaticTargetResult staticResult = mTargetInfoDelegate.getStaticTargetResult();

        // interpret that info in terms of goal hotness
        if (pairResult.score < PairTargetResult.NOISE_SCORE_THRESHOLD) {
            if (firstSignal) {
                firstSignal = false;
                System.out.println("Got target pair signal!");
            }
            if (pairResult.side == TargetSide.LEFT) {
                mVisibleGoalHotness = GoalHotnessState.LEFT_HOT;
            } else {
                mVisibleGoalHotness = GoalHotnessState.RIGHT_HOT;
            }
        } else if (staticResult.score < StaticTargetResult.NOISE_SCORE_THRESHOLD) {
            if (pairResult.side == TargetSide.LEFT) {
                mVisibleGoalHotness = GoalHotnessState.RIGHT_HOT;
            } else {
                mVisibleGoalHotness = GoalHotnessState.LEFT_HOT;
            }
        } else {
            mVisibleGoalHotness = GoalHotnessState.UNCERTAIN;
        }

        if (modeTime >= 5 && mPrevModeTime < 5 && mode == RobotMode.AUTO) {
            System.out.println("Flipping inferred hotness");
            // flip the inferred hot goal
            if (mInferredGoalHotness == GoalHotnessState.LEFT_HOT) {
                mInferredGoalHotness = GoalHotnessState.RIGHT_HOT;
            } else if (mInferredGoalHotness == GoalHotnessState.RIGHT_HOT) {
                mInferredGoalHotness = GoalHotnessState.LEFT_HOT;
            }
        }

        DriverStationLCD.getInstance().println(
                DriverStationLCD.Line.kUser4,
                1,
                "I: " + mInferredGoalHotness + " V: " + mVisibleGoalHotness + "         ");
        mPrevModeTime = modeTime;
    }

    public GoalHotnessState getVisibleGoalHotness() {
        return mVisibleGoalHotness;
    }

    /**
     * Call to make a decision about what the hot goal is for this auto mode
     */
    public void triggerModeHotnessDecision() {
        mInferredGoalHotness = mVisibleGoalHotness;
        System.out.println("Triggering initial hotness to: " + mInferredGoalHotness);
    }

    public GoalHotnessState getInferredGoalHotness() {
        return mInferredGoalHotness;
    }
}

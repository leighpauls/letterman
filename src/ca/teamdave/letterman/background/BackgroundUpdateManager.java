package ca.teamdave.letterman.background;

import ca.teamdave.letterman.DaveUtils;

import java.util.Vector;

/**
 * Makes sure that "background" updates get run
 */
public class BackgroundUpdateManager {
    private static BackgroundUpdateManager sInstance = null;

    private final Vector mComponents;
    private double mModeStartTime;
    private double mPrevCycleTime;
    private RobotMode mPrevMode;

    private BackgroundUpdateManager() {
        mComponents = new Vector();
        mPrevCycleTime = mModeStartTime = DaveUtils.systemTimeSeconds();
        mPrevMode = RobotMode.DISABLED;
    }

    public static BackgroundUpdateManager getInstance() {
        if (sInstance == null) {
            sInstance = new BackgroundUpdateManager();
        }
        return sInstance;
    }

    public void registerComponent(BackgroundUpdatingComponent updatingComponent) {
        mComponents.addElement(updatingComponent);
    }

    /**
     * Run all of the background tasks
     * @param curMode The current mode of the robot
     * @return The period of the last cycle
     */
    public double runUpdates(RobotMode curMode) {
        double cycleTime = DaveUtils.systemTimeSeconds();
        double cyclePeriod = cycleTime - mPrevCycleTime;
        if (mPrevMode != curMode) {
            mPrevMode = curMode;
            mModeStartTime = cycleTime;
        }

        for (int i = 0; i < mComponents.size(); ++i) {
            BackgroundUpdatingComponent component =
                    (BackgroundUpdatingComponent) mComponents.elementAt(i);
            component.updateComponent(
                    curMode,
                    cycleTime - mModeStartTime,
                    cyclePeriod);
        }

        mPrevCycleTime = cycleTime;

        return cyclePeriod;
    }
}

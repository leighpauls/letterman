package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.DaveUtils;
import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.component.ShooterConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

/**
 * Controls the catapult cam
 */
public class Shooter implements BackgroundUpdatingComponent {
    // outputs
    private final Victor[] mVictors;
    private final boolean mOutputInverted;

    // input params
    private final DigitalInput mReadyPositionSwitch;
    private final double mPostFirePauseThreshold;
    private final double mLostReadinessThreshold;
    private final double mSwitchDebounceTimeThreshold;

    // control state
    private FiringState mState;
    private double mPostFirePauseCount;
    private double mLostReadinessCount;
    private double mSwitchDebounceTimeCount;

    public Shooter(ShooterConfig config) {
        mVictors = DaveUtils.getVictorSet(config.victors);
        mOutputInverted = config.outputInverted;

        mReadyPositionSwitch = new DigitalInput(config.limitSwitch);

        mPostFirePauseThreshold = config.postFirePause;
        mLostReadinessThreshold = config.lostReadinessTime;
        mSwitchDebounceTimeThreshold = config.switchDebounceTime;

        mPostFirePauseCount = 0;
        mLostReadinessCount = 0;
        mSwitchDebounceTimeCount = 0;

        mState = FiringState.LATCHED_STOP;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    private static class FiringState extends EnumerationClass {
        private FiringState(String name) {
            super(name);
        }
        public static final FiringState FIRING = new FiringState("firing");
        public static final FiringState RETRACTING = new FiringState("retracting");
        public static final FiringState READY_TO_FIRE = new FiringState("ready_to_fire");
        public static final FiringState FORCE_FEED = new FiringState("force_feed");
        public static final FiringState LATCHED_STOP = new FiringState("latched_stop");

    }

    private void setVictors(double power) {
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i].set(power * (mOutputInverted ? -1.0 : 1.0));
        }
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        // update the state machine
        if (mState == FiringState.FIRING) {
            if (mReadyPositionSwitch.get()) {
                // the cam hasn't released yet, drive further forward
                setVictors(1.0);
                mPostFirePauseCount = 0.0;
            } else {
                // the cam has released
                setVictors(1.0);
                mPostFirePauseCount += deltaTime;
                if (mPostFirePauseCount >= mPostFirePauseThreshold) {
                    mState = FiringState.RETRACTING;
                    mSwitchDebounceTimeCount = 0;
                }
            }
        } else if (mState == FiringState.RETRACTING) {
            if (mReadyPositionSwitch.get()) {
                mSwitchDebounceTimeCount += deltaTime;
                if (mSwitchDebounceTimeCount >= mSwitchDebounceTimeThreshold) {
                    // I'm in the ready position
                    setVictors(0.0);
                    mState = FiringState.READY_TO_FIRE;
                    mLostReadinessCount = 0;
                }
            } else {
                // keep retracting
                setVictors(1.0);
                mSwitchDebounceTimeCount = 0;
            }
        } else if (mState == FiringState.READY_TO_FIRE) {
            setVictors(0.0);
            if (mReadyPositionSwitch.get()) {
                mLostReadinessCount = 0;
            } else {
                mLostReadinessCount += deltaTime;
                if (mLostReadinessCount > mLostReadinessThreshold) {
                    mState = FiringState.RETRACTING;
                    mSwitchDebounceTimeCount = 0;
                    mLostReadinessCount = 0;
                }
            }
        } else if (mState == FiringState.FORCE_FEED) {
            setVictors(0.7);
            // immediately flip back to the stopped state, so this state must be forced each cycle
            mState = FiringState.LATCHED_STOP;
        } else if (mState == FiringState.LATCHED_STOP) {
            setVictors(0.0);
            // don't exit this state on my own
        } else {
            System.err.println("Unhandled firing state: " + mState);
        }
    }

    /**
     * Tries to start a new firing process
     * @return true iff the shooter is now in the process of firing
     */
    public boolean tryFiring() {
        if (mState == FiringState.FIRING) {
            // already firing
            return true;
        }
        if (mState == FiringState.RETRACTING) {
            // can't start firing yet
            return false;
        }
        if (mState == FiringState.LATCHED_STOP) {
            // just try to feed
            mSwitchDebounceTimeCount = 0;
            mState = FiringState.RETRACTING;
            return false;
        }

        mState = FiringState.FIRING;
        mPostFirePauseCount = 0;

        return true;
    }

    /**
     * Force the catapult to feed. The first cycle for which this isn't called will put the catapult
     * into latched stop mode
     */
    public void forceFeed() {
        mState = FiringState.FORCE_FEED;
    }

    /**
     * Force the cam to stop feeding, and latch it like that. A call to tryFiring(), forceFeed(), or
     * tryRetracting() will pull it out of that state
     */
    public void latchStop() {
        mState = FiringState.LATCHED_STOP;
    }

    /**
     * Try kicking the shooter into retracting mode.
     * @return true iff the shooter is now retracting
     */
    public boolean tryRetracting(boolean forceUnlatch) {
        if (mState == FiringState.READY_TO_FIRE
                || mState == FiringState.LATCHED_STOP
                || (mState == FiringState.FORCE_FEED && !forceUnlatch)) {
            // won't override these states to retract
            return false;
        }
        if (mState != FiringState.RETRACTING) {
            mState = FiringState.RETRACTING;
            mSwitchDebounceTimeCount = 0;
        }
        return true;
    }
}

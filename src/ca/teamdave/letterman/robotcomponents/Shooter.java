package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.component.ShooterConfig;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * Controls the catapult cam
 */
public class Shooter implements BackgroundUpdatingComponent {
    private final Victor[] mVictors;
    private final Encoder mEncoder;
    private final DigitalInput mReadyPositionSwitch;
    private final double mPostFirePauseThreshold;
    private final double mLostReadynessThreshold;


    private FiringState mState;
    private double mPostFirePauseCount;
    private double mLostReadinessCount;


    public Shooter(ShooterConfig config) {
        mVictors = new Victor[config.victors.length];
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i] = new Victor(config.victors[i]);
        }
        mEncoder = new Encoder(config.encoderA, config.encoderB, config.inputInverted);
        mEncoder.setDistancePerPulse(1 / config.ticksPerDegree);
        mReadyPositionSwitch = new DigitalInput(config.limitSwitch);

        mPostFirePauseThreshold = config.postFirePause;
        mLostReadynessThreshold = config.lostReadynessTime;

        mPostFirePauseCount = 0;
        mLostReadinessCount = 0;

        mState = FiringState.READY_TO_FIRE;
    }

    private static class FiringState extends EnumerationClass {
        private FiringState(String name) {
            super(name);
        }

        public static final FiringState FIRING = new FiringState("firing");
        public static final FiringState RETRACTING = new FiringState("retracting");
        public static final FiringState READY_TO_FIRE= new FiringState("ready_to_fire");
    }

    private void setVictors(double power) {
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i].set(power);
        }
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        // update the state machine
        if (mState == FiringState.FIRING) {
            if (mReadyPositionSwitch.get()) {
                // the cam hasn't release yet, drive further forward
                setVictors(1.0);
                mPostFirePauseCount = 0.0;
            } else {
                // the cam has released
                setVictors(0.0);
                mPostFirePauseCount += deltaTime;
                if (mPostFirePauseCount >= mPostFirePauseThreshold) {
                    mState = FiringState.RETRACTING;
                }
            }
        } else if (mState == FiringState.RETRACTING) {
            if (mReadyPositionSwitch.get()) {
                // I'm in the ready position
                setVictors(0.0);
                mState = FiringState.READY_TO_FIRE;
                mLostReadinessCount = 0;
            } else {
                // keep retracting
                setVictors(1.0);
            }
        } else if (mState == FiringState.READY_TO_FIRE) {
            setVictors(0.0);
            if (mReadyPositionSwitch.get()) {
                mLostReadinessCount = 0;
            } else {
                mLostReadinessCount += deltaTime;
                if (mLostReadinessCount > mLostReadynessThreshold) {
                    mState = FiringState.RETRACTING;
                    mLostReadinessCount = 0;
                }
            }
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

        mState = FiringState.FIRING;
        mPostFirePauseCount = 0;

        return true;
    }
}

package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.component.IntakeConfig;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;

/**
 * Controls the intake roller and arm
 */
public class Intake implements BackgroundUpdatingComponent {
    // outputs
    private final Victor mRollerVictor;
    private final Solenoid mArmInSolenoid;
    private final Solenoid mArmOutSolenoid;

    // Physical Constants
    private final double mRetractionTime;
    private final double mExtensionTime;

    // Control state
    private IntakeState mState;
    private double mRollerAdjustment;

    private double mRetractionCount;
    private double mExtensionCount;

    public Intake(IntakeConfig config) {
        mRollerVictor = new Victor(config.victorChannel);
        mArmInSolenoid = new Solenoid(config.armInRelay);
        mArmOutSolenoid = new Solenoid(config.armOutRelay);

        mRetractionTime = config.retractionTime;
        mExtensionTime = config.extensionTime;

        mState = IntakeState.TRAVEL;
        mRollerAdjustment = 0;

        mRetractionCount = 0;
        mExtensionCount = 0;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    private static class IntakeState extends EnumerationClass {
        protected IntakeState(String name) {
            super(name);
        }
        public static final IntakeState TRAVEL = new IntakeState("travel");
        public static final IntakeState PICKUP = new IntakeState("pickup");
        public static final IntakeState PICKUP_RETRACTING = new IntakeState("pickup_retracting");
        public static final IntakeState SHOOT_EXTENDING = new IntakeState("shoot_extending");
        public static final IntakeState SHOOT_HOLDING = new IntakeState("shoot_holding");
        public static final IntakeState SHOOT_RETRACTING = new IntakeState("shoot_holding");
        public static final IntakeState LATCHED_OUT = new IntakeState("latched_out");
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        double rollerPower;
        boolean armOut;
        if (mState == IntakeState.TRAVEL) {
            rollerPower = 0;
            armOut = false;
        } else if (mState == IntakeState.PICKUP) {
            rollerPower = 0.5;
            armOut = true;
            // put into retracting mode to force PICKUP to be momentary
            mState = IntakeState.PICKUP_RETRACTING;
            mRetractionCount = 0;
        } else if (mState == IntakeState.PICKUP_RETRACTING) {
            rollerPower = 1.0;
            armOut = false;
            mRetractionCount += deltaTime;
            if (mRetractionCount > mRetractionTime) {
                mState = IntakeState.TRAVEL;
            }
        } else if (mState == IntakeState.SHOOT_EXTENDING) {
            rollerPower = 0.25;
            armOut = true;
            mExtensionCount += deltaTime;
            if (mExtensionCount > mExtensionTime) {
                mState = IntakeState.SHOOT_HOLDING;
            }
        } else if (mState == IntakeState.SHOOT_HOLDING) {
            rollerPower = 0;
            armOut = true;
            // put into retracting mode to force SHOOT_HOLDING to be momentary
            mState = IntakeState.SHOOT_RETRACTING;
        } else if (mState == IntakeState.SHOOT_RETRACTING) {
            rollerPower = 0;
            armOut = false;
            mState = IntakeState.TRAVEL;
        } else { // (mState == IntakeState.LATCHED_OUT)
            rollerPower = 0;
            armOut = true;
        }

        // scale the roller control so the maximum values can still be hit
        double scaledAdjustment;
        if (mRollerAdjustment > 0) {
            scaledAdjustment = mRollerAdjustment * (1.0 - rollerPower);
        } else {
            scaledAdjustment = mRollerAdjustment * (rollerPower - (-1.0));
        }

        // set the outputs
        mRollerVictor.set(rollerPower + scaledAdjustment);
        mArmOutSolenoid.set(armOut);
        mArmInSolenoid.set(!armOut);
    }

    public void pickup() {
        mState = IntakeState.PICKUP;
    }

    /**
     * Starts the shooting sequence
     * @return true iff the pickup is ready to shoot
     */
    public boolean tryShooting() {
        if (mState == IntakeState.SHOOT_HOLDING || mState == IntakeState.SHOOT_RETRACTING) {
            mState = IntakeState.SHOOT_HOLDING;
            return true;
        }
        if (mState == IntakeState.SHOOT_EXTENDING) {
            // already part-way through a shoot extension
            return false;
        }
        // start a new extension
        mState = IntakeState.SHOOT_EXTENDING;
        mExtensionCount = 0;
        return false;
    }

    public void latchOut() {
        mState = IntakeState.LATCHED_OUT;
    }

    public void latchIn() {
        mState = IntakeState.TRAVEL;
    }

    public void setRollerAdjustment(double rollerAdjustment) {
        mRollerAdjustment = rollerAdjustment;
    }

    public boolean isClearForShot() {
        return mState == IntakeState.SHOOT_HOLDING;
    }
}

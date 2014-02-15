package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.DaveUtils;
import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.PidController;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.component.BlockerConfig;
import ca.teamdave.letterman.config.component.BlockerControlConfig;
import ca.teamdave.letterman.config.component.BlockerHardwareConfig;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Victor;

/**
 * Controls the blocker bar
 */
public class Blocker implements BackgroundUpdatingComponent {
    // hardware fields
    private final Victor[] mVictors;
    private final AnalogChannel mPotentiometer;

    // control parameters
    private double mDegreesPerVolt;
    private double mOffsetDegrees;
    private boolean mInvertOutput;
    private PidController mPidController;

    // setpoint parameters
    private double mBlockPosition, mCatchPosition, mTravelPosition;
    private double mShotClearancePosition;

    // control state info
    private BlockerSetPoint mTargetSetPoint;


    private static class BlockerSetPoint extends EnumerationClass {
        protected BlockerSetPoint(String name) {
            super(name);
        }
        public static final BlockerSetPoint MANUAL_CONTROL = new BlockerSetPoint("manual_control");
        public static final BlockerSetPoint BLOCK = new BlockerSetPoint("block");
        public static final BlockerSetPoint CATCH = new BlockerSetPoint("catch");
        public static final BlockerSetPoint TRAVEL = new BlockerSetPoint("travel");
    }

    public Blocker(BlockerConfig config) {
        mVictors = DaveUtils.getVictorSet(config.hardwareConfig.victors);
        mPotentiometer = new AnalogChannel(config.hardwareConfig.potChannel);

        setControlConfig(config.controlConfig);

        mTargetSetPoint = BlockerSetPoint.TRAVEL;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void setControlConfig(BlockerControlConfig config) {
        mDegreesPerVolt = config.degreesPerVolt;
        mOffsetDegrees = config.offsetDegrees;
        mInvertOutput = config.invertOutput;

        mPidController = new PidController(config.pidConfig);
        mBlockPosition = config.blockPosition;
        mCatchPosition = config.catchPosition;
        mTravelPosition = config.travelPosition;
        mShotClearancePosition = config.shotClearancePosition;

        if (mTargetSetPoint != BlockerSetPoint.MANUAL_CONTROL) {
            mPidController.reset(getSetPoint(), getBlockerPosition());
        }
    }

    private void setVictors(double power) {
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i].set(power * (mInvertOutput ? -1.0 : 1.0));
        }
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
        System.out.println("Blocker Pos: " + getBlockerPosition());
        if (mTargetSetPoint == BlockerSetPoint.MANUAL_CONTROL) {
            return;
        }
        // PID control to the current set point
        double output = mPidController.update(deltaTime, getSetPoint(), getBlockerPosition());
        output = Math.min(0.5, Math.max(-0.5, output));
        setVictors(output);
    }

    private double getBlockerPosition() {
        return mPotentiometer.getAverageVoltage() * mDegreesPerVolt + mOffsetDegrees;
    }

    private double getSetPoint() {
        if (mTargetSetPoint == BlockerSetPoint.BLOCK) {
            return mBlockPosition;
        } else if (mTargetSetPoint == BlockerSetPoint.CATCH) {
            return mCatchPosition;
        } else if (mTargetSetPoint == BlockerSetPoint.TRAVEL) {
            return mTravelPosition;
        }
        System.err.println("Asking for setpoint while in manual mode, returning travel by default");
        return mTravelPosition;
    }

    public void setManualControl(double power) {
        mTargetSetPoint = BlockerSetPoint.MANUAL_CONTROL;
        power = Math.min(0.5, Math.max(-0.5, power));
        setVictors(power);
    }
    public void setBlockPosition() {
        if (mTargetSetPoint != BlockerSetPoint.BLOCK) {
            mTargetSetPoint = BlockerSetPoint.BLOCK;
            mPidController.reset(getSetPoint(), getBlockerPosition());
        }
    }
    public void setCatchPosition() {
        if (mTargetSetPoint != BlockerSetPoint.CATCH) {
            mTargetSetPoint = BlockerSetPoint.CATCH;
            mPidController.reset(getSetPoint(), getBlockerPosition());
        }
    }
    public void setTravelPosition() {
        if (mTargetSetPoint != BlockerSetPoint.TRAVEL) {
            mTargetSetPoint = BlockerSetPoint.TRAVEL;
            mPidController.reset(getSetPoint(), getBlockerPosition());
        }
    }

    public boolean isManualControl() {
        return mTargetSetPoint == BlockerSetPoint.MANUAL_CONTROL;
    }

    /**
     * @return true iff the blocker is low enough to shoot over
     */
    public boolean isClearForShot() {
        return getBlockerPosition() > mShotClearancePosition;
    }
}

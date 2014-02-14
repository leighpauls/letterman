package ca.teamdave.letterman.auto.commands.shoot;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Shooter;

/**
 * Command for firing the catapult, use in series after a {@link WaitForBlockerIntakeClearance} for
 * safety
 */
public class FireAndRetract implements AutoCommand {
    private final Shooter mShooter;
    private int mShotId;
    private boolean mFired;

    public FireAndRetract(Shooter shooter) {
        mShooter = shooter;
        mFired = false;
    }

    public void firstStep() {
        mShotId = mShooter.getCurShotId();
    }

    public Completion runStep(double deltaTime) {
        if (!mFired) {
            mShooter.tryFiring();
            if (mShooter.getCurShotId() == mShotId) {
                // still trying to fire this shot
                return Completion.RUNNING;
            }
            // the next shot has started retracting
            mFired = true;
        }

        // I've finished shooting, just put myself into a safe state
        mShooter.tryRetracting(true);
        return Completion.FINISHED;
    }
}

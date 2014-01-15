package ca.teamdave.letterman.auto;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.modes.AutoMode;

/**
 * Runs the selected autonomous mode
 */
public class AutoModeRunner {
    private final AutoCommand mRootCommand;
    private boolean mFistCycle;

    public AutoModeRunner(AutoMode mode) {
        mRootCommand = mode.getRootCommand();
        mFistCycle = true;
    }

    public void runCycle(double deltaTime) {
        if (mFistCycle) {
            mRootCommand.firstStep();
            mFistCycle = false;
        }
        mRootCommand.runStep(deltaTime);
    }
}

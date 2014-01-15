package ca.teamdave.letterman.auto.commands.meta;

import ca.teamdave.letterman.auto.commands.AutoCommand;

/**
 * Executes child commands in parallel. Each child command will be executed on every cycle until
 * all child commands have returned FINISHED at least once.
 */
public class Latch implements AutoCommand {
    private final AutoCommand[] mCommands;
    private final boolean[] mCompletedCommands;
    private int mRemainingCommands;

    public Latch(AutoCommand[] commands) {
        mCommands = commands;
        mCompletedCommands = new boolean[mCommands.length];
        mRemainingCommands = mCommands.length;
    }

    public void firstStep() {
        for (int i = 0; i < mCommands.length; ++i) {
            mCommands[i].firstStep();
        }
    }

    public Completion runStep(double deltaTime) {
        for (int i = 0; i < mCommands.length; ++i) {
            // has the command completed for the first time?
            if (mCommands[i].runStep(deltaTime) == Completion.FINISHED && !mCompletedCommands[i]) {
                mCompletedCommands[i] = true;
                mRemainingCommands--;
            }
        }
        return mRemainingCommands == 0 ? Completion.FINISHED : Completion.RUNNING;
    }
}

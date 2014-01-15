package ca.teamdave.letterman.auto.commands.meta;

import ca.teamdave.letterman.auto.commands.AutoCommand;

/**
 * Executes a set of commands in series.
 * If this command is continued to be run after it returns FINISHED, it will keep executing the
 * last command.
 */
public class Series implements AutoCommand {
    private final AutoCommand[] mCommands;
    private int mCurCommand;
    private boolean mCommandFirstStep;

    public Series(AutoCommand[] commands) {
        mCommands = commands;
        mCurCommand = 0;
        mCommandFirstStep = true;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        if (mCommandFirstStep) {
            mCommands[mCurCommand].firstStep();
            mCommandFirstStep = false;
        }
        if (mCommands[mCurCommand].runStep(deltaTime) == Completion.FINISHED) {
            // are there more commands after this one?
            if (mCurCommand + 1 < mCommands.length) {
                mCurCommand++;
                mCommandFirstStep = true;
            } else {
                return Completion.FINISHED;
            }
        }
        return Completion.RUNNING;
    }
}

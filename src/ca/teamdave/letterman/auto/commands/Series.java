package ca.teamdave.letterman.auto.commands;

/**
 * Executes a set of commands in series
 */
public class Series implements AutoCommand {
    private final AutoCommand[] mCommands;
    int mCurCommand;

    public Series(AutoCommand[] commands) {
        mCommands = commands;
        mCurCommand = 0;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        if (mCommands[mCurCommand].runStep(deltaTime) == Completion.FINISHED) {
            if (mCurCommand + 1 < mCommands.length) {
                mCurCommand++;
            } else {
                return Completion.FINISHED;
            }
        }
        return Completion.RUNNING;
    }
}

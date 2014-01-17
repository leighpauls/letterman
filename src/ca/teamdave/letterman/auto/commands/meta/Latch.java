package ca.teamdave.letterman.auto.commands.meta;

import ca.teamdave.letterman.auto.commands.AutoCommand;

/**
 * Executes child commands in parallel. Each child command will be executed on every cycle until
 * all child commands return FINISHED on the same cycle
 */
public class Latch implements AutoCommand {
    private final AutoCommand[] mCommands;

    public Latch(AutoCommand[] commands) {
        mCommands = commands;
    }

    public void firstStep() {
        for (int i = 0; i < mCommands.length; ++i) {
            mCommands[i].firstStep();
        }
    }

    public Completion runStep(double deltaTime) {
        int numFinished = 0;
        for (int i = 0; i < mCommands.length; ++i) {
            if (mCommands[i].runStep(deltaTime) == Completion.FINISHED) {
                numFinished++;
            }
        }

        if (numFinished == mCommands.length) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

package ca.teamdave.letterman.auto.commands.blocker;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Blocker;

/**
 * Set the blocker to move to Catch position, will always be finished
 */
public class BlockerCatchPosition implements AutoCommand {
    private final Blocker mBlocker;

    public BlockerCatchPosition(Blocker blocker) {
        mBlocker = blocker;
    }

    public void firstStep() {}

    public AutoCommand.Completion runStep(double deltaTime) {
        mBlocker.setCatchPosition();
        return Completion.FINISHED;
    }
}

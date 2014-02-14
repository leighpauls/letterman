package ca.teamdave.letterman.auto.commands.blocker;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Blocker;

/**
 * Set the blocker to move to block position, will always be finished
 */
public class BlockerBlockPosition implements AutoCommand {
    private final Blocker mBlocker;

    public BlockerBlockPosition(Blocker blocker) {
        mBlocker = blocker;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        mBlocker.setBlockPosition();
        return Completion.FINISHED;
    }
}

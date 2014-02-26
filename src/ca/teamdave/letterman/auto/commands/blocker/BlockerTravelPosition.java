package ca.teamdave.letterman.auto.commands.blocker;


import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Blocker;

/**
 * Set the blocker to move to travel position, will always be finished
 */
public class BlockerTravelPosition implements AutoCommand {
    private final Blocker mBlocker;

    public BlockerTravelPosition(Blocker blocker) {
        mBlocker = blocker;
    }

    public void firstStep() {}

    public AutoCommand.Completion runStep(double deltaTime) {
        mBlocker.setTravelPosition();
        return Completion.FINISHED;
    }
}

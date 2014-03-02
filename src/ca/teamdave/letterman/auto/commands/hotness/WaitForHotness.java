package ca.teamdave.letterman.auto.commands.hotness;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.descriptors.GoalHotnessState;
import ca.teamdave.letterman.robotcomponents.HotnessTracker;

/**
 * Wait until the specified goal is hot
 */
public class WaitForHotness implements AutoCommand {
    private final HotnessTracker mHotnessTracker;
    private final GoalHotnessState mDesiredState;

    public WaitForHotness(HotnessTracker hotnessTracker, GoalHotnessState desiredState) {
        mHotnessTracker = hotnessTracker;
        mDesiredState = desiredState;
    }

    public void firstStep() {

    }

    public Completion runStep(double deltaTime) {
        if (mHotnessTracker.getInferredGoalHotness() == mDesiredState) {
            return Completion.FINISHED;
        }
        // TODO: see if that hotness has already passed
        return Completion.RUNNING;
    }
}

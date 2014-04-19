package ca.teamdave.letterman.auto.commands.hotness;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.descriptors.GoalHotnessState;
import com.team254.lib.CheesyVisionServer;

/**
 * Wait until the specified goal is hot
 */
public class WaitForHotness implements AutoCommand {
    private final GoalHotnessState mDesiredState;

    public WaitForHotness(GoalHotnessState desiredState) {
        mDesiredState = desiredState;
    }

    public void firstStep() {

    }

    public Completion runStep(double deltaTime) {
        boolean leftStatus = CheesyVisionServer.getInstance().getLeftStatus();
        boolean rightStatus = CheesyVisionServer.getInstance().getRightStatus();

        if ((mDesiredState == GoalHotnessState.LEFT_HOT && leftStatus && (!rightStatus))
                || (mDesiredState == GoalHotnessState.RIGHT_HOT && rightStatus && (!leftStatus))) {
            return Completion.FINISHED;
        }

        return Completion.RUNNING;
    }
}

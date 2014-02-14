package ca.teamdave.letterman.auto.commands.shoot;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Blocker;
import ca.teamdave.letterman.robotcomponents.Intake;

/**
 * Doesn't return finished until the blocker is low enough and the intake is far out enough to
 * safely start shooting
 */
public class WaitForBlockerIntakeClearance implements AutoCommand {

    private final Blocker mBlocker;
    private final Intake mIntake;

    public WaitForBlockerIntakeClearance(Blocker blocker, Intake intake) {
        mBlocker = blocker;
        mIntake = intake;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        if (mBlocker.isClearForShot() && mIntake.isClearForShot()) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

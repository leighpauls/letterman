package ca.teamdave.letterman.auto.commands.intake;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Intake;

/**
 * Fire and forget intake latch in
 */
public class IntakeLatchIn implements AutoCommand {

    private final Intake mIntake;

    public IntakeLatchIn(Intake intake) {
        mIntake = intake;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        mIntake.latchIn();
        return Completion.FINISHED;
    }
}

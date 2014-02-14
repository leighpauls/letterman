package ca.teamdave.letterman.auto.commands.intake;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Intake;

/**
 * Move the intake to the shoot position, always returns finished
 */
public class IntakeShootPosition implements AutoCommand {
    private final Intake mIntake;

    public IntakeShootPosition(Intake intake) {
        mIntake = intake;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        mIntake.tryShooting();
        return Completion.FINISHED;
    }
}

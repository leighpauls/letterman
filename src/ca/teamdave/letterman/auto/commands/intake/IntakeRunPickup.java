package ca.teamdave.letterman.auto.commands.intake;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Intake;

/**
 * Runs the intake in, is always finished
 */
public class IntakeRunPickup implements AutoCommand {
    private final Intake mIntake;

    public IntakeRunPickup(Intake intake) {
        mIntake = intake;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        mIntake.pickup();

        return Completion.FINISHED;
    }
}

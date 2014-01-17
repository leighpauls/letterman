package ca.teamdave.letterman.auto.commands;

/**
 * Command that does nothing, and is always done
 */
public class NoOp implements AutoCommand {
    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        return Completion.FINISHED;
    }
}

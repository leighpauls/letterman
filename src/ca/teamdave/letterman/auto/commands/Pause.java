package ca.teamdave.letterman.auto.commands;

/**
 * Pause to simulate functions for hardware that doesn't exist yet
 */
public class Pause implements AutoCommand {
    private double mTimeRemaining;

    public Pause(double time) {
        mTimeRemaining = time;
    }

    public void firstStep() {}

    public Completion runStep(double deltaTime) {
        mTimeRemaining -= deltaTime;
        if (mTimeRemaining <= 0) {
            return Completion.FINISHED;
        }
        return Completion.RUNNING;
    }
}

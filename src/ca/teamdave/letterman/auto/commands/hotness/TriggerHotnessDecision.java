package ca.teamdave.letterman.auto.commands.hotness;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.HotnessTracker;

/**
 * Triggers the hotness decision
 */
public class TriggerHotnessDecision implements AutoCommand {
    private final HotnessTracker mHotnessTracker;

    public TriggerHotnessDecision(HotnessTracker hotnessTracker) {
        mHotnessTracker = hotnessTracker;
    }

    public void firstStep() {
        mHotnessTracker.triggerModeHotnessDecision();
    }

    public Completion runStep(double deltaTime) {
        return Completion.FINISHED;
    }
}

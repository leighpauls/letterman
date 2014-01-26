package ca.teamdave.letterman.auto.commands;

import ca.teamdave.letterman.EnumerationClass;

/**
 * Interface for all auto commands
 */
public interface AutoCommand {
    /**
     * Enumeration indicating if a command is done or not
     */
    public class Completion extends EnumerationClass {
        private Completion(String state) {
            super(state);
        }
        public static Completion RUNNING = new Completion("running");
        public static Completion FINISHED = new Completion("finished");
    }

    void firstStep();
    Completion runStep(double deltaTime);
}

package ca.teamdave.letterman.auto.commands;

/**
 * Interface for all auto commands
 */
public interface AutoCommand {
    /**
     * Enumeration indicating if a command is done or not
     */
    public class Completion {
        private final String mState;
        private Completion(String state) {
            mState = state;
        }
        public String toString() {
            return "Completion: " + mState;
        }
        public static Completion RUNNING = new Completion("running");
        public static Completion FINISHED = new Completion("finished");
    }

    void firstStep();
    Completion runStep(double deltaTime);
}

package ca.teamdave.letterman;

/**
 * Interface for things that need to be updated every cycle, regardless of mode
 */
public interface UpdatingComponent {
    void updateComponent(double deltaTime);
}

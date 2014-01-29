package ca.teamdave.letterman.background;

/**
 * Interface for things that need to be updated every cycle, regardless of mode
 */
public interface BackgroundUpdatingComponent {

    void updateComponent(RobotMode mode, double modeTime, double deltaTime);
}

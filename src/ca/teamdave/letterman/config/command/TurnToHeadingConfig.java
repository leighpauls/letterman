package ca.teamdave.letterman.config.command;

import ca.teamdave.letterman.config.control.PidControllerConfig;

/**
 * Configuration for {@link ca.teamdave.letterman.auto.commands.TurnToHeading}
 */
public class TurnToHeadingConfig {
    public final double heading;
    public final double completionErrorAngle;
    public final PidControllerConfig turnControl;

    public TurnToHeadingConfig(
            double heading,
            double completionErrorAngle,
            PidControllerConfig turnControl) {
        this.heading = heading;
        this.completionErrorAngle = completionErrorAngle;
        this.turnControl = turnControl;
    }
}

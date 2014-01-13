package ca.teamdave.letterman.config.control;

/**
 * Configuration for a {@link ca.teamdave.letterman.PidController}
 */
public class PidControllerConfig {
    public final double p;
    public final double i;
    public final double d;

    public PidControllerConfig(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }
}

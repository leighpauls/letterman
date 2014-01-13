package ca.teamdave.letterman.config.component;

/**
 * Configuration for {@link WheelSetConfig}
*/
public class WheelSetConfig {
    public final int[] victorChannels;
    public final boolean outputInverted;

    public final int encoderA;
    public final int encoderB;
    public final boolean inputInverted;
    public final double ticksPerFoot;

    public WheelSetConfig(
            int[] victorChannels,
            boolean outputInverted,
            int encoderA,
            int encoderB,
            boolean inputInverted,
            double ticksPerFoot) {
        this.victorChannels = victorChannels;
        this.encoderA = encoderA;
        this.encoderB = encoderB;
        this.inputInverted = inputInverted;
        this.outputInverted = outputInverted;
        this.ticksPerFoot = ticksPerFoot;
    }
}

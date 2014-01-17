package ca.teamdave.letterman.config.command;

/**
 * Config for a RobotStopped command
 */
public class WaitForDriveStoppedConfig {
    public final double forwardStopSpeed; // in feet/second
    public final double turnStopSpeed; // in deg/second

    public WaitForDriveStoppedConfig(double forwardStopSpeed, double turnStopSpeed) {
        this.forwardStopSpeed = forwardStopSpeed;
        this.turnStopSpeed = turnStopSpeed;
    }
}

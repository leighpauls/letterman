package ca.teamdave.letterman.config.command;

import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.config.control.PidControllerConfig;

/**
 * Configuration for a {@link ca.teamdave.letterman.auto.commands.drive.DriveToPoint} object
 */
public class DriveToPointConfig {
    public final RobotPosition endPosition;

    /** Distance from the target where turning is no longer tried */
    public final double turnLockDistance;
    /** Distance from the target where the command is "completed" */
    public final double completeDistance;

    public final PidControllerConfig turnControl;
    public final PidControllerConfig driveControl;


    public DriveToPointConfig(
            RobotPosition endPosition,
            double turnLockDistance,
            double completeDistance,
            PidControllerConfig turnControl,
            PidControllerConfig driveControl) {
        this.endPosition = endPosition;
        this.turnLockDistance = turnLockDistance;
        this.completeDistance = completeDistance;
        this.turnControl = turnControl;
        this.driveControl = driveControl;
    }
}

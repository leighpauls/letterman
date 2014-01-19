package ca.teamdave.letterman.auto.commands.drive;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.config.command.WaitForRegionConfig;
import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.robotcomponents.DriveBase;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that finishes if the robot is within a defined rectangular region
 */
public class WaitForRegion implements AutoCommand {
    private final double mX1, mY1, mX2, mY2;
    private final DriveBase mDriveBase;

    public WaitForRegion(WaitForRegionConfig config, DriveBase driveBase) {
        mX1 = config.x1;
        mY1 = config.y1;
        mX2 = config.x2;
        mY2 = config.y2;
        mDriveBase = driveBase;
    }

    public void firstStep() { }

    private boolean isBetween(double value, double limitA, double limitB) {
        return value <= Math.max(limitA, limitB) && value >= Math.min(limitA, limitB);
    }

    public Completion runStep(double deltaTime) {
        RobotPosition position = mDriveBase.getPose().getPosition();
        return (isBetween(position.getX(), mX1, mX2) && isBetween(position.getY(), mY1, mY2))
                ? Completion.FINISHED
                : Completion.RUNNING;
    }
}

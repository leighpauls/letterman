package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.RobotPose;
import ca.teamdave.letterman.RobotPosition;
import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.DriveToPoint;
import ca.teamdave.letterman.auto.commands.Series;
import ca.teamdave.letterman.config.command.DriveToPointConfig;

/**
 * Auto mode that does the driving pattern for scoring two balls
 */
public class ScoreTwoDriving implements AutoMode {

    public RobotPose getInitialPose() {
        return new RobotPose(new RobotPosition(0, 0), 0);
    }

    public AutoCommand getRootCommand() {
        return new Series(new AutoCommand[] {
                // TODO: drive this construction nicely from somewhere
        });
    }
}

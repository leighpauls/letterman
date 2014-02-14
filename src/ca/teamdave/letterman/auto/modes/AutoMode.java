package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.descriptors.RobotPose;

/**
 * Describes a particular autonomous mode
 */
public interface AutoMode {
    RobotPose getInitialPose();
    AutoCommand getRootCommand() throws Exception;
    String getVisibleName();
}

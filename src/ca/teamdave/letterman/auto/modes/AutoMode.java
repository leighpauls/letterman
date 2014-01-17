package ca.teamdave.letterman.auto.modes;

import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.auto.commands.AutoCommand;

/**
 * Describes a particular autonomous mode
 */
public interface AutoMode {
    RobotPose getInitialPose();
    AutoCommand getRootCommand();
}

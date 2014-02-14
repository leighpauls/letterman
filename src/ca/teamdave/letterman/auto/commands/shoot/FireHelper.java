package ca.teamdave.letterman.auto.commands.shoot;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.auto.commands.intake.IntakeShootPosition;
import ca.teamdave.letterman.auto.commands.blocker.BlockerBlockPosition;
import ca.teamdave.letterman.auto.commands.meta.Latch;
import ca.teamdave.letterman.auto.commands.meta.Series;
import ca.teamdave.letterman.robotcomponents.Blocker;
import ca.teamdave.letterman.robotcomponents.Intake;
import ca.teamdave.letterman.robotcomponents.Shooter;

/**
 * Helper class for making the firing sequence
 */
public class FireHelper {
    public static AutoCommand getFireCommand(Blocker blocker, Intake intake, Shooter shooter) {
        return new Latch(new AutoCommand[]{
                // set the blocker and intake out for clearance
                new BlockerBlockPosition(blocker),
                new IntakeShootPosition(intake),
                // try to shoot
                new Series(new AutoCommand[] {
                        new WaitForBlockerIntakeClearance(blocker, intake),
                        new FireAndRetract(shooter)
                })
        });
    }
}

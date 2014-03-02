package ca.teamdave.letterman.auto.commands.shoot;

import ca.teamdave.letterman.auto.commands.AutoCommand;
import ca.teamdave.letterman.robotcomponents.Shooter;

/**
 * Fire and forget retract trigger, returns once the shooter is ready or retracting
 */
public class StartRetract implements AutoCommand {
    private final Shooter mShooter;

    public StartRetract(Shooter shooter) {
        mShooter = shooter;
    }

    public void firstStep() {

    }

    public Completion runStep(double deltaTime) {
        mShooter.tryRetracting(true);

        return mShooter.isReadyOrRetracting() ? Completion.FINISHED : Completion.RUNNING;
    }
}

package ca.teamdave.letterman.auto.commands.hotness;

import ca.teamdave.letterman.auto.commands.drive.TurnToHeading;
import ca.teamdave.letterman.config.command.TurnToHeadingConfig;
import ca.teamdave.letterman.descriptors.GoalHotnessState;
import ca.teamdave.letterman.robotcomponents.HotnessTracker;
import ca.teamdave.letterman.robotcomponents.Robot;

/**
 * Turn towards the hot goal (based on the start time)
 */
public class TurnToHotness extends TurnToHeading {
    private final HotnessTracker mHotnessTracker;
    private boolean mShouldInvertDirection;

    /**
     * @param turnToHeadingConfig Config for the right hand turn, will be flipped to left if needed
     * @param robot
     */
    public TurnToHotness(TurnToHeadingConfig turnToHeadingConfig, Robot robot) {
        super(turnToHeadingConfig, robot.getDriveBase());
        mHotnessTracker = robot.getHotnessTracker();
    }

    protected double getDestHeading() {
        return (mShouldInvertDirection ? -1 : 1) * super.getDestHeading();
    }

    public void firstStep() {
        mShouldInvertDirection =
                mHotnessTracker.getInferredGoalHotness() == GoalHotnessState.LEFT_HOT;
        super.firstStep();
    }
}

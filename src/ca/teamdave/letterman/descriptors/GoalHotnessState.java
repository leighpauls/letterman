package ca.teamdave.letterman.descriptors;

import ca.teamdave.letterman.EnumerationClass;

/**
 * Describes the perceived "hotness" state of the goals
 */
public class GoalHotnessState extends EnumerationClass {

    public static final GoalHotnessState LEFT_HOT = new GoalHotnessState("left_hot");
    public static final GoalHotnessState RIGHT_HOT = new GoalHotnessState("right_hot");
    public static final GoalHotnessState UNCERTAIN = new GoalHotnessState("uncertain");

    protected GoalHotnessState(String name) {
        super(name);
    }
}

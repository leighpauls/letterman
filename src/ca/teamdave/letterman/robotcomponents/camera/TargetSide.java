package ca.teamdave.letterman.robotcomponents.camera;

import ca.teamdave.letterman.EnumerationClass;

/**
 * Enumeration the left and right target
 */
public class TargetSide extends EnumerationClass {
    TargetSide(String name) {
        super(name);
    }
    public static final TargetSide LEFT = new TargetSide("left");
    public static final TargetSide RIGHT = new TargetSide("right");
}

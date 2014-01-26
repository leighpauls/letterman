package ca.teamdave.letterman.background;

/**
* The auto/teleop/disabled mode of the robot
*/
public class RobotMode {
    private final String mModeName;
    public RobotMode(String modeName) {
        mModeName = modeName;
    }
    public String toString() {
        return mModeName;
    }
    public static final RobotMode DISABLED = new RobotMode("Disabled");
    public static final RobotMode TELEOP = new RobotMode("Teleop");
    public static final RobotMode AUTO = new RobotMode("Auto");
}

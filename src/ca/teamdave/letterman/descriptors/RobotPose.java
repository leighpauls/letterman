package ca.teamdave.letterman.descriptors;

/**
 * Container class for a robot's position and orientation
 */
public class RobotPose {
    private final RobotPosition mPosition;
    private final double mHeading;

    public RobotPose(RobotPosition position, double heading) {
        mPosition = position;
        mHeading = heading;
    }

    public RobotPosition getPosition() {
        return mPosition;
    }

    public double getHeading() {
        return mHeading;
    }


}

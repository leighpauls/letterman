package ca.teamdave.letterman;

/**
 * Container class for a robot's position and orientation
 */
public class RobotPose {
    private final double mX;
    private final double mY;
    private final double mHeading;

    public RobotPose(double x, double y, double heading) {
        mX = x;
        mY = y;
        mHeading = heading;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public double getHeading() {
        return mHeading;
    }
}

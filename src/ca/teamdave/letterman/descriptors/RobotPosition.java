package ca.teamdave.letterman.descriptors;

import com.sun.squawk.util.MathUtils;

/**
 * Position of a robot, without orientation information
 */
public class RobotPosition {
    private final double mX;
    private final double mY;

    public RobotPosition(double x, double y) {
        mX = x;
        mY = y;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public double getHeadingToPosition(RobotPosition endPosition) {
        double deltaX = endPosition.mX - mX;
        double deltaY = endPosition.mY - mY;
        double headingRad = MathUtils.atan2(deltaY, deltaX);
        return headingRad * 180.0 / Math.PI;
    }

    public double distanceTo(RobotPosition endPosition) {
        double deltaX = endPosition.mX - mX;
        double deltaY = endPosition.mY - mY;
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }
}

package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.descriptors.RobotPosition;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.config.component.DriveBaseConfig;
import edu.wpi.first.wpilibj.Gyro;

/**
 * Controls the driving subsystem
 */
public class DriveBase implements BackgroundUpdatingComponent {

    private final WheelSet mLeft;
    private final WheelSet mRight;
    private final Gyro mGyro;

    private RobotPose mPose;
    private double mGyroOffset;

    public DriveBase(DriveBaseConfig config) {
        mLeft = new WheelSet(config.left);
        mRight = new WheelSet(config.right);
        mGyro = new Gyro(config.gyroChannel);
        mGyro.setSensitivity(config.gyroVoltsSecondsPerDegree);

        mPose = config.initialPose;
        mGyroOffset = mPose.getHeading();

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void updateComponent(double deltaTime) {
        double velocity = (mLeft.getVelocity() + mRight.getVelocity()) / 2;
        double headingDegrees = mGyro.getAngle() + mGyroOffset;
        double headingRadians = headingDegrees * Math.PI / 180.0;

        double deltaX = velocity * deltaTime * Math.cos(headingRadians);
        double deltaY = velocity * deltaTime * Math.sin(headingRadians);

        mPose = new RobotPose(
                new RobotPosition(
                        mPose.getPosition().getX() + deltaX,
                        mPose.getPosition().getY() + deltaY),
                headingDegrees);

       /*  System.out.println(
                mPose.getPosition().getX() + ", "
                        + mPose.getPosition().getY() + ", "
                        + mPose.getHeading()); */
    }

    public void setArcade(double forward, double sideways) {
        mLeft.setPower(forward + sideways);
        mRight.setPower(forward - sideways);
    }

    public RobotPose getPose() {
        return mPose;
    }

    public void reset(RobotPose initialPose) {
        mPose = initialPose;
        mGyroOffset = initialPose.getHeading() - mGyro.getAngle();
    }

    public double getForwardVelocity() {
        return (mLeft.getVelocity() + mRight.getVelocity()) / 2;
    }

    public double getTurnVelocity() {
        return mGyro.getRate();
    }
}

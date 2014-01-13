package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.RobotPose;
import ca.teamdave.letterman.UpdateManager;
import ca.teamdave.letterman.UpdatingComponent;
import ca.teamdave.letterman.robotconfig.DriveBaseConfig;
import edu.wpi.first.wpilibj.Gyro;

/**
 * Controls the driving subsystem
 */
public class DriveBase implements UpdatingComponent {

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

        UpdateManager.getInstance().registerComponent(this);
    }

    public void updateComponent(double deltaTime) {
        double velocity = (mLeft.getVelocity() + mRight.getVelocity()) / 2;
        double headingDegrees = mGyro.getAngle() + mGyroOffset;
        double headingRadians = headingDegrees * Math.PI / 180.0;

        double deltaX = velocity * deltaTime * Math.cos(headingRadians);
        double deltaY = velocity * deltaTime * Math.sin(headingRadians);

        mPose = new RobotPose(mPose.getX() + deltaX, mPose.getY() + deltaY, headingDegrees);
        System.out.println(mPose.getX() + ", " + mPose.getY() + ", " + mPose.getHeading());
    }

    public void setArcade(double forward, double sideways) {
        mLeft.setPower(forward + sideways);
        mRight.setPower(forward - sideways);
    }

    public RobotPose getPose() {
        return mPose;
    }

    public void reset(RobotPose initialPose) {
        mGyro.reset();
        mPose = initialPose;
        mGyroOffset = initialPose.getHeading();
    }
}

package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.config.component.DriveBaseConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
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
    private double mPrevGyroAngle;
    private double mGyroRate;

    public DriveBase(DriveBaseConfig config) {
        mLeft = new WheelSet(config.leftWheelSet);
        mRight = new WheelSet(config.rightWheelSet);
        mGyro = new Gyro(config.gyroChannel);
        mGyro.setSensitivity(config.gyroVoltSecondsPerDegree);

        mPose = config.initialPose;
        mGyroOffset = mPose.getHeading();
        mPrevGyroAngle = mPose.getHeading();
        mGyroRate = 0;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void updateComponent(double deltaTime) {
        mLeft.update(deltaTime);
        mRight.update(deltaTime);
        double velocity = (mLeft.getVelocity() + mRight.getVelocity()) / 2;
        double headingDegrees = mGyro.getAngle() + mGyroOffset;
        double headingRadians = headingDegrees * Math.PI / 180.0;

        double deltaX = velocity * deltaTime * Math.cos(headingRadians);
        double deltaY = velocity * deltaTime * Math.sin(headingRadians);

        mGyroRate = (headingDegrees - mPrevGyroAngle) / deltaTime;
        mPrevGyroAngle = headingDegrees;

        mPose = new RobotPose(
                new RobotPosition(
                        mPose.getPosition().getX() + deltaX,
                        mPose.getPosition().getY() + deltaY),
                headingDegrees);
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
        mPrevGyroAngle = initialPose.getHeading();
    }

    public double getForwardVelocity() {
        return (mLeft.getVelocity() + mRight.getVelocity()) / 2;
    }

    public double getTurnVelocity() {
        return mGyroRate;
    }
}

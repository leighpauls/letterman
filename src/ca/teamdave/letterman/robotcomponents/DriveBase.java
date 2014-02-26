package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.EnumerationClass;
import ca.teamdave.letterman.background.BackgroundUpdateManager;
import ca.teamdave.letterman.background.BackgroundUpdatingComponent;
import ca.teamdave.letterman.background.RobotMode;
import ca.teamdave.letterman.config.component.DriveBaseConfig;
import ca.teamdave.letterman.descriptors.RobotPose;
import ca.teamdave.letterman.descriptors.RobotPosition;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Controls the driving subsystem
 */
public class DriveBase implements BackgroundUpdatingComponent {

    private final WheelSet mLeft;
    private final WheelSet mRight;
    private final Gyro mGyro;
    private final Solenoid mHighGearSolenoid;
    private final Solenoid mLowGearSolenoid;

    private RobotPose mPose;
    private double mGyroOffset;
    private double mPrevGyroAngle;
    private double mGyroRate;

    public DriveBase(DriveBaseConfig config) {
        mLeft = new WheelSet(config.leftWheelSet);
        mRight = new WheelSet(config.rightWheelSet);

        mGyro = new Gyro(config.gyroChannel);
        mGyro.setSensitivity(config.gyroVoltSecondsPerDegree);

        mHighGearSolenoid = new Solenoid(config.highGearSolenoid);
        mLowGearSolenoid = new Solenoid(config.lowGearSolenoid);


        mPose = config.initialPose;
        mGyroOffset = mPose.getHeading();
        mPrevGyroAngle = mPose.getHeading();
        mGyroRate = 0;

        BackgroundUpdateManager.getInstance().registerComponent(this);
    }

    public void updateComponent(RobotMode mode, double modeTime, double deltaTime) {
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

        DriverStationLCD.getInstance().println(
                DriverStationLCD.Line.kUser3,
                1,
                "X: " + (int)mPose.getPosition().getX()
                        + ", Y: " + (int)mPose.getPosition().getY()
                        + ", D: " + (int)mPose.getHeading()
                        + "                ");
        DriverStationLCD.getInstance().updateLCD();

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

    public static class GearState extends EnumerationClass {
        protected GearState(String name) {
            super(name);
        }
        public static GearState HIGH_GEAR = new GearState("high_gear");
        public static GearState LOW_GEAR = new GearState("low_gear");
    }

    public void setGearState(GearState gearState) {
        if (gearState == GearState.HIGH_GEAR) {
            mHighGearSolenoid.set(true);
            mLowGearSolenoid.set(false);
        } else {
            mHighGearSolenoid.set(false);
            mLowGearSolenoid.set(true);
        }
    }
}

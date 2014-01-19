/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.teamdave.letterman.robotcomponents;

import ca.teamdave.letterman.config.component.WheelSetConfig;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

/**
 * Holds motors and sensors for one side of wheels
 * @author leighpauls
 */
public class WheelSet {

    private final Victor[] mVictors;
    private final boolean mOutputInverted;

    private final Encoder mEncoder;
    private double mPrevPosition;
    private double mVelocity;

    private final double DEADZONE = 0.1;

    public WheelSet(WheelSetConfig config) {
        mVictors = new Victor[config.victorChannels.length];
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i] = new Victor(config.victorChannels[i]);
        }
        mOutputInverted = config.outputInverted;

        mEncoder = new Encoder(config.encoderA, config.encoderB, config.inputInverted);
        mEncoder.setDistancePerPulse(1 / config.ticksPerFoot);
        mEncoder.start();

        mPrevPosition = 0;
        mVelocity = 0;
    }

    public void setPower(double power) {
        if (Math.abs(power) < DEADZONE) {
            power = 0;
        }
        for (int i = 0; i < mVictors.length; ++i) {
            mVictors[i].set(power * (mOutputInverted ? -1 : 1));
        }
    }

    public double getPosition() {
        return mEncoder.getDistance();
    }

    public double getVelocity() {
        return mVelocity;
    }

    public void update(double deltaTime) {
        double curPosition = getPosition();
        mVelocity = (curPosition - mPrevPosition) / deltaTime;
        mPrevPosition = curPosition;
    }
}

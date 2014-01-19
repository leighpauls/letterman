package ca.teamdave.letterman;

import ca.teamdave.letterman.config.control.PidControllerConfig;

/**
 * Statefull PID controller
 */
public class PidController {
    private final double mP, mI, mD;
    private double mIntegral;
    private double mPrevError;

    public PidController(PidControllerConfig config) {
        mP = config.p;
        mI = config.i;
        mD = config.d;
        mIntegral = 0;
        mPrevError = 0;
    }

    public void reset(double setPoint, double curSensorValue) {
        mIntegral = 0;
        mPrevError = setPoint - curSensorValue;
    }

    public double update(double deltaTime, double setPoint, double curSensorValue) {
        double output = 0;

        // proportional contribution
        double error = setPoint - curSensorValue;
        output += mP * error;

        // integral contribution
        mIntegral += error * deltaTime;
        output += mI * mIntegral;

        // derivative contribution
        output += mD * (error - mPrevError) / deltaTime;
        mPrevError = error;

        return output;
    }

    public String toString() {
        return "PID Controller: " + mP +", "+mI+", "+mD;
    }
}

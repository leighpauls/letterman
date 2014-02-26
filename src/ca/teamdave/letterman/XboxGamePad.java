/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.teamdave.letterman;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Wrapper class for the xbox game pad as a joystick
 * @author leighpauls
 */
public class XboxGamePad {
    private final Joystick mPad;
    
    public XboxGamePad(int port) {
        mPad = new Joystick(port);
    }

    private static final double DEADBAND_RADIUS = 0.15;
    private static double deadband(double jsValue) {
        if (Math.abs(jsValue) < DEADBAND_RADIUS) {
            return 0;
        }
        // re-linearize outside of the deadband
        jsValue -= DEADBAND_RADIUS * DaveUtils.sign(jsValue);
        jsValue *= 1.0 / (1.0 - DEADBAND_RADIUS);
        // TODO: should this value be squared for a shallower curve?
        return jsValue;
    }
    
    public double getXLeft() {
        return deadband(mPad.getRawAxis(1));
    }
    public double getYLeft() {
        return deadband(mPad.getRawAxis(2));
    }
    /**
     * More positive is more left
     * @return 
     */
    public double getTriggerDifference() {
        return deadband(mPad.getRawAxis(3));
    }
    
    public double getXRight() {
        return deadband(mPad.getRawAxis(4));
    }
    public double getYRight() {
        return deadband(mPad.getRawAxis(5));
    }
    
    public boolean getAButton() {
        return mPad.getRawButton(1);
    }
    public boolean getBButton() {
        return mPad.getRawButton(2);
    }
    public boolean getXButton() {
        return mPad.getRawButton(3);
    }
    public boolean getYButton() {
        return mPad.getRawButton(4);
    }
    public boolean getLeftBumper() {
        return mPad.getRawButton(5);
    }
    
    public boolean getRightBumper() {
        return mPad.getRawButton(6);
    }
    
    public boolean getBackButton() {
        return mPad.getRawButton(7);
    }
    public boolean getStartButton() {
        return mPad.getRawButton(8);
    }
    public boolean getLeftStickButton() {
        return mPad.getRawButton(9);
    }
    public boolean getRightStickButton() {
        return mPad.getRawButton(10);
    }

    public boolean getDPadLeft() {
        return mPad.getRawAxis(6) < -0.5;
    }
    public boolean getDPadRight() {
        return mPad.getRawAxis(6) > 0.5;
    }
}

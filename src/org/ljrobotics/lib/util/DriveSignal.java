package org.ljrobotics.lib.util;

import org.ljrobotics.frc2018.Constants;

/**
 * A drivetrain command consisting of the left, right motor settings and whether the brake mode is enabled.
 */
public class DriveSignal {
    protected double mLeftMotor;
    protected double mRightMotor;
    protected boolean mBrakeMode;

    public DriveSignal(double left, double right) {
        this(left, right, false);
    }

    public DriveSignal(double left, double right, boolean brakeMode) {
        mLeftMotor = left;
        mRightMotor = right;
        mBrakeMode = brakeMode;
    }

    public static DriveSignal NEUTRAL = new DriveSignal(0, 0);
    public static DriveSignal BRAKE = new DriveSignal(0, 0, true);

    public double getLeft() {
        return mLeftMotor;
    }

    public double getRight() {
        return mRightMotor;
    }

    public boolean getBrakeMode() {
        return mBrakeMode;
    }

    @Override
    public String toString() {
        return "L: " + mLeftMotor + ", R: " + mRightMotor + (mBrakeMode ? ", BRAKE" : "");
    }
    
    @Override
    public boolean equals(Object o) {
    	if(o == null || !(o instanceof DriveSignal)) {
    		return false;
    	}
    	DriveSignal other = (DriveSignal)o;
    	return Util.epsilonEquals(other.mLeftMotor, this.mLeftMotor, Constants.EQUALS_EPSILON) &&
    			Util.epsilonEquals(other.mRightMotor, this.mRightMotor, Constants.EQUALS_EPSILON) &&
    			other.mBrakeMode == this.mBrakeMode;
    }
}
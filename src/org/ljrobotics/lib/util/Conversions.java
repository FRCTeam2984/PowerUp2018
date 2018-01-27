package org.ljrobotics.lib.util;

import org.ljrobotics.frc2018.Constants;

public class Conversions {
	
	public static double encoderTicksToInchesRight(double ticksPerSecond) {
		double rotationsPerSecond = ticksPerSecond / Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT;
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		return rotationsPerSecond * wheelCircumference;
	}
	
	public static double encoderTicksToInchesLeft(double ticksPerSecond) {
		double rotationsPerSecond = ticksPerSecond / Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT;
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		return rotationsPerSecond * wheelCircumference;
	}
	
	public static double inchesToEncoderTicksRight(double inchesPerSecond) {
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		double rotationsPerSecond = inchesPerSecond / wheelCircumference;
		return rotationsPerSecond * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT;
	}
	
	public static double inchesToEncoderTicksLeft(double inchesPerSecond) {
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		double rotationsPerSecond = inchesPerSecond / wheelCircumference;
		return rotationsPerSecond * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT;
	}
}

package org.ljrobotics.lib.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;

public class ConversionsTest {
	
	@Before
	public void before() {
		Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT = 10;
		Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT = 10;
		Constants.DRIVE_WHEEL_DIAMETER_INCHES = 10;
		Constants.DRIVE_WHEEL_CIRCUMFERENCE = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
	}

	@Test
	public void encoderTicksToInchesCorrectRight() {
		assertEquals(Conversions.encoderTicksToInchesRight(1), 10*Math.PI*0.1);
	}
	
	@Test
	public void encoderTicksToInchesCorrectLeft () {
		assertEquals(Conversions.encoderTicksToInchesLeft(1), 10*Math.PI*0.1);
	}
	
	@Test
	public void inchesToEncoderTicksCorrectLeft() {
		assertEquals(Conversions.inchesToEncoderTicksLeft(1), 1D/Constants.DRIVE_WHEEL_CIRCUMFERENCE * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT);
	}
	
	@Test
	public void inchesToEncoderTicksCorrectRight() {
		assertEquals(Conversions.inchesToEncoderTicksLeft(1), 1D/Constants.DRIVE_WHEEL_CIRCUMFERENCE * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT);
	}
}

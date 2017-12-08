package org.ljrobotics.frc2018.subsystems;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.mockito.ArgumentCaptor;

import com.ctre.CANTalon;

public class DriveTest {

	private Drive drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	
	@Before
	public void before() {
		frontLeft = mock(CANTalon.class);
		frontRight = mock(CANTalon.class);
		backLeft = mock(CANTalon.class);
		backRight = mock(CANTalon.class);
		
		drive = new Drive(frontLeft, frontRight, backLeft, backRight);
	}
	
	@Test
	public void stopSetsTalonsToZero() {
		drive.stop();
		verifyTalons(0,0,0,0);
	}
	
	@Test
	public void setBreakModeSetsBreakModeOnFirstCall() {
		drive.setBrakeMode(false);
		drive.setBrakeMode(false);
		verify(this.frontLeft, times(2)).enableBrakeMode(false);
		verify(this.frontRight, times(2)).enableBrakeMode(false);
		verify(this.backLeft, times(2)).enableBrakeMode(false);
		verify(this.backRight, times(2)).enableBrakeMode(false);
	}
	
	@Test
	public void setBreakModeSetsBreakModeAfterToggle() {
		drive.setBrakeMode(false);
		drive.setBrakeMode(true);
		verify(this.frontLeft, times(2)).enableBrakeMode(true);
		verify(this.frontRight, times(2)).enableBrakeMode(true);
		verify(this.backLeft, times(2)).enableBrakeMode(true);
		verify(this.backRight, times(2)).enableBrakeMode(true);
	}
	
	private void verifyTalons(double frontLeft, double frontRight, double backLeft, double backRight) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.frontLeft).set(captor.capture());
		assertEquals(frontLeft, (double)captor.getValue(), 0.00001);
		verify(this.frontRight).set(captor.capture());
		assertEquals(frontRight, (double)captor.getValue(), 0.00001);
		verify(this.backLeft).set(captor.capture());
		assertEquals(backLeft, (double)captor.getValue(), 0.00001);
		verify(this.backRight).set(captor.capture());
		assertEquals(backRight, (double)captor.getValue(), 0.00001);
	}
	
}

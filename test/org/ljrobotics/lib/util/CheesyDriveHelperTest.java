package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheesyDriveHelperTest {

	private CheesyDriveHelper driveHelper;
	
	@Before
	public void before() {
		driveHelper = new CheesyDriveHelper();
	}
	
	@Test
	public void allSignalsZeroProduceNoMovement() {
		DriveSignal ds = this.driveHelper.cheesyDrive(0, 0, false, false);
		assertEquals(new DriveSignal(0, 0), ds);
	}
	
	@Test
	public void throttle1WithAllOthersZeroResultsInFullSpeedForward() {
		DriveSignal ds = this.driveHelper.cheesyDrive(1, 0, false, false);
		assertEquals(new DriveSignal(1, 1), ds);
	}
	
	@Test
	public void throttleHalfWithAllOthersZeroResultsInHalfSpeedForward() {
		DriveSignal ds = this.driveHelper.cheesyDrive(0.5, 0, false, false);
		assertEquals(new DriveSignal(0.5, 0.5), ds);
	}
	
	@Test
	public void throttleNegative1WithAllOthersZeroResultsInFullSpeedBackward() {
		DriveSignal ds = this.driveHelper.cheesyDrive(-1, 0, false, false);
		assertEquals(new DriveSignal(-1, -1), ds);
	}
	
	@Test
	public void zeroThrottleFullWheelButNoQuickturnStandsStill() {
		DriveSignal ds = this.driveHelper.cheesyDrive(0, 1, false, false);
		assertEquals(new DriveSignal(0, 0), ds);
	}
	
	@Test
	public void zeroThrottleFullWheelAndQuickturnSpinsAtFullRate() {
		DriveSignal ds = this.driveHelper.cheesyDrive(0, 1, true, false);
		assertEquals(new DriveSignal(1, -1), ds);
	}
	
	@Test
	public void lowPowerTurnTurnsWithRegardToSineAndNegInertia() {
		DriveSignal ds = this.driveHelper.cheesyDrive(1, 0.2, false, false);
		assertEquals(new DriveSignal(1, -0.26086), ds);
	}
	
	@Test
	public void lowPowerConstantTurnTurnsWithRegardToSineOnly() {
		this.driveHelper.cheesyDrive(1, 0.2, false, false);
		DriveSignal ds = this.driveHelper.cheesyDrive(1, 0.2, false, false);
		assertEquals(new DriveSignal(1, 0.64913), ds);
	}
	
	@Test
	public void highPowerConstantTurnResultsInNegInertiaDecay() {
		DriveSignal ds = this.driveHelper.cheesyDrive(1, -0.7, false, false);
		assertEquals(new DriveSignal(-1, 1), ds);

		ds = this.driveHelper.cheesyDrive(1, -0.7, false, false);
		assertEquals(new DriveSignal(-1, 1), ds);
		
		ds = this.driveHelper.cheesyDrive(1, -0.7, false, false);
		assertEquals(new DriveSignal(-0.636736, 1), ds);
		
		ds = this.driveHelper.cheesyDrive(1, -0.7, false, false);
		assertEquals(new DriveSignal(-0.051736, 1), ds);
		
		ds = this.driveHelper.cheesyDrive(1, -0.7, false, false);
		assertEquals(new DriveSignal(-0.051736, 1), ds);
	}
	
}

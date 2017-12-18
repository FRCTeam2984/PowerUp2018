package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DriveSignalTest {

	@Test
	public void neutralDriveSignalsEqualEachOther() {
		assertEquals(DriveSignal.NEUTRAL, DriveSignal.NEUTRAL);
	}
	
	@Test
	public void zeroDriveSignalsEqualEachOther() {
		DriveSignal first = new DriveSignal(0,0);
		DriveSignal second = new DriveSignal(0,0);
		assertEquals(first, second);
	}
	
	@Test
	public void zeroDriveSignalsWithDifferentBreakDontEqualEachOther() {
		DriveSignal first = new DriveSignal(0,0, true);
		DriveSignal second = new DriveSignal(0,0, false);
		assertNotEquals(first, second);
	}
	
	@Test
	public void differentMagnatudeSignalsDontEqualEachOther() {
		DriveSignal first = new DriveSignal(0.1,0, true);
		DriveSignal second = new DriveSignal(0,0, true);
		assertNotEquals(first, second);
	}
	
	@Test
	public void differentRightMagnatudeSignalsDontEqualEachOther() {
		DriveSignal first = new DriveSignal(0,0.1, true);
		DriveSignal second = new DriveSignal(0,0, true);
		assertNotEquals(first, second);
	}
	
	@Test
	public void signalDoesNotEqualNull() {
		DriveSignal first = new DriveSignal(0.1,0, true);
		assertNotEquals(first, null);
	}
	
	@Test
	public void signalDoesNotEqualAString() {
		DriveSignal first = new DriveSignal(0.1,0, true);
		assertNotEquals(first, first.toString());
	}
}

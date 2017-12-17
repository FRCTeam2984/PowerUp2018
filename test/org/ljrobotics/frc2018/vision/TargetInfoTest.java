package org.ljrobotics.frc2018.vision;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TargetInfoTest {

	@Test
	public void zeroDistanceGivesOrigin() {
		TargetInfo target = new TargetInfo(0, 0);
		
		assertEquals(0, target.getX(), 0.00001);
		assertEquals(0, target.getY(), 0.00001);
		assertEquals(0, target.getZ(), 0.00001);
	}
	
	@Test
	public void oneDistanceHeadOnGivesYOfOne() {
		TargetInfo target = new TargetInfo(1, 0);
		
		assertEquals(0, target.getX(), 0.00001);
		assertEquals(1, target.getY(), 0.00001);
		assertEquals(0, target.getZ(), 0.00001);
	}
	
	@Test
	public void oneDistanceAt90GivesXOfNegative1() {
		TargetInfo target = new TargetInfo(1, 90);
		
		assertEquals(-1, target.getX(), 0.00001);
		assertEquals(0, target.getY(), 0.00001);
		assertEquals(0, target.getZ(), 0.00001);
	}
	
	@Test
	public void oneDistanceAtNegative90GivesXOf1() {
		TargetInfo target = new TargetInfo(1, -90);
		
		assertEquals(1, target.getX(), 0.00001);
		assertEquals(0, target.getY(), 0.00001);
		assertEquals(0, target.getZ(), 0.00001);
	}
	
}

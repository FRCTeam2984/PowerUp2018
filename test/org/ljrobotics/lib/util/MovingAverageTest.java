package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MovingAverageTest {

	@Test
	public void firstNumberAddedSetsAverage() {
		MovingAverage ma = new MovingAverage(1);
		ma.addNumber(10);
		assertEquals(10, ma.getAverage(), 0.00001);
	}
	
	@Test
	public void addingTooManyNumbersMovesAverage() {
		MovingAverage ma = new MovingAverage(2);
		ma.addNumber(1);
		ma.addNumber(2);
		ma.addNumber(3);
		assertEquals(2.5, ma.getAverage(), 0.00001);
	}
	
	@Test
	public void underMaxSizeReturnsTrueWhenUnderMaxSize() {
		MovingAverage ma = new MovingAverage(4);
		ma.addNumber(1);
		ma.addNumber(2);
		ma.addNumber(3);
		assertTrue(ma.isUnderMaxSize());
	}
	
	@Test
	public void underMaxSizeReturnsFalseWhenOverMaxSize() {
		MovingAverage ma = new MovingAverage(3);
		ma.addNumber(1);
		ma.addNumber(2);
		ma.addNumber(3);
		assertFalse(ma.isUnderMaxSize());
	}
	
	@Test
	public void clearResetsSize() {
		MovingAverage ma = new MovingAverage(3);
		ma.addNumber(1);
		ma.addNumber(2);
		ma.addNumber(3);
		ma.clear();
		assertEquals(0, ma.getSize());
	}
	
	@Test
	public void clearResetsAverage() {
		MovingAverage ma = new MovingAverage(3);
		ma.addNumber(1);
		ma.addNumber(2);
		ma.addNumber(3);
		ma.clear();
		assertEquals(0, ma.getAverage(), 0.00001);
	}
	
}

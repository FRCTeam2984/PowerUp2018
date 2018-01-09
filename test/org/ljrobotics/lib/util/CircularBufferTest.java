package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CircularBufferTest {

	@Test
	public void emptyBufferHasZeroAverage() {
		CircularBuffer buf = new CircularBuffer(10);
		assertEquals(0, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void bufferWithOneElementHasThatElementsAverage() {
		CircularBuffer buf = new CircularBuffer(10);
		buf.addValue(10);
		assertEquals(10, buf.getAverage(), 0.00001);
	}

	@Test
	public void twoElementBufferReturnsAverageAsAverage() {
		CircularBuffer buf = new CircularBuffer(2);
		buf.addValue(10);
		buf.addValue(30);
		assertEquals(20, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void oneElementBufferReturnsLastValueAsAverage() {
		CircularBuffer buf = new CircularBuffer(1);
		buf.addValue(10);
		buf.addValue(100);
		assertEquals(100, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void recomputeAverageComputesAverage() {
		CircularBuffer buf = new CircularBuffer(2);
		buf.addValue(10);
		buf.addValue(30);
		buf.recomputeAverage();
		assertEquals(20, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void bufferOverflowRemovesFirstElementFromAverage() {
		CircularBuffer buf = new CircularBuffer(2);
		buf.addValue(10);
		buf.addValue(30);
		buf.addValue(50);
		assertEquals(40, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void emptyBufferIsNotFull() {
		CircularBuffer buf = new CircularBuffer(2);
		assertFalse(buf.isFull());
	}
	
	@Test
	public void emptyZeroSizeBufferIsAlwaysFull() {
		CircularBuffer buf = new CircularBuffer(0);
		assertTrue(buf.isFull());
	}
	
	@Test
	public void addingToZeroSizeBufferDoesNothing() {
		CircularBuffer buf = new CircularBuffer(0);
		buf.addValue(10);
		assertEquals(0, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void getNumValuesReturnsValuesAddedSoFarForANonFilledBuffer() {
		CircularBuffer buf = new CircularBuffer(10);
		buf.addValue(1);
		buf.addValue(2);
		buf.addValue(3);
		assertEquals(3, buf.getNumValues());
	}
	
	@Test
	public void getNumValuesReturnsWindowSizeForAFilledBuffer() {
		CircularBuffer buf = new CircularBuffer(2);
		buf.addValue(1);
		buf.addValue(2);
		buf.addValue(3);
		assertEquals(2, buf.getNumValues());
	}
	
	@Test
	public void afterClearNumValuesReturns0() {
		CircularBuffer buf = new CircularBuffer(10);
		buf.addValue(1);
		buf.addValue(2);
		buf.addValue(3);
		buf.clear();
		assertEquals(0, buf.getNumValues());
	}
	
	@Test
	public void afterClearAverageReturns0() {
		CircularBuffer buf = new CircularBuffer(10);
		buf.addValue(1);
		buf.addValue(2);
		buf.addValue(3);
		buf.clear();
		assertEquals(0, buf.getAverage(), 0.00001);
	}
	
	@Test
	public void afterClearRecomputeAverageKeepsAverage0() {
		CircularBuffer buf = new CircularBuffer(10);
		buf.addValue(1);
		buf.addValue(2);
		buf.addValue(3);
		buf.clear();
		buf.recomputeAverage();
		assertEquals(0, buf.getAverage(), 0.00001);
	}
}

package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InterpolatingDoubleTest {

	@Test
	public void returnsOwnValueWhenInterpolatingBetweenItself() {
		InterpolatingDouble id = new InterpolatingDouble(10D);
		assertEquals(id.value, id.interpolate(id, 0.5).value, 0.00001);
	}
	
	@Test
	public void returnsAverageWhenInterpolatingWithHalf() {
		InterpolatingDouble id = new InterpolatingDouble(10D);
		InterpolatingDouble id2 = new InterpolatingDouble(20D);
		assertEquals(15, id.interpolate(id2, 0.5).value, 0.00001);
	}
	
	@Test
	public void inverInterpolateReturnsHowFarBetweenTwoOthers() {
		InterpolatingDouble id = new InterpolatingDouble(5D);
		InterpolatingDouble id2 = new InterpolatingDouble(20D);
		InterpolatingDouble id3 = new InterpolatingDouble(0D);
		assertEquals(0.25, id3.inverseInterpolate(id2, id), 0.00001);
	}
	
	@Test
	public void compareToSaysLargerIsLarger() {
		InterpolatingDouble id = new InterpolatingDouble(5D);
		InterpolatingDouble id2 = new InterpolatingDouble(20D);
		assertEquals(1, id2.compareTo(id));
	}
	
	@Test
	public void compareToSaysSmalerIsSmaler() {
		InterpolatingDouble id = new InterpolatingDouble(5D);
		InterpolatingDouble id2 = new InterpolatingDouble(20D);
		assertEquals(-1, id.compareTo(id2));
	}
	
}

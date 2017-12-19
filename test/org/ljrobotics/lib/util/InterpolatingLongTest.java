package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InterpolatingLongTest {

	@Test
	public void returnsOwnValueWhenInterpolatingBetweenItself() {
		InterpolatingLong il = new InterpolatingLong(10L);
		assertEquals(il.value, il.interpolate(il, 0.5).value, 0.00001);
	}
	
	@Test
	public void returnsAverageWhenInterpolatingWithHalf() {
		InterpolatingLong il = new InterpolatingLong(10L);
		InterpolatingLong il2 = new InterpolatingLong(20L);
		assertEquals(15, il.interpolate(il2, 0.5).value, 0.00001);
	}
	
	@Test
	public void inverInterpolateReturnsHowFarBetweenTwoOthers() {
		InterpolatingLong il = new InterpolatingLong(5L);
		InterpolatingLong il2 = new InterpolatingLong(20L);
		InterpolatingLong il3 = new InterpolatingLong(0L);
		assertEquals(0.25, il3.inverseInterpolate(il2, il), 0.00001);
	}
	
	@Test
	public void compareToSaysLargerIsLarger() {
		InterpolatingLong il = new InterpolatingLong(5L);
		InterpolatingLong il2 = new InterpolatingLong(20L);
		assertEquals(1, il2.compareTo(il));
	}
	
	@Test
	public void compareToSaysSmalerIsSmaler() {
		InterpolatingLong il = new InterpolatingLong(5L);
		InterpolatingLong il2 = new InterpolatingLong(20L);
		assertEquals(-1, il.compareTo(il2));
	}
	
}

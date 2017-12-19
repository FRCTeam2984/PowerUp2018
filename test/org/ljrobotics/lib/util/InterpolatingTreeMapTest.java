package org.ljrobotics.lib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class InterpolatingTreeMapTest {

	private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> iTM;
	
	@Before
	public void before() {
		this.iTM = new InterpolatingTreeMap<>();
	}
	
	@Test
	public void putPutsValueAndKeepsRestGivenSpace() {
		this.iTM = new InterpolatingTreeMap<>(1);
		InterpolatingDouble id = new InterpolatingDouble(0D);
		this.iTM.put(id, id);
		assertEquals(1, this.iTM.size());
	}
	
	@Test
	public void putPutsValueAndRemovesFirstGivenSpaceConstraint() {
		this.iTM = new InterpolatingTreeMap<>(1);
		InterpolatingDouble id = new InterpolatingDouble(0D);
		InterpolatingDouble id2 = new InterpolatingDouble(1D);
		this.iTM.put(id, id);
		this.iTM.put(id2, id2);
		assertEquals(1, this.iTM.size());
		assertEquals(1, this.iTM.get(id2).value, 0.00001);
	}
	
	@Test
	public void getInterpolatedReturnsInterpolatedValueInTree() {
		InterpolatingDouble k1 = new InterpolatingDouble(0D);
		InterpolatingDouble k2 = new InterpolatingDouble(5D);
		InterpolatingDouble k3 = new InterpolatingDouble(10D);

		InterpolatingDouble v1 = new InterpolatingDouble(1D);
		InterpolatingDouble v2 = new InterpolatingDouble(2D);
		InterpolatingDouble v3 = new InterpolatingDouble(3D);
		this.iTM.put(k1, v1);
		this.iTM.put(k2, v2);
		this.iTM.put(k3, v3);
		
		InterpolatingDouble searchK = new InterpolatingDouble(4D);
		InterpolatingDouble searchV = this.iTM.getInterpolated(searchK);

		assertEquals(1.8, searchV.value, 0.00001);
	}
	
	@Test
	public void getInterpolatedReturnsLowerBoundWhenAtLowerBound() {
		InterpolatingDouble k1 = new InterpolatingDouble(0D);
		InterpolatingDouble k2 = new InterpolatingDouble(5D);

		InterpolatingDouble v1 = new InterpolatingDouble(1D);
		InterpolatingDouble v2 = new InterpolatingDouble(2D);
		this.iTM.put(k1, v1);
		this.iTM.put(k2, v2);
		
		InterpolatingDouble searchK = new InterpolatingDouble(-1D);
		InterpolatingDouble searchV = this.iTM.getInterpolated(searchK);

		assertEquals(1, searchV.value, 0.00001);
	}

	@Test
	public void getInterpolatedReturnsUpperBoundWhenAtUpperBound() {
		InterpolatingDouble k1 = new InterpolatingDouble(0D);
		InterpolatingDouble k2 = new InterpolatingDouble(5D);

		InterpolatingDouble v1 = new InterpolatingDouble(1D);
		InterpolatingDouble v2 = new InterpolatingDouble(2D);
		this.iTM.put(k1, v1);
		this.iTM.put(k2, v2);
		
		InterpolatingDouble searchK = new InterpolatingDouble(6D);
		InterpolatingDouble searchV = this.iTM.getInterpolated(searchK);

		assertEquals(2, searchV.value, 0.00001);
	}
	
}

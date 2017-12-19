package org.ljrobotics.lib.util.control;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LookaheadTest {

	@Test
	public void lookaheadDistanceIsMaxDistanceWhenOverMaxSpeed() {
		Lookahead la = new Lookahead(0,10,0,10);
		assertEquals(10, la.getLookaheadForSpeed(11), 0.00001);
	}
	
	@Test
	public void lookaheadDistanceIsMinDistanceWhenUnderMinSpeed() {
		Lookahead la = new Lookahead(0,10,0,10);
		assertEquals(0, la.getLookaheadForSpeed(-1), 0.00001);
	}
	
	@Test
	public void lookaheadDistanceIsHalfWhenAtHalfSpeed() {
		Lookahead la = new Lookahead(0,10,0,10);
		assertEquals(5, la.getLookaheadForSpeed(5), 0.00001);
	}
	
}

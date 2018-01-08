package org.ljrobotics.lib.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LatchedBooleanTest {

	@Test
	public void latchedBooleanReturnsTrueAfterFirstNewValue() {
		LatchedBoolean latchedBoolean = new LatchedBoolean();
		assertTrue(latchedBoolean.update(true));
	}
	
	@Test
	public void latchedBooleanReturnsFalseAfterRepeatedTrueValue() {
		LatchedBoolean latchedBoolean = new LatchedBoolean();
		latchedBoolean.update(true);
		assertFalse(latchedBoolean.update(true));
	}
	
	@Test
	public void latchedBooleanReturnsFalseAfterRepeatedFalseValue() {
		LatchedBoolean latchedBoolean = new LatchedBoolean();
		latchedBoolean.update(false);
		assertFalse(latchedBoolean.update(false));
	}
	
}

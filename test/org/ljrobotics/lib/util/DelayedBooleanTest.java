package org.ljrobotics.lib.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DelayedBooleanTest {

	@Test
	public void delayedBooleanReturnsFalseAfterStateWasFalse() {
		DelayedBoolean bool = new DelayedBoolean(0,10);
		assertFalse(bool.update(1000, true));
	}
	
	@Test
	public void delayedBooleanReturnsFalseAfterStateWasTrue() {
		DelayedBoolean bool = new DelayedBoolean(0,10);
		bool.update(500, true);
		assertFalse(bool.update(1000, false));
	}
	
	@Test
	public void delayedBooleanReturnsFalseAfterTooSoonUpdate() {
		DelayedBoolean bool = new DelayedBoolean(0,10);
		bool.update(5, true);
		assertFalse(bool.update(10, true));
	}
	
	@Test
	public void delayedBooleanReturnsTrueAfterTimelyUpdate() {
		DelayedBoolean bool = new DelayedBoolean(0,10);
		bool.update(5, true);
		assertTrue(bool.update(20, true));
	}
	
}

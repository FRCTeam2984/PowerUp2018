package org.ljrobotics.lib.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.first.wpilibj.Timer;

public class TimeDelayedBooleanTest {

	private TimeDelayedBoolean bool;
	private DummyFPGATimer timer;

	@Before
	public void before() {
		this.timer = new DummyFPGATimer();
		Timer.SetImplementation(this.timer);
		
		this.bool = new TimeDelayedBoolean();
	}
	
	@Test
	public void falseReturnsFalse() {
		assertFalse(bool.update(false, 100));
	}
	
	@Test
	public void returnsTrueWhenTimeHasPassed() {
		bool.update(true, 1000);
		timer.setFPGATimestamp(1001);
		assertTrue(bool.update(true, 1000));
	}
	
	@Test
	public void returnsFalseWhenTimeHasNotPassed() {
		bool.update(true, 1000);
		timer.setFPGATimestamp(500);
		assertFalse(bool.update(true, 1000));
	}
	
}

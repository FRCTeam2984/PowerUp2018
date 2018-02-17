package org.ljrobotics.frc2018.subsystems;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class AutoSelectorSwitchTest {

	private AutoSelectorSwitch autoSwitch;
	
	@Before
	public void before() {
		autoSwitch = new AutoSelectorSwitch(null, null, null);
	}
	
	@Test
	public void withAllInputsFalseGetAutoModeReturnsZero() {
		assertEquals(0, autoSwitch.getAutoMode(true, true, true));
	}
	
	@Test
	public void withInputsOneGetAutoModeReturnsOne() {
		assertEquals(1, autoSwitch.getAutoMode(false, true, true));
	}
	
	@Test
	public void withInputsTwoGetAutoModeReturnsTwo() {
		assertEquals(2, autoSwitch.getAutoMode(true, false, true));
	}
	
	@Test
	public void withInputsThreeGetAutoModeReturnsThree() {
		assertEquals(3, autoSwitch.getAutoMode(false, false, true));
	}
	
	@Test
	public void withInputsFourGetAutoModeReturnsFour() {
		assertEquals(4, autoSwitch.getAutoMode(true, true, false));
	}
	
	@Test
	public void withInputsFiveGetAutoModeReturnsFive() {
		assertEquals(5, autoSwitch.getAutoMode(false, true, false));
	}
	
	@Test
	public void withInputsSixGetAutoModeReturnsSix() {
		assertEquals(6, autoSwitch.getAutoMode(true, false, false));
	}
	
	@Test
	public void withInputsSevenGetAutoModeReturnsSeven() {
		assertEquals(7, autoSwitch.getAutoMode(false, false, false));
	}

}

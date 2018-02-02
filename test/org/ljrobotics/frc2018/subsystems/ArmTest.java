package org.ljrobotics.frc2018.subsystems;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.lib.util.DummyReporter;
import org.mockito.ArgumentCaptor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class ArmTest {

	private Arm arm;
	private TalonSRX slave;
	private TalonSRX master;

	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}

	@Before
	public void before() {
		slave = mock(TalonSRX.class);
		master = mock(TalonSRX.class);

		this.arm = new Arm(slave, master, null, null);
	}

	private void verifyTalons(ControlMode mode, double master) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);

		verify(this.master).set(eq(mode), captor.capture());
		assertEquals(master, (double) captor.getValue(), 0.00001);
	}

	@Test
	public void frontSpeedZero() {
		arm.setRestrictedSpeedTest(0, false, false);
		verifyTalons(ControlMode.PercentOutput, 0);
	}

	@Test
	public void frontBrokenWithNegativeSpeed() {
		arm.setRestrictedSpeedTest(-1, true, false);
		verifyTalons(ControlMode.PercentOutput, 0);
	}

	@Test
	public void frontBrokenWithPositiveSpeed() {
		arm.setRestrictedSpeedTest(1, true, false);
		verifyTalons(ControlMode.PercentOutput, 1);
	}

	@Test
	public void backSpeedZero() {
		arm.setRestrictedSpeedTest(0, false, false);
		verifyTalons(ControlMode.PercentOutput, 0);
	}

	@Test
	public void backBrokenWithNegativeSpeed() {
		arm.setRestrictedSpeedTest(-1, false, true);
		verifyTalons(ControlMode.PercentOutput, -1);
	}

	@Test
	public void backBrokenWithPositiveSpeed() {
		arm.setRestrictedSpeedTest(1, false, true);
		verifyTalons(ControlMode.PercentOutput, 0);
	}

}

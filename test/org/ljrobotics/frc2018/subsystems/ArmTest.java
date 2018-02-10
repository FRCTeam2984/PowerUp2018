package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.lib.util.DummyReporter;
import org.mockito.ArgumentCaptor;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class ArmTest {

	private Arm arm;
	private TalonSRX slave;
	private TalonSRX master;
	private SensorCollection collection;
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}

	@Before
	public void before() {
		slave = mock(TalonSRX.class);
		master = mock(TalonSRX.class);

		collection = mock(SensorCollection.class);
		when(master.getSensorCollection()).thenReturn(collection);

		this.arm = new Arm(slave, master);
	}

	private void verifyTalons(ControlMode mode, double master) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.master).set(eq(mode), captor.capture());

		assertEquals(master, (double) captor.getValue(), 0.00001);
	}

	private void verifyEncoder(double degrees) {
		double current = arm.encoderTicksToDegrees(arm.getFakeEncoderValue());
		assertEquals(current, degrees, 1);
	}

	@Test
	public void stopSetsTalonsToZero() {
		arm.stop();
		verifyTalons(ControlMode.PercentOutput, 0);
	}

	@Test
	public void limitSwitchResetsEncoderFront() {
		when(collection.isRevLimitSwitchClosed()).thenReturn(true);
		arm.updateEncoder();
		verifyEncoder(Constants.MAX_ARM_ENCODER_DEGREES);
	}
}

package org.ljrobotics.frc2018.subsystems;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.lib.util.DriveSignal;
import org.ljrobotics.lib.util.DummyReporter;
import org.ljrobotics.lib.util.InterpolatingDouble;
import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;
import org.ljrobotics.lib.util.math.Twist2d;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.eq;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class IntakeTest {

	private Intake intake;
	private TalonSRX left;
	private TalonSRX right;
	
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}

	@Before
	public void before() {
		left = mock(TalonSRX.class);
		right = mock(TalonSRX.class);

		intake = new Intake(left, right);
	}

	@Test
	public void withNoAnaomoliesTalonsArePowered() {
		setCurrent(0,0);
		intake.setSpeedCurrentChecked(0, 0.5);
		verifyTalons(ControlMode.PercentOutput, 0.5, 0.5);
	}
	
	private void setCurrent(double left, double right) {
		when(this.left.getOutputCurrent()).thenReturn(left);
		when(this.right.getOutputCurrent()).thenReturn(right);
	}

	private void verifyTalons(ControlMode mode, double frontLeft, double frontRight) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.left).set(eq(mode), captor.capture());
		assertEquals(frontLeft, (double) captor.getValue(), 0.00001);

		verify(this.right).set(eq(mode), captor.capture());
		assertEquals(frontRight, (double) captor.getValue(), 0.00001);
	}

}

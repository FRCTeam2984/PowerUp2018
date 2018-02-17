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
import java.util.List;
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

		intake = new Intake(left, right, null, null);
	}

	private void verifyTalons(ControlMode mode, double frontLeft, double frontRight, int timesCalled) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.left, times(timesCalled)).set(eq(mode), captor.capture());
		List<Double> captures = captor.getAllValues();
		assertEquals(frontLeft, (double) captures.get(captures.size()-1), 0.00001);

		verify(this.right, times(timesCalled)).set(eq(mode), captor.capture());
		captures = captor.getAllValues();
		assertEquals(frontRight, (double) captures.get(captures.size()-1), 0.00001);
	}

}

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

public class DriveTest {

	private Drive drive;
	private TalonSRX frontLeft;
	private TalonSRX frontRight;
	private TalonSRX backLeft;
	private TalonSRX backRight;

	private RobotState robotState;
	private Gyro gyro;
	
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}

	@Before
	public void before() {
		frontLeft = mock(TalonSRX.class);
		frontRight = mock(TalonSRX.class);
		backLeft = mock(TalonSRX.class);
		backRight = mock(TalonSRX.class);

		robotState = mock(RobotState.class);
		gyro = mock(Gyro.class);

		drive = new Drive(frontLeft, frontRight, backLeft, backRight, robotState, gyro);
	}

	@Test
	public void stopSetsTalonsToZero() {
		drive.stop();
		verifyTalons(ControlMode.PercentOutput, 0, 0);
	}

	@Test
	public void setOpenLoopWithPotitiveY() {
		drive.setOpenLoop(new DriveSignal(1, 1));
		verifyTalons(ControlMode.PercentOutput, 1, 1);
	}

	@Test
	public void setOpenLoopWithNegativeY() {
		drive.setOpenLoop(new DriveSignal(-1, -1));
		verifyTalons(ControlMode.PercentOutput, -1, -1);
	}

	@Test
	public void setOpenLoopWithPotitiveRotation() {
		drive.setOpenLoop(new DriveSignal(1, -1));
		verifyTalons(ControlMode.PercentOutput, 1, -1);
	}

	@Test
	public void setOpenLoopWithNegativeRotation() {
		drive.setOpenLoop(new DriveSignal(-1, 1));
		verifyTalons(ControlMode.PercentOutput, -1, 1);
	}

	@Test
	public void setOpenLoopWithPositiveYAndRotation() {
		drive.setOpenLoop(new DriveSignal(1, 0));
		verifyTalons(ControlMode.PercentOutput, 1, 0);
	}

	@Test
	public void setOpenLoopWithNegativeYAndRotation() {
		drive.setOpenLoop(new DriveSignal(-1, 0));
		verifyTalons(ControlMode.PercentOutput, -1, 0);
	}

	@Test
	public void setOpenLoopWithValuesOverOne() {
		drive.setOpenLoop(new DriveSignal(10, 5));
		verifyTalons(ControlMode.PercentOutput, 1, 1);
	}

	@Test
	public void setOpenLoopWithValuesUnderOne() {
		drive.setOpenLoop(new DriveSignal(-10, -5));
		verifyTalons(ControlMode.PercentOutput, -1, -1);
	}

	@Test
	public void setBreakModeSetsBreakModeOnFirstCall() {
		drive.setNeutralMode(NeutralMode.Coast);
		drive.setNeutralMode(NeutralMode.Coast);
		verify(this.frontLeft, times(2)).setNeutralMode(NeutralMode.Coast);
		verify(this.frontRight, times(2)).setNeutralMode(NeutralMode.Coast);
		verify(this.backLeft, times(2)).setNeutralMode(NeutralMode.Coast);
		verify(this.backRight, times(2)).setNeutralMode(NeutralMode.Coast);
	}

	@Test
	public void setBreakModeSetsBreakModeAfterToggle() {
		drive.setNeutralMode(NeutralMode.Coast);
		drive.setNeutralMode(NeutralMode.Brake);
		verify(this.frontLeft, times(2)).setNeutralMode(NeutralMode.Brake);
		verify(this.frontRight, times(2)).setNeutralMode(NeutralMode.Brake);
		verify(this.backLeft, times(2)).setNeutralMode(NeutralMode.Brake);
		verify(this.backRight, times(2)).setNeutralMode(NeutralMode.Brake);
	}

	@Test
	public void newPathIsNotFinished() {
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(new Waypoint(0, 0, 0, 0));
		waypoints.add(new Waypoint(100, 0, 0, 60));
		Path path = PathBuilder.buildPathFromWaypoints(waypoints);
		drive.setWantDrivePath(path, false);
		assertFalse(drive.isDoneWithPath());
	}

	@Test
	public void isFinishedReturnsTrueAfterPathFinished() {
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		waypoints.add(new Waypoint(0, 0, 0, 0));
		waypoints.add(new Waypoint(100, 0, 0, 60));
		Path path = PathBuilder.buildPathFromWaypoints(waypoints);
		drive.setWantDrivePath(path, false);

		InterpolatingDouble time = new InterpolatingDouble(90D);
		RigidTransform2d pos = new RigidTransform2d(new Translation2d(0, 1), Rotation2d.fromDegrees(0));
		Entry<InterpolatingDouble, RigidTransform2d> zeroState = new AbstractMap.SimpleEntry<InterpolatingDouble, RigidTransform2d>(
				time, pos);
		when(robotState.getLatestFieldToVehicle()).thenAnswer(i -> zeroState);
		when(robotState.getDistanceDriven()).thenAnswer(i -> 0D);

		Twist2d velocityZero = new Twist2d(0, 0, 0);
		when(robotState.getPredictedVelocity()).thenAnswer(i -> velocityZero);

		drive.updatePathFollower(0);

		time = new InterpolatingDouble(3D);
		pos = new RigidTransform2d(new Translation2d(99.9999, 0.0001), Rotation2d.fromDegrees(0));
		Entry<InterpolatingDouble, RigidTransform2d> entry2 = new AbstractMap.SimpleEntry<InterpolatingDouble, RigidTransform2d>(
				time, pos);
		when(robotState.getLatestFieldToVehicle()).thenAnswer(i -> entry2);
		when(robotState.getDistanceDriven()).thenAnswer(i -> 99.9999D);

		Twist2d velocity2 = new Twist2d(0, 0, 0);
		when(robotState.getPredictedVelocity()).thenAnswer(i -> velocity2);

		drive.updatePathFollower(3);
		assertTrue(drive.isDoneWithPath());
	}
	
	@Test
	public void getGyroAngleReturnsRotation2dOfGyroAngle() {
		when(this.gyro.getAngle()).thenReturn(90D);
		Rotation2d expected = Rotation2d.fromDegrees(90*Constants.GYRO_MODIFIER);
		assertEquals(expected, this.drive.getGyroAngle());
	}
	
	@Test
	public void getLeftVelocityInInchesPerSecondReturnsVelocity() {
		Constants.DRIVE_WHEEL_DIAMETER_INCHES = 1;
		Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT = Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT = 200;
		when(this.frontLeft.getSelectedSensorVelocity(0)).thenReturn(200);
		assertEquals(10*Math.PI, this.drive.getLeftVelocityInchesPerSec(), 0.00001);
	}
	
	@Test
	public void getRightVelocityInInchesPerSecondReturnsVelocity() {
		Constants.DRIVE_WHEEL_DIAMETER_INCHES = 1;
		Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT = Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT = 200;
		when(this.frontRight.getSelectedSensorVelocity(0)).thenReturn(200);
		assertEquals(10*Math.PI, this.drive.getRightVelocityInchesPerSec(), 0.00001);
	}

	private void verifyTalons(ControlMode mode, double frontLeft, double frontRight) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.frontLeft).set(eq(mode), captor.capture());
		assertEquals(frontLeft, (double) captor.getValue(), 0.00001);

		verify(this.frontRight).set(eq(mode), captor.capture());
		assertEquals(frontRight, (double) captor.getValue(), 0.00001);
	}

}

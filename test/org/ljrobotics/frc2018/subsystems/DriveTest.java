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
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.utils.Motion;
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

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.HLUsageReporting;

public class DriveTest {

	private Drive drive;
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;

	private RobotState robotState;
	
	static {
		// prevents exception during test
		HLUsageReporting.SetImplementation(new DummyReporter());
	}

	@Before
	public void before() {
		frontLeft = mock(CANTalon.class);
		frontRight = mock(CANTalon.class);
		backLeft = mock(CANTalon.class);
		backRight = mock(CANTalon.class);

		robotState = mock(RobotState.class);

		drive = new Drive(frontLeft, frontRight, backLeft, backRight, robotState);
	}

	@Test
	public void stopSetsTalonsToZero() {
		drive.stop();
		verifyTalons(0, 0, 0, 0);
	}

	@Test
	public void moveWithPotitiveY() {
		drive.move(new Motion(1, 0));
		verifyTalons(1, 1, 0, 0);
	}

	@Test
	public void moveWithNegativeY() {
		drive.move(new Motion(-1, 0));
		verifyTalons(-1, -1, 0, 0);
	}

	@Test
	public void moveWithPotitiveRotation() {
		drive.move(new Motion(0, 1));
		verifyTalons(1, -1, 0, 0);
	}

	@Test
	public void moveWithNegativeRotation() {
		drive.move(new Motion(0, -1));
		verifyTalons(-1, 1, 0, 0);
	}

	@Test
	public void moveWithPositiveYAndRotation() {
		drive.move(new Motion(0.5, 0.5));
		verifyTalons(1, 0, 0, 0);
	}

	@Test
	public void moveWithNegativeYAndRotation() {
		drive.move(new Motion(-0.5, -0.5));
		verifyTalons(-1, 0, 0, 0);
	}

	@Test
	public void moveWithValuesOverOne() {
		drive.move(new Motion(10, 5));
		verifyTalons(1, 0.333333, 0, 0);
	}

	@Test
	public void moveWithValuesUnderOne() {
		drive.move(new Motion(-10, -5));
		verifyTalons(-1, -0.333333, 0, 0);
	}

	@Test
	public void setBreakModeSetsBreakModeOnFirstCall() {
		drive.setBrakeMode(false);
		drive.setBrakeMode(false);
		verify(this.frontLeft, times(2)).enableBrakeMode(false);
		verify(this.frontRight, times(2)).enableBrakeMode(false);
		verify(this.backLeft, times(2)).enableBrakeMode(false);
		verify(this.backRight, times(2)).enableBrakeMode(false);
	}

	@Test
	public void setBreakModeSetsBreakModeAfterToggle() {
		drive.setBrakeMode(false);
		drive.setBrakeMode(true);
		verify(this.frontLeft, times(2)).enableBrakeMode(true);
		verify(this.frontRight, times(2)).enableBrakeMode(true);
		verify(this.backLeft, times(2)).enableBrakeMode(true);
		verify(this.backRight, times(2)).enableBrakeMode(true);
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

	private void verifyTalons(double frontLeft, double frontRight, double backLeft, double backRight) {
		final ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
		verify(this.frontLeft).set(captor.capture());
		assertEquals(frontLeft, (double) captor.getValue(), 0.00001);

		verify(this.frontRight).set(captor.capture());
		assertEquals(frontRight, (double) captor.getValue(), 0.00001);

		verify(this.backLeft).set(captor.capture());
		assertEquals(backLeft, (double) captor.getValue(), 0.00001);

		verify(this.backRight).set(captor.capture());
		assertEquals(backRight, (double) captor.getValue(), 0.00001);
	}

}

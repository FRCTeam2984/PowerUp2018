package org.ljrobotics.frc2018.loops;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class RobotStateEstimatorTest {

	private Drive drive;
	private RobotState robotState;
	private RobotStateEstimator robotStateEstimator;
	
	@Before
	public void before() {
		this.drive = mock(Drive.class);
		this.robotState = RobotState.getInstance();
		this.robotState.reset(0, new RigidTransform2d());
		this.robotStateEstimator = new RobotStateEstimator(this.drive, this.robotState);
	}
	
	@Test
	public void zeroMovementCausesAnObservationOfZero() {
		setLeftRightAndGyro(0, 0, 0, 0, 0);
		this.robotStateEstimator.onStart(0);
		setLeftRightAndGyro(0, 0, 0, 0, 0);
		this.robotStateEstimator.onLoop(1/400D);

		RigidTransform2d fieldToVehicle = new RigidTransform2d();
		assertEquals(fieldToVehicle, this.robotState.getFieldToVehicle(1/400D));
	}
	
	@Test
	public void movingForwardWithZeroVelocitiesShowsDisplacementButNoVelocity() {
		setLeftRightAndGyro(0, 0, 0, 0, 0);
		this.robotStateEstimator.onStart(0);
		setLeftRightAndGyro(1, 1, 0, 0, 0);
		this.robotStateEstimator.onLoop(1D);

		RigidTransform2d fieldToVehicle = RigidTransform2d.fromTranslation(new Translation2d(1,0));
		assertEquals(fieldToVehicle, this.robotState.getFieldToVehicle(1D));
		assertEquals(fieldToVehicle, this.robotState.getPredictedFieldToVehicle(1));
		
	}
	
	@Test
	public void turningAFullCircleShowsNoDifferenceInTranslation() {
		Constants.TRACK_WIDTH_INCHES = 1;
		setLeftRightAndGyro(0, 0, Math.PI, 0, 0);
		this.robotStateEstimator.onStart(0);
		setLeftRightAndGyro(Math.PI/2, 0, Math.PI, 0, -Math.PI/2);
		this.robotStateEstimator.onLoop(1D/2D);
		setLeftRightAndGyro(Math.PI, 0, Math.PI, 0, -Math.PI);
		this.robotStateEstimator.onLoop(2D/2D);
		setLeftRightAndGyro(3*Math.PI/2, 0, Math.PI, 0, -3*Math.PI/2);
		this.robotStateEstimator.onLoop(3D/2D);
		setLeftRightAndGyro(2*Math.PI, 0, Math.PI, 0, -2*Math.PI);
		this.robotStateEstimator.onLoop(4D/2D);

		RigidTransform2d fieldToVehicle = RigidTransform2d.fromRotation(Rotation2d.fromRadians(2*Math.PI));
		assertEquals(fieldToVehicle, this.robotState.getFieldToVehicle(2D));
		assertEquals(Math.PI, this.robotState.getDistanceDriven(), 0.00001);
	}
	
	public void setLeftRightAndGyro(double left, double right, double leftVel, double rightVel, double gyro) {
		when(this.drive.getLeftDistanceInches()).thenReturn(left);
		when(this.drive.getRightDistanceInches()).thenReturn(right);
		when(this.drive.getLeftVelocityInchesPerSec()).thenReturn(leftVel);
		when(this.drive.getRightVelocityInchesPerSec()).thenReturn(rightVel);
		when(this.drive.getGyroAngle()).thenReturn(Rotation2d.fromRadians(gyro));
	}
	
}

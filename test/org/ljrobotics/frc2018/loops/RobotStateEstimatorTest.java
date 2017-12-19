package org.ljrobotics.frc2018.loops;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;

public class RobotStateEstimatorTest {

	private Drive drive;
	private RobotState robotState;
	private RobotStateEstimator robotStateEstimator;
	
	@Before
	public void before() {
		this.drive = mock(Drive.class);
		this.robotState = RobotState.getInstance();
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
	
	public void setLeftRightAndGyro(double left, double right, double leftVel, double rightVel, double gyro) {
		when(this.drive.getLeftDistanceInches()).thenReturn(left);
		when(this.drive.getRightDistanceInches()).thenReturn(right);
		when(this.drive.getLeftVelocityInchesPerSec()).thenReturn(leftVel);
		when(this.drive.getRightVelocityInchesPerSec()).thenReturn(rightVel);
		when(this.drive.getGyroAngle()).thenReturn(Rotation2d.fromDegrees(gyro));
	}
	
}

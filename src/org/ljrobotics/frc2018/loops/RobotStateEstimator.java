package org.ljrobotics.frc2018.loops;

import org.ljrobotics.frc2018.state.Kinematics;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Twist2d;

/**
 * Periodically estimates the state of the robot using the robot's distance
 * traveled (compares two waypoints), gyroscope orientation, and velocity, among
 * various other factors. Similar to a car's odometer.
 */
public class RobotStateEstimator implements Loop {
	static RobotStateEstimator instance;

	public static RobotStateEstimator getInstance() {
		if (instance == null) {
			Drive drive = Drive.getInstance();
			RobotState robotState = RobotState.getInstance();
			instance = new RobotStateEstimator(drive, robotState);
		}
		return instance;
	}

	private RobotState robotState;
	private Drive drive;
	private double prevLeftEncoderDistance = 0;
	private double prevRightEncoderDistance = 0;
	
	public RobotStateEstimator(Drive drive, RobotState robotState) {
		this.drive = drive;
		this.robotState = robotState;
	}

	@Override
	public synchronized void onStart(double timestamp) {
		prevLeftEncoderDistance = drive.getLeftDistanceInches();
		prevRightEncoderDistance = drive.getRightDistanceInches();
	}

	@Override
	public synchronized void onLoop(double timestamp) {
		final double leftDistance = drive.getLeftDistanceInches();
		final double rightDistance = drive.getRightDistanceInches();
		final Rotation2d gyroAngle = drive.getGyroAngle();
		final Twist2d odometryVelocity = robotState.generateOdometryFromSensors(
				leftDistance - prevLeftEncoderDistance, rightDistance - prevRightEncoderDistance, gyroAngle);
		final Twist2d predictedVelocity = Kinematics.forwardKinematics(drive.getLeftVelocityInchesPerSec(),
				drive.getRightVelocityInchesPerSec());
		robotState.addObservations(timestamp, odometryVelocity, predictedVelocity);
		prevLeftEncoderDistance = leftDistance;
		prevRightEncoderDistance = rightDistance;
	}

	@Override
	public void onStop(double timestamp) {
		
	}

}
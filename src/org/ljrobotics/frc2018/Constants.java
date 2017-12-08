package org.ljrobotics.frc2018;

import org.ljrobotics.lib.util.ConstantsBase;

public class Constants extends ConstantsBase{

	public static double SEGMENT_COMPLETION_TOLERANCE = 0.1; //inches

	public static double PATH_FOLLOWING_MAX_ACCEL = 120; //inches per second^2
	public static double PATH_FOLLOWING_MAX_VEL = 120; //inches per second
	
	public static double PATH_FOLLWOING_GOAL_POS_TOLERANCE = 0.75;
	public static double PATH_FOLLOWING_GOAL_VEL_TOLERANCE = 12.0;
	public static double PATH_STOP_STEERING_DISTANCE = 9.0;

	public static double DRIVE_HIGH_GEAR_NOMINAL_OUTPUT = 0.5;

	public static double TRACK_SCRUB_FACTOR = 0.924; //Corrective factor for skidding

	public static double TRACK_WIDTH_INCHES = 20;

	public static double kMinLookAhead;
	public static double kMaxLookAhead;
	public static double kMinLookAheadSpeed;
	public static double kMaxLookAheadSpeed;
	public static double kInertiaSteeringGain;
	public static double kPathFollowingProfileKp;
	public static double kPathFollowingProfileKi;
	public static double kPathFollowingProfileKv;
	public static double kPathFollowingProfileKffv;
	public static double kPathFollowingProfileKffa;
	public static double kPathFollowingMaxVel;
	public static double kPathFollowingMaxAccel;
	public static double kPathFollowingGoalPosTolerance;
	public static double kPathFollowingGoalVelTolerance;
	public static double kPathStopSteeringDistance;
	public static double kDriveHighGearMaxSetpoint;
	public static double kDriveWheelDiameterInches;

	
	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}

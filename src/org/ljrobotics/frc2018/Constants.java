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

	public static double MIN_LOOK_AHEAD;
	public static double MAX_LOOK_AHEAD;
	public static double MIN_LOOK_AHEAD_SPEED;
	public static double MAX_LOOK_AHEAD_SPEED;
	public static double INERTIA_STEERING_GAIN;
	public static double PATH_FOLLWOING_PROFILE_Kp;
	public static double PATH_FOLLWOING_PROFILE_Ki;
	public static double PATH_FOLLWOING_PROFILE_Kv;
	public static double PATH_FOLLWOING_PROFILE_Kffv;
	public static double PATH_FOLLWOING_PROFILE_Kffa;
	public static double PATH_FOLLOING_GOAL_POS_TOLERANCE = 0.75;
	public static double PATH_FOLLOING_GOAL_VEL_TOLERANCE = 12;
	public static double DRIVE_MAX_SETPOINT;
	public static double DRIVE_WHEEL_DIAMETER_INCHES;

	public static double LOOPER_Dt;

	public static final int FRONT_LEFT_MOTOR_ID =  0x0;
	public static final int FRONT_RIGHT_MOTOR_ID = 0x1;
	public static final int REAR_LEFT_MOTOR_ID =   0x2;
	public static final int REAR_RIGHT_MOTOR_ID =  0x3;

	public static int JOYSTICK_DRIVE_ID =  0x1;


	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}

/*
           _____                    _____                    _____
         /\    \                  /\    \                  /\    \
        /::\    \                /::\    \                /::\    \
       /::::\    \              /::::\    \              /::::\    \
      /::::::\    \            /::::::\    \            /::::::\    \
     /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \
    /:::/__\:::\    \        /:::/__\:::\    \        /:::/  \:::\    \
   /::::\   \:::\    \      /::::\   \:::\    \      /:::/    \:::\    \
  /::::::\   \:::\    \    /::::::\   \:::\    \    /:::/    / \:::\    \
 /:::/\:::\   \:::\    \  /:::/\:::\   \:::\____\  /:::/    /   \:::\    \
/:::/  \:::\   \:::\____\/:::/  \:::\   \:::|    |/:::/____/     \:::\____\
\::/    \:::\   \::/    /\::/   |::::\  /:::|____|\:::\    \      \::/    /
 \/____/ \:::\   \/____/  \/____|:::::\/:::/    /  \:::\    \      \/____/
          \:::\    \            |:::::::::/    /    \:::\    \
           \:::\____\           |::|\::::/    /      \:::\    \
            \::/    /           |::| \::/____/        \:::\    \
             \/____/            |::|  ~|               \:::\    \
                                |::|   |                \:::\    \
                                \::|   |                 \:::\____\
                                 \:|   |                  \::/    /
                                  \|___|                   \/____/

*/
package org.ljrobotics.frc2018;

import org.ljrobotics.lib.util.ConstantsBase;

public class Constants extends ConstantsBase{
	// Gyroscope configuration

	public static double GYRO_MODIFIER = 1;
	// Path following configuration

	public static double SEGMENT_COMPLETION_TOLERANCE = 0.1; //inches

	public static double PATH_FOLLOWING_MAX_ACCEL = 120; //inches per second^2
	public static double PATH_FOLLOWING_MAX_VEL = 120; //inches per second

	public static double PATH_FOLLOWING_GOAL_POS_TOLERANCE = 0.75;
	public static double PATH_FOLLOWING_GOAL_VEL_TOLERANCE = 12.0;
	public static double PATH_STOP_STEERING_DISTANCE = 9.0;
	public static double DRIVE_HIGH_GEAR_NOMINAL_OUTPUT = 0.5;

	public static double TRACK_SCRUB_FACTOR = 0.924; //Corrective factor for skidding

	public static double TRACK_WIDTH_INCHES = 19.75;
	// To edit for path following

	public static double MIN_LOOK_AHEAD = 12;
	public static double MAX_LOOK_AHEAD = 24;
	public static double MIN_LOOK_AHEAD_SPEED = 9;
	public static double MAX_LOOK_AHEAD_SPEED = 120;

	public static double PATH_FOLLOWING_PROFILE_Kp = 0.5;
	public static double PATH_FOLLOWING_PROFILE_Ki = 0.003;
	public static double PATH_FOLLOWING_PROFILE_Kv = 0.02;
	public static double PATH_FOLLOWING_PROFILE_Kffv = 0.5;
	public static double PATH_FOLLOWING_PROFILE_Kffa = 0.05;
	// Do not touch

	public static double INERTIA_STEERING_GAIN = 0;

	public static double DRIVE_MAX_SETPOINT = 100; //Inches Per Second
	public static double DRIVE_WHEEL_DIAMETER_INCHES = 6;
	public static double DRIVE_WHEEL_CIRCUMFERENCE = DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
	public static int DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT = 248;
	public static int DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT = -1470;
	// RIGHT FOR 2018: 141.895/36.25*360, Left for 2018: -34.558/36.25*-1440
	// RIGHT FOR 2017: 32/39*303    , left for 2017: 24.488/25*-1440

	public static double DRIVE_Kp = 0.00005;
	public static double DRIVE_Ki = 0.0000001;
	public static double DRIVE_Kd = 0;
	public static double DRIVE_Kf = 0.035;
	// -----------------------------------------------------------------------------------------------
	// Intake
	public static double SUCK_SPEED = 1;
	public static double SPIT_SPEED = -1;

	//turn pid
	public static double TURN_Kp = 0.0048;
	public static double TURN_Ki = 0.0016D;
	public static double TURN_Kd = 0.0D;
	public static double TURN_Kf = 0D;
	public static int LEFT_INTAKE_MOTOR_ID = 29;
	public static int RIGHT_INTAKE_MOTOR_ID = 28;
	public static int TENSION_INTAKE_MOTOR_ID = 20;

	//turn constants
	//degrees
	public static double TURN_DEGREE_TOLERANCE = 1D;
	//gyroscope is turning under some velocity (in degrees / second)
	public static double LOW_VELOCITY_THRESHOLD = 1;
	public static int MAX_SUCK_CURRENT = 20;
	public static int NOMINAL_SUCK_CURRENT = 10;
	public static int MAX_SUCK_CURRENT_TIME = 1000;
	// Speed
	public static double TURN_SPEED = 1.0;
	// -----------------------------------------------------------------------------------------------
	// Control Config

	public static int JOYSTICK_ROTATION_AXIS = 2;
	public static boolean USE_TANK_DRIVE = false;
	public static int JOYSTICK_POWER_POWER = 3;
	public static double JOYSTICK_POWER_DEADBAND = 0.05;
	public static double JOYSTICK_WHEEL_DEADBAND = 0.05;
	public static int QUICKTURN_BUTTON = 7;
	// -----------------------------------------------------------------------------------------------
	// Loopers

	public static double LOOPER_Dt = 0.005;
	// -----------------------------------------------------------------------------------------------
	// Android configuration

	public static int ANDROID_APP_TCP_PORT = 8254;
	// -----------------------------------------------------------------------------------------------
	// Motor configuration

	public static int JOYSTICK_DRIVE_ID =  0x0;
	public static int JOYSTICK_OPERATOR_ID = 1;
	public static final int FRONT_LEFT_MOTOR_ID =  12;
	public static final int FRONT_RIGHT_MOTOR_ID = 10;
	public static final int REAR_LEFT_MOTOR_ID =   23;
	public static final int REAR_RIGHT_MOTOR_ID =  22;
	// -----------------------------------------------------------------------------------------------
	// General
	public static double EQUALS_EPSILON = 0.00001;

	// -----------------------------------------------------------------------------------------------
	//Camera position and rotation

	public static double CAMERA_X_OFFSET = 0; //From wheelbase center, looking down
	public static double CAMERA_Y_OFFSET = 0; //From wheelbase center, looking down
	public static double CAMERA_YAW = 0; //In degrees, + is anti-clockwise viewed from above

	public static double MAX_TRACKER_DISTANCE = 18;
	public static double MAX_GOAL_TRACK_AGE = 1;
	public static double CAMERA_FRAME_RATE = 30;
	// -----------------------------------------------------------------------------------------------
	// File location

	public static int MAX_ARM_CURRENT = 20;

	public static int NOMINAL_ARM_CURRENT = 10;

	public static int MAX_ARM_CURRENT_TIME = 1;

	public static int LEFT_ARM_MOTOR_ID = 28;

	public static int RIGHT_ARM_MOTOR_ID = 29;

	@Override
	public String getFileLocation() {
		// TODO Fill in file once a location is chosen
		return "/home/lvuser/constants";
	}


	/*
	{
        "DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT":1439,
        "DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT":1418,
        "GYRO_MODIFIER":-1,
        "DRIVE_Kf": 0.00875,
        "DRIVE_Kp": 0.015,
        "DRIVE_Ki": 0,
        "DRIVE_Kd": -0.0003,
        "PATH_FOLLOWING_GOAL_POS_TOLERANCE": 3,
        "PATH_FOLLOWING_MAX_ACCEL": 60,
        "PATH_FOLLOWING_MAX_VEL": 80,
        "PATH_FOLLOWING_PROFILE_Kffa": 0.05,
        "MIN_LOOK_AHEAD_SPEED": 9,
        "PATH_FOLLOWING_PROFILE_Kp": 1.5,
        "PATH_FOLLOWING_PROFILE_Ki": 0.002,
        "MIN_LOOK_AHEAD": 12,
        "PATH_FOLLOWING_PROFILE_Kv": 0.1,
        "SEGMENT_COMPLETION_TOLERANCE": 4,
        "JOYSTICK_ROTATION_AXIS": 2,
        "USE_TANK_DRIVE": false,
        "QUICKTURN_BUTTON": 5,
        "JOYSTICK_POWER_POWER": 1
	}*/
}

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
	public static int DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT = 248;
	public static int DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT = -1470;
	// RIGHT FOR 2018: 141.895/36.25*360, Left for 2018: -34.558/36.25*-1440
	// RIGHT FOR 2017: 32/39*303    , left for 2017: 24.488/25*-1440
	
	public static double DRIVE_Kp = 0.00005;
	public static double DRIVE_Ki = 0.0000001;
	public static double DRIVE_Kd = 0;
	public static double DRIVE_Kf = 0.035;
	// -----------------------------------------------------------------------------------------------
	// Loopers
	
	public static double LOOPER_Dt = 0.005;
	// -----------------------------------------------------------------------------------------------
	// Android configuration

	public static int ANDROID_APP_TCP_PORT = 8254;
	// -----------------------------------------------------------------------------------------------
	// Motor configuration

	public static int JOYSTICK_DRIVE_ID =  0x0;
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

	@Override
	public String getFileLocation() {
		// TODO Fill in file once a location is chosen
		return "/home/lvuser/constants";
	}

}

package org.ljrobotics.frc2018;

import org.ljrobotics.lib.util.ConstantsBase;

public class Constants extends ConstantsBase{

	public static double SEGMENT_COMPLETION_TOLERANCE = 0.1; //inches

	public static double PATH_FOLLOWING_MAX_ACCEL = 120; //inches per second^2
	public static double PATH_FOLLOWING_MAX_VEL = 120; //inches per second
	
	public static double PATH_FOLLWOING_GOAL_POS_TOLERANCE = 0.75;
	public static double PATH_FOLLOWING_GOAL_VEL_TOLERANCE = 12.0;
	public static double PATH_STOP_STEERING_DISTANCE = 9.0;
	
	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}

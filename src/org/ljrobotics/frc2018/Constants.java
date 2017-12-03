package org.ljrobotics.frc2018;

import org.ljrobotics.lib.util.ConstantsBase;
import org.ljrobotics.lib.util.math.PolynomialRegression;

public class Constants extends ConstantsBase{

	public static double PATH_FOLLOWING_MAX_ACCEL;
	public static double SEGMENT_COMPLETION_TOLERANCE;
	public static double kPathFollowingMaxVel;
	public static double kPathFollowingGoalPosTolerance;
	public static double kPathFollowingGoalVelTolerance;
	public static double kPathStopSteeringDistance;
	public static PolynomialRegression kFlywheelAutoAimPolynomial;

	@Override
	public String getFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}

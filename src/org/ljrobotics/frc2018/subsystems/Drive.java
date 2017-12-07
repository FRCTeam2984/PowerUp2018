package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.RobotMap;
import org.ljrobotics.frc2018.loops.Looper;

import com.ctre.CANTalon;

/**
 * The Drive subsystem. This subsystem is responsible for everything
 * regarding driving. It controls the four drive motors.
 * 
 * @author Max
 *
 */
public class Drive extends Subsystem{

	private static Drive instance;

	public static Drive getInstance() {
		if (instance == null) {
			CANTalon frontLeft = new CANTalon(RobotMap.FRONT_LEFT_MOTOR_ID);
			CANTalon frontRight = new CANTalon(RobotMap.FRONT_RIGHT_MOTOR_ID);
			CANTalon rearLeft = new CANTalon(RobotMap.REAR_LEFT_MOTOR_ID);
			CANTalon rearRight = new CANTalon(RobotMap.REAR_RIGHT_MOTOR_ID);

			instance = new Drive(frontLeft, frontRight, rearLeft, rearRight);
		}
		return instance;
	}
	
	// The robot drivetrain's various states.
	public enum DriveControlState {
		VELOCITY_SETPOINT //Under PID velocity control
	}
	
	private CANTalon frontLeft;
	private CANTalon frontRight;
	private CANTalon backLeft;
	private CANTalon backRight;
	
	private DriveControlState driveControlState;

	/**
	 * Creates a new Drive Subsystem from that controls the given motor controllers.
	 * 
	 * @param frontLeft the font left talon motor controller
	 * @param frontRight the font right talon motor controller
	 * @param backLeft the back left talon motor controller
	 * @param backRight the back right talon motor controller
	 */
	public Drive(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight) {

		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
		
		this.driveControlState = DriveControlState.VELOCITY_SETPOINT;

	}

	@Override
	public void stop() {
		this.frontLeft.set(0);
		this.frontRight.set(0);
		this.backLeft.set(0);
		this.backRight.set(0);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}

}

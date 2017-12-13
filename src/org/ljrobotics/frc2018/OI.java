package org.ljrobotics.frc2018;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private static OI instance;

	public static OI getInstance() {
		if (instance == null) {
			instance = new OI();
		}
		return instance;
	}

	/**
	 * The joystick used to control the driving of the robot
	 */
	public Joystick stick;

	private OI() {
		this.stick = new Joystick(Constants.JOYSTICK_DRIVE_ID);
	}
}

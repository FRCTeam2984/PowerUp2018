package org.ljrobotics.frc2018;

import org.ljrobotics.frc2018.commands.IntakeIdle;
import org.ljrobotics.frc2018.commands.IntakeSpit;
import org.ljrobotics.frc2018.commands.IntakeSuck;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

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
		JoystickButton suck = new JoystickButton(this.stick, 2);
		suck.whenPressed(new IntakeSuck());
		suck.whenReleased(new IntakeIdle());
		
		JoystickButton spit = new JoystickButton(this.stick, 4);
		spit.whenPressed(new IntakeSpit());
		spit.whenReleased(new IntakeIdle());
	}
}

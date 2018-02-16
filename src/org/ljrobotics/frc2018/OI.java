package org.ljrobotics.frc2018;

import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.commands.IntakeIdle;
import org.ljrobotics.frc2018.commands.IntakeSpit;
import org.ljrobotics.frc2018.commands.IntakeSuck;
import org.ljrobotics.frc2018.subsystems.Arm;

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

	public Joystick stick2;

	private OI() {
		this.stick = new Joystick(Constants.JOYSTICK_DRIVE_ID);
		this.stick2 = new Joystick(Constants.JOYSTICK_OPERATOR_ID);
		
		JoystickButton intake = new JoystickButton(this.stick2, 5);
		intake.whenPressed(new IntakeSuck());
		intake.whenReleased(new IntakeIdle());
		
		JoystickButton spit = new JoystickButton(this.stick2, 6);
		spit.whenPressed(new IntakeSpit());
		spit.whenReleased(new IntakeIdle());
		
		JoystickButton button1 = new JoystickButton(this.stick2, 1);
		JoystickButton button2 = new JoystickButton(this.stick2, 2);
		JoystickButton button3 = new JoystickButton(this.stick2, 3);
		JoystickButton button4 = new JoystickButton(this.stick2, 4);

		button1.whenPressed(new ArmSetpoint(Arm.ArmPosition.STOWED));
		button2.whenPressed(new ArmSetpoint(Arm.ArmPosition.INTAKE));
		button3.whenPressed(new ArmSetpoint(Arm.ArmPosition.SWITCH));
		button4.whenPressed(new ArmSetpoint(Arm.ArmPosition.SCALE));
//		button2.whenPressed(new InstantCommand() {
//			@Override
//			public void execute() {
//				Arm.getInstance().zeroSensors();
//				Arm.getInstance().setWantedState(ArmControlState.Idle);
//			}
//		});

		// JoystickButton suck = new JoystickButton(this.stick2, 1);
		// suck.whenPressed(new	 IntakeSuck());
		// suck.whenReleased(new IntakeIdle());
		//
		// JoystickButton spit = new JoystickButton(this.stick2, 4);
		// spit.whenPressed(new IntakeSpit());
		// spit.whenReleased(new IntakeIdle());
	}
}

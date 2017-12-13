package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.frc2018.utils.Motion;
import org.ljrobotics.frc2018.Constants;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class JoystickDrive extends Command {

	Joystick joystick;
	double multiplier;

	public JoystickDrive() {
		this.requires(Drive.getInstance());
	}

	protected void initialize() {
		this.joystick = new Joystick(Constants.JOYSTICK_DRIVE_ID);
		this.multiplier=1;
	}

	protected void execute() {
		double y = -this.joystick.getRawAxis(1)*this.multiplier;
		double rotation = this.joystick.getRawAxis(2)*this.multiplier;
		Drive.getInstance().move(new Motion(y,rotation));
	}

	protected void end() {
	}

	protected void interrupted() {
	}

	protected boolean isFinished(){
		return false;
	}

}

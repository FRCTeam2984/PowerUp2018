package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.frc2018.utils.Motion;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class JoystickDrive extends Command {

	Joystick joystick;
	double multiplier;

	public JoystickDrive() {
		this.requires(Drive.getInstance());
		this.multiplier = 1;
		this.joystick = OI.getInstance().stick;
	}

	protected void initialize() {

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

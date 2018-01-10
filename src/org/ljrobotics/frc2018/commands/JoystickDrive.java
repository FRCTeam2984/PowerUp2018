package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.CheesyDriveHelper;
import org.ljrobotics.lib.util.DriveSignal;

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
		double power = -this.joystick.getRawAxis(1)*this.multiplier;
		double wheel = this.joystick.getRawAxis(4)*this.multiplier;
		DriveSignal driveSignal = CheesyDriveHelper.getInstance().cheesyDrive(power, wheel, false, false);
		Drive.getInstance().setOpenLoop(driveSignal);
	}

	protected void end() {
	}

	protected void interrupted() {
	}

	protected boolean isFinished(){
		return false;
	}

}

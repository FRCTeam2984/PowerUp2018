package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.CheesyDriveHelper;
import org.ljrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class TankJoystickDrive extends Command {

	Joystick joystick;
	double multiplier;

	public TankJoystickDrive() {
		this.requires(Drive.getInstance());
		this.multiplier = 1;
		this.joystick = OI.getInstance().stick;
	}

	protected void initialize() {

	}

	protected void execute() {
		double power = this.joystick.getRawAxis(1)*this.multiplier;
		power = Math.pow(power, 5);
		double wheel = this.joystick.getRawAxis(Constants.JOYSTICK_ROTATION_AXIS)*this.multiplier;
		wheel = (Math.abs(wheel) < 0.05) ? 0 : wheel;
		double left = power + wheel;
		double right = power - wheel;
		DriveSignal driveSignal = new DriveSignal(left, right);
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

package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.CheesyDriveHelper;
import org.ljrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class CheesyJoystickDrive extends Command {

	Joystick joystick;
	double multiplier;

	public CheesyJoystickDrive() {
		this.requires(Drive.getInstance());
		this.multiplier = 1;
		this.joystick = OI.getInstance().stick;
	}

	protected void initialize() {

	}

	protected void execute() {
		double power = this.joystick.getRawAxis(1)*this.multiplier;
		power = (Math.abs(power) < Constants.JOYSTICK_POWER_DEADBAND) ? 0 : power;
		power = Math.pow(power, Constants.JOYSTICK_POWER_POWER);
		double wheel = this.joystick.getRawAxis(Constants.JOYSTICK_ROTATION_AXIS)*this.multiplier;
		wheel = (Math.abs(wheel) < Constants.JOYSTICK_WHEEL_DEADBAND) ? 0 : wheel;
		DriveSignal driveSignal;
		// The rightmost trigger
		if( this.joystick.getRawButton(Constants.QUICKTURN_BUTTON) ) {
			driveSignal = CheesyDriveHelper.getInstance().cheesyDrive(power, wheel, true, false);
		} else {
			driveSignal = CheesyDriveHelper.getInstance().cheesyDrive(power, wheel, false, false);
		}
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

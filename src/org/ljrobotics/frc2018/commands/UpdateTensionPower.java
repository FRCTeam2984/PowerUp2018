package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class UpdateTensionPower extends Command {

	Joystick joystick;

	public UpdateTensionPower() {
		this.requires(Intake.getInstance());
		this.joystick = OI.getInstance().stick2;
	}

	protected void initialize() {

	}

	protected void execute() {
		double power = this.joystick.getRawAxis(1);
		power = (Math.abs(power) < 0.05) ? 0 : power;
		power /= 2;
		Intake.getInstance().setTensionPower(power);
	}

	protected void end() {
	}

	protected void interrupted() {
	}

	protected boolean isFinished(){
		return false;
	}

}

package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.frc2018.subsystems.Arm.ArmControlState;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class MoveArmWithJoystick extends Command {

	Joystick joystick;

	public MoveArmWithJoystick() {
		this.requires(Arm.getInstance());
		this.joystick = OI.getInstance().stick2;
	}

	protected void initialize() {
		Arm.getInstance().setWantedState(ArmControlState.Moving);
	}

	protected void execute() {
		double power = this.joystick.getRawAxis(1)*0.4;
		Arm.getInstance().setWantedSpeed(power);
	}

	protected void end() {
	}

	protected void interrupted() {
	}

	protected boolean isFinished(){
		return false;
	}

}

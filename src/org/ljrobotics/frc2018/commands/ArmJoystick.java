package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmJoystick extends InstantCommand {
	Joystick joystick;

	public ArmJoystick() {
		requires(Arm.getInstance());
		this.joystick = OI.getInstance().stick2;
		
	}

	@Override
	protected void execute() {
		double power = this.joystick.getRawAxis(1) * Constants.MAX_ARM_SPEED;
		System.out.println("Joysticking");
		Arm.getInstance().setWantedSpeed(power);
	}

	@Override
	protected void end() {
		// Arm.getInstance().setWantedState(ArmControlState.Idle);
	}

}

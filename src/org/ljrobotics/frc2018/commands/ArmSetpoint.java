package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmSetpoint extends InstantCommand {

	
	public ArmSetpoint() {
		requires(Arm.getInstance());
	}
	
	@Override
	protected void execute() {
		System.out.println("Arming");
		Arm.getInstance().setAngleSetpoint(180);
	}

}

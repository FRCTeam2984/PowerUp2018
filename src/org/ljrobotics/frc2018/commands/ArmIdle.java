package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.frc2018.subsystems.Intake;
import org.ljrobotics.frc2018.subsystems.Arm.ArmControlState;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmIdle extends InstantCommand {

	
	public ArmIdle() {
		requires(Arm.getInstance());
	}
	
	@Override
	protected void execute() {
		System.out.println("Idling");
		Arm.getInstance().setWantedState(ArmControlState.Idle);
	}

}

package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class IntakeIdle extends InstantCommand {

	
	public IntakeIdle() {
		requires(Intake.getInstance());
	}
	
	@Override
	protected void execute() {
		Intake.getInstance().setWantedState(Intake.IntakeControlState.Idle);

	}

}

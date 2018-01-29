package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class IntakeSpit extends InstantCommand {

	
	public IntakeSpit() {
		requires(Intake.getInstance());
	}
	
	@Override
	protected void execute() {
		Intake.getInstance().setWantedState(Intake.IntakeControlState.Spit);

	}

}

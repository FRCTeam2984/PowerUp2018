package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class IntakeSuck extends InstantCommand {

	
	public IntakeSuck() {
		requires(Intake.getInstance());
	}
	
	@Override
	protected void execute() {
		System.out.println("Sucking");
		Intake.getInstance().setWantedState(Intake.IntakeControlState.Suck);

	}

}

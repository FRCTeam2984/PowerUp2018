package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StowArmAndStopIntake extends CommandGroup{

	public StowArmAndStopIntake() {
		this.addParallel(new IntakeIdle());
	}
	
}

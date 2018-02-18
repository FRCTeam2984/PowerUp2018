package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;

public class ConditionalStowArm extends InstantCommand {

	@Override
	protected void execute() {
		if(Arm.getInstance().getLastState() == Arm.ArmPosition.INTAKE &&
				!DriverStation.getInstance().isAutonomous()) {
			System.out.println("Stowing Arm and stopping Intake");
			Scheduler.getInstance().add(new StowArmAndStopIntake());
		}
	}

}

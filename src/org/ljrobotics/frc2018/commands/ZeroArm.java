package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

public class ZeroArm extends Command {

	public ZeroArm() {
		requires(Arm.getInstance());
	}
	
	@Override
	public void execute() {
		Arm.getInstance().setWantedSpeed(-0.05);
	}
	
	@Override
	protected boolean isFinished() {
		return Arm.getInstance().atLowerLimit();
	}
	
	public void end() {
		System.out.println("Arm Zeroed");
	}

}

package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.frc2018.subsystems.Arm.ArmControlState;
import org.ljrobotics.frc2018.subsystems.Drive;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LimitSpeed extends InstantCommand {

	private double limit;
	
	public LimitSpeed(double limit) {
		this.limit = limit;
	}
	
	@Override
	protected void execute() {
		Drive.getInstance().setSpeedLimit(this.limit);
	}

}

package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.frc2018.subsystems.Intake;
import org.ljrobotics.frc2018.commands.ArmSetpoint;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmPosition extends InstantCommand {
	private Arm.ArmPosition position;

	public ArmPosition(Arm.ArmPosition position) {
		this.position = position;
		requires(Arm.getInstance());
	}

	@Override
	protected void execute() {
		Arm.getInstance().setWantedPosition(position);
	}

}

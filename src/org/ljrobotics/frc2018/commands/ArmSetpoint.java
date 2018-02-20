package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmSetpoint extends InstantCommand {
	private Arm.ArmPosition setpoint;

	public ArmSetpoint(Arm.ArmPosition position) {
		this.setpoint = position;
		requires(Arm.getInstance());
	}

	public ArmSetpoint() {
		this(Arm.ArmPosition.STOWED);
	}

	@Override
	protected void execute() {
		System.out.println("Setpointing");
		Arm.getInstance().setPosition(this.setpoint);
	}

}

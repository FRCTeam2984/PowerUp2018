package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.Arm;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class ArmSetpoint extends InstantCommand {
	private double setpoint;

	public ArmSetpoint(double setpoint) {
		this.setpoint = setpoint;
		requires(Arm.getInstance());
	}
	
	public ArmSetpoint(Arm.ArmPosition position) {
		this(position.getAngle());
	}

	public ArmSetpoint() {
		this(0);
	}

	@Override
	protected void execute() {
		System.out.println("Setpointing");
		Arm.getInstance().setPosition(this.setpoint);
	}

}

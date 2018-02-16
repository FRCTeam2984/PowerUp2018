package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.LEDControl;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class SetLEDMode extends InstantCommand {

	
	private LEDControl.LEDState state;
	
	public SetLEDMode(LEDControl.LEDState state) {
		requires(LEDControl.getInstance());
		this.state = state;
	}
	
	@Override
	protected void execute() {
		LEDControl.getInstance().setWantedState(this.state);

	}

}

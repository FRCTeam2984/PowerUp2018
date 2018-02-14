package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.paths.ShortRightSwitch;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ArmMultipoint extends CommandGroup{

	public ArmMultipoint() {
		this.addSequential(new ArmSetpoint(10000));
		this.addSequential(new ArmSetpoint(20000));
	}
	
}
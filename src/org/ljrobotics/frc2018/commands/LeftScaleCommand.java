package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.paths.LeftScale;
import org.ljrobotics.frc2018.paths.ShortRightSwitch;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftScaleCommand extends CommandGroup{

	public LeftScaleCommand() {
		PathContainer path = new LeftScale();
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new FollowPath(path));
		this.addSequential(new TurnToAngle(90));
	}
	
}

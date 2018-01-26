package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.paths.ShortRightSwitch;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitchCommand extends CommandGroup{

	public RightSwitchCommand() {
		PathContainer path = new ShortRightSwitch();
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new FollowPath(path));
		this.addSequential(new DriveForward(0.2, 1));
	}
	
}

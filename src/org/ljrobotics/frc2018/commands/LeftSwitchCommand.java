package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.paths.ShortLeftSwitch;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSwitchCommand extends CommandGroup{

	public LeftSwitchCommand() {
		PathContainer path = new ShortLeftSwitch();
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new FollowPath(path));
		this.addSequential(new DriveForward(0.2, 1));
	}
	
}

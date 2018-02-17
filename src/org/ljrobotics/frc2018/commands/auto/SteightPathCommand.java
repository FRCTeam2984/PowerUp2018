package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.paths.SteightPath;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SteightPathCommand extends CommandGroup{

	public SteightPathCommand() {
		PathContainer path = new SteightPath();
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new FollowPath(path));
	}
	
}

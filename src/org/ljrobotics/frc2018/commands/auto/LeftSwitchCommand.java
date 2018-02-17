package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.DriveForward;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.paths.CenterLeftSwitch;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftSwitchCommand extends CommandGroup{

	public LeftSwitchCommand() {
		PathContainer path = new CenterLeftSwitch();
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new FollowPath(path));
		this.addSequential(new DriveForward(0.2, 1));
	}
	
}

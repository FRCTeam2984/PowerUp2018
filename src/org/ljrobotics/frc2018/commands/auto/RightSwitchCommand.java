package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.commands.DriveForward;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.IntakeSpit;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.commands.ZeroArm;
import org.ljrobotics.frc2018.paths.CenterRightSwitch;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightSwitchCommand extends CommandGroup{

	public RightSwitchCommand() {
		PathContainer path = new CenterRightSwitch();
		this.addParallel(new ZeroArm());
		this.addSequential(new ResetToPathHead(path));
		this.addParallel(new ArmSetpoint(Arm.ArmPosition.SWITCH));
		this.addSequential(new FollowPath(path));
		this.addSequential(new DriveForward(0.2, 1));
		this.addSequential(new IntakeSpit());
	}
	
}

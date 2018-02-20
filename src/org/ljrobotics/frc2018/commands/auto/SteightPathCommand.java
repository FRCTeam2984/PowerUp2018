package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.commands.WaitSecond;
import org.ljrobotics.frc2018.commands.ZeroArm;
import org.ljrobotics.frc2018.paths.SteightPath;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SteightPathCommand extends CommandGroup{

	public SteightPathCommand() {
		PathContainer path = new SteightPath();
		this.addParallel(new ZeroArm());
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.INTAKE));
		this.addSequential(new WaitSecond(100));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.STOWED));
		this.addSequential(new WaitSecond(500));
		this.addSequential(new FollowPath(path));
	}
	
}

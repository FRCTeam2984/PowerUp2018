package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.paths.LeftScale;
import org.ljrobotics.frc2018.paths.ShortRightSwitch;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftScaleCommand extends CommandGroup{

	public LeftScaleCommand() {
		PathContainer path = new LeftScale();
		this.addParallel(new ZeroArm());
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.STOWED));
		this.addSequential(new WaitSecond(500));
		this.addSequential(new FollowPath(path));
		this.addSequential(new TurnToAngle(-90));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.SCALE));
		this.addSequential(new WaitSecond(1500));
		this.addSequential(new IntakeSpit());
	}
	
}

package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.commands.DriveForward;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.IntakeIdle;
import org.ljrobotics.frc2018.commands.IntakeSpit;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.commands.TurnToAngle;
import org.ljrobotics.frc2018.commands.WaitSecond;
import org.ljrobotics.frc2018.commands.ZeroArm;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ScaleCommand extends CommandGroup{

	public ScaleCommand(PathContainer path, double endingAngle) {
		this.addParallel(new ZeroArm());
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.INTAKE));
		this.addSequential(new WaitSecond(100));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.STOWED));
		this.addSequential(new WaitSecond(500));
		this.addSequential(new FollowPath(path));
		this.addSequential(new TurnToAngle(endingAngle));
		this.addSequential(new DriveForward(0, 0));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.SCALE));
		this.addSequential(new WaitSecond(2000));
		this.addSequential(new IntakeSpit());
		this.addSequential(new WaitSecond(1000));
		this.addSequential(new IntakeIdle());
	}
	
}

package org.ljrobotics.frc2018.commands.auto;

import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.commands.DriveForward;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.IntakeIdle;
import org.ljrobotics.frc2018.commands.IntakeSpit;
import org.ljrobotics.frc2018.commands.IntakeSuck;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.commands.TurnToAngle;
import org.ljrobotics.frc2018.commands.WaitSecond;
import org.ljrobotics.frc2018.commands.ZeroArm;
import org.ljrobotics.frc2018.paths.CenterLeftScaleFast;
import org.ljrobotics.frc2018.paths.LeftScaleToSwitch;
import org.ljrobotics.frc2018.subsystems.Arm;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class TwoCubeCenterLeft extends CommandGroup{

	public TwoCubeCenterLeft() {
		PathContainer path = new CenterLeftScaleFast();
		this.addParallel(new ZeroArm());
		this.addSequential(new ResetToPathHead(path));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.INTAKE));
		this.addSequential(new WaitSecond(20));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.STOWED));
		this.addSequential(new WaitSecond(300));
		this.addSequential(new FollowPath(path));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.SCALE));
		this.addSequential(new TurnToAngle(90));
		this.addSequential(new WaitSecond(2000));
		this.addSequential(new IntakeSpit());
		this.addSequential(new WaitSecond(1000));
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.INTAKE));
		this.addSequential(new IntakeIdle());
		this.addSequential(new TurnToAngle(180));
		PathContainer toTheSwitch = new LeftScaleToSwitch();
		this.addSequential(new ResetToPathHead(toTheSwitch));
		this.addSequential(new IntakeSuck());
		this.addSequential(new FollowPath(toTheSwitch));
		this.addSequential(new DriveForward(0.3, 0.5));
		this.addSequential(new DriveForward(-0.3, 0.1));
		this.addSequential(new IntakeIdle());
		this.addSequential(new ArmSetpoint(Arm.ArmPosition.SCALE));
		this.addSequential(new WaitSecond(100));
		this.addSequential(new DriveForward(0.4, 0.1));
		this.addSequential(new IntakeSpit());
	}
	
}

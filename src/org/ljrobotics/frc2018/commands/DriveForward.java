package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.DriveSignal;
import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForward extends Command {

	private double startTime;
	private double time;
	private double speed;
	
	/**
	 * 
	 * @param speed speed in percent output
	 * @param time time in seconds
	 */
	public DriveForward(double speed, double time) {
		this.speed = speed;
		this.time = time;
		this.requires(Drive.getInstance());
	}
	
	@Override
	protected void initialize() {
		startTime = Timer.getFPGATimestamp();
	}
	
	@Override
	protected boolean isFinished() {
		return Timer.getFPGATimestamp() > startTime + time;
	}
	
	@Override
	protected void end() {
		Drive.getInstance().setOpenLoop(new DriveSignal(0,0));
	}
	
	@Override
	protected void execute() {
		Drive.getInstance().setOpenLoop(new DriveSignal(speed,speed));
	}

}

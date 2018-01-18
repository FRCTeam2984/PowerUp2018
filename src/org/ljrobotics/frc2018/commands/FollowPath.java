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

public class FollowPath extends Command {

	private PathContainer pathContainer;
	private Path path;
	private long startTime;
	
	public FollowPath(PathContainer pathContainer) {
		this.pathContainer = pathContainer;
		this.path = this.pathContainer.buildPath();
		this.requires(Drive.getInstance());
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		SmartDashboard.putNumber("starttime", startTime );
		Drive.getInstance().setWantDrivePath(path, pathContainer.isReversed());
	}
	
	@Override
	protected boolean isFinished() {
		return Drive.getInstance().isDoneWithPath();
	}
	
	@Override
	protected void end() {
		SmartDashboard.putNumber("totaltime", (startTime-System.currentTimeMillis())/1000);
		Drive.getInstance().setOpenLoop(new DriveSignal(0,0));
	}
	
	@Override
	protected void execute() {
	}

}

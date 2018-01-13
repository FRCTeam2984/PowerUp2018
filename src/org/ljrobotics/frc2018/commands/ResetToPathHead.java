package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ResetToPathHead extends InstantCommand {

	private PathContainer pathContainer;
	
	public ResetToPathHead(PathContainer pathContainer) {
		this.pathContainer = pathContainer;
		requires(Drive.getInstance());
	}
	
	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		RigidTransform2d startPose = pathContainer.getStartPose();
        RobotState.getInstance().reset(Timer.getFPGATimestamp(), startPose);
        Drive.getInstance().setGyroAngle(startPose.getRotation());

	}

}

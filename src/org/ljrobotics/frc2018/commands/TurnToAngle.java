package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.DriveSignal;
import org.ljrobotics.lib.util.control.SynchronousPIDF;
import org.ljrobotics.lib.util.drivers.LazyGyroscope;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnToAngle extends Command {
	
	private double angleToTurnTo;
	private boolean hasRun;
	
    public TurnToAngle(double angle) {
    	this.requires(Drive.getInstance());
    	this.angleToTurnTo = angle;
    	hasRun = false;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	System.out.println("init");
    	Drive.getInstance().setTurnAngle(this.angleToTurnTo);
    	hasRun = true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return Drive.getInstance().isDoneWithTurn() && hasRun;
//    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Drive.getInstance().setOpenLoop(new DriveSignal(0,0));
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	
    }
}

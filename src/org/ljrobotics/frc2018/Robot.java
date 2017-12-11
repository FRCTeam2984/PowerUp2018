
package org.ljrobotics.frc2018;

import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.frc2018.subsystems.Drive;
import org.ljrobotics.lib.util.CrashTracker;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	
	private Drive drive;
	private RobotState robotState;
	
	private Looper looper;
	
	private SubsystemManager subsystemManager;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	public Robot() {
		this.robotState = RobotState.getInstance();
		this.drive = Drive.getInstance();
		this.looper = new Looper();
		
		this.subsystemManager = new SubsystemManager(this.drive);
		
		CrashTracker.logRobotConstruction();
	}
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			CrashTracker.logRobotInit();
			
			//Initialization Code
			oi = new OI();
			
			this.subsystemManager.registerEnabledLoops(this.looper);
			
		} catch( Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		try {
			CrashTracker.logDisabledInit();
			
			this.looper.stop();
			
			this.subsystemManager.stop();
			
		} catch( Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}

	@Override
	public void disabledPeriodic() {
		this.allPeriodic();
	}

	@Override
	public void autonomousInit() {
		try {
			CrashTracker.logAutoInit();
			
			this.looper.start();
			
		} catch( Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		this.allPeriodic();
	}

	@Override
	public void teleopInit() {
		try {
			CrashTracker.logTeleopInit();
			
			this.looper.start();
			
		} catch( Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		this.allPeriodic();
	}

	@Override
	public void testInit() {
		try {
			CrashTracker.logDisabledInit();
		} catch( Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}
	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	/**
	 * A method that has code that is run in all periodic functions
	 */
	public void allPeriodic() {
		
		this.robotState.outputToSmartDashboard();
		this.subsystemManager.outputToSmartDashboard();
		this.subsystemManager.writeToLog();
		this.looper.outputToSmartDashboard();
	}
}
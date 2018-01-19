
package org.usfirst.frc.team2984;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.OI;
import org.ljrobotics.frc2018.SubsystemManager;
import org.ljrobotics.frc2018.commands.FollowPath;
import org.ljrobotics.frc2018.commands.ResetToPathHead;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.frc2018.loops.RobotStateEstimator;
import org.ljrobotics.frc2018.loops.VisionProcessor;
import org.ljrobotics.frc2018.paths.TestPath;
import org.ljrobotics.frc2018.paths.AutoLeftSwitchSide;

import org.ljrobotics.frc2018.paths.AutoRightSwitchSide;
import org.ljrobotics.frc2018.paths.LeftScale;
import org.ljrobotics.frc2018.paths.ShortRightSwitch;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.subsystems.Drive;
//import org.ljrobotics.frc2018.vision.VisionServer;
import org.ljrobotics.lib.util.CrashTracker;
import org.ljrobotics.lib.util.GameData;
import org.ljrobotics.lib.util.IncorrectGameData;
import org.ljrobotics.lib.util.PaddleSide;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
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

	// private VisionServer visionServer;

	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	public Robot() {
		this.robotState = RobotState.getInstance();
		this.drive = Drive.getInstance();
		this.looper = new Looper();
		// this.visionServer = VisionServer.getInstance();

		this.subsystemManager = new SubsystemManager(this.drive);

		CrashTracker.logRobotConstruction();
		
		new Constants().loadFromFile();
	}

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {
		try {
			CrashTracker.logRobotInit();

			// Initialization Code
			oi = OI.getInstance();

			this.subsystemManager.registerEnabledLoops(this.looper);
			// this.looper.register(VisionProcessor.getInstance());
			this.looper.register(RobotStateEstimator.getInstance());

			// this.visionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());
		} catch (Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}

		this.zeroAllSensors();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit() {
		try {
			Scheduler.getInstance().removeAll();
			CrashTracker.logDisabledInit();

			this.looper.stop();

			this.subsystemManager.stop();

		} catch (Throwable throwable) {
			CrashTracker.logThrowableCrash(throwable);
			throw throwable;
		}
	}

	@Override
	public void disabledPeriodic() {
		this.allPeriodic();
		this.zeroAllSensors();
	}

	@Override
	public void autonomousInit() {
		try {
			CrashTracker.logAutoInit();

			this.zeroAllSensors();

			this.looper.start();

			PathContainer path = new AutoLeftSwitchSide();
			GameData gd = null;

			try {
				gd = new GameData();
				if (gd.GetPaddleSide(0) == PaddleSide.LEFT) {
					path = new LeftScale();
				} else if (gd.GetPaddleSide(0) == PaddleSide.RIGHT) {
					path = new ShortRightSwitch();
				}
			} catch (IncorrectGameData e) {
				System.out.println(e.getErrorData());
			}

			CommandGroup command = new CommandGroup();
			command.addSequential(new ResetToPathHead(path));
			command.addSequential(new FollowPath(path));

			Scheduler.getInstance().add(command);

		} catch (Throwable throwable) {
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
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		try {
			CrashTracker.logTeleopInit();

			this.zeroAllSensors();

			this.looper.start();

		} catch (Throwable throwable) {
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
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		try {
			CrashTracker.logDisabledInit();
		} catch (Throwable throwable) {
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

	public void zeroAllSensors() {
		this.subsystemManager.zeroSensors();
		this.robotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());
	}

	/**
	 * A method that has code that is run in all periodic functions
	 */
	public void allPeriodic() {

		this.robotState.outputToSmartDashboard();
		this.subsystemManager.outputToSmartDashboard();
		this.subsystemManager.writeToLog();
		this.looper.outputToSmartDashboard();
		// SmartDashboard.putBoolean("cameraConnected",
		// this.visionServer.isConnected());
	}
}
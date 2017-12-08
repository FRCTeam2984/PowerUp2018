package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.RobotMap;
import org.ljrobotics.frc2018.RobotState;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.control.Lookahead;
import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathFollower;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;
import org.ljrobotics.lib.util.drivers.LazyCANTalon;

import com.ctre.CANTalon;
import com.ctre.CANTalon.StatusFrameRate;

/**
 * The Drive subsystem. This subsystem is responsible for everything regarding
 * driving. It controls the four drive motors.
 * 
 * @author Max
 *
 */
public class Drive extends Subsystem {

	private static Drive instance;

	public static Drive getInstance() {
		if (instance == null) {
			CANTalon frontLeft = new LazyCANTalon(RobotMap.FRONT_LEFT_MOTOR_ID);
			CANTalon frontRight = new LazyCANTalon(RobotMap.FRONT_RIGHT_MOTOR_ID);
			CANTalon rearLeft = new LazyCANTalon(RobotMap.REAR_LEFT_MOTOR_ID);
			CANTalon rearRight = new LazyCANTalon(RobotMap.REAR_RIGHT_MOTOR_ID);

			instance = new Drive(frontLeft, frontRight, rearLeft, rearRight);
		}
		return instance;
	}

	// The robot drivetrain's various states.
	public enum DriveControlState {
		VELOCITY_SETPOINT, // Under PID velocity control
		PATH_FOLLOWING // Following a path
	}

	public static final int VELOCITY_CONTROL_SLOT = 0;

	// Talons
	private CANTalon leftMaster;
	private CANTalon rightMaster;
	private CANTalon leftSlave;
	private CANTalon rightSlave;

	// Control States
	private DriveControlState driveControlState;

	// Controllers
	private PathFollower pathFollower;

	// Hardware States
	private boolean isBrakeMode;

	private Path currentPath;

	/**
	 * Creates a new Drive Subsystem from that controls the given motor controllers.
	 * 
	 * @param frontLeft
	 *            the font left talon motor controller
	 * @param frontRight
	 *            the font right talon motor controller
	 * @param backLeft
	 *            the back left talon motor controller
	 * @param backRight
	 *            the back right talon motor controller
	 */
	public Drive(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight) {

		this.leftMaster = frontLeft;
		this.rightMaster = frontRight;
		this.leftSlave = backLeft;
		this.rightSlave = backRight;

		CANTalonFactory.updateCANTalonToDefault(this.leftMaster);
		leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);

		CANTalonFactory.updatePermanentSlaveTalon(this.leftSlave, this.leftMaster.getDeviceID());
		leftSlave.reverseOutput(false);
		leftMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);

		CANTalonFactory.updateCANTalonToDefault(this.rightMaster);
		rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);

		CANTalonFactory.updatePermanentSlaveTalon(this.rightSlave, this.rightMaster.getDeviceID());
		rightSlave.reverseOutput(false);
		rightMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);

		this.driveControlState = DriveControlState.VELOCITY_SETPOINT;

		this.isBrakeMode = false;
		this.setBrakeMode(true);
	}

	@Override
	public void stop() {
		this.leftMaster.set(0);
		this.rightMaster.set(0);
	}

	@Override
	public void reset() {

	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {

	}

	/**
	 * Start up velocity mode. This sets the drive train in high gear as well.
	 * 
	 * @param left_inches_per_sec
	 * @param right_inches_per_sec
	 */
	public synchronized void setVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
		configureTalonsForSpeedControl();
		driveControlState = DriveControlState.VELOCITY_SETPOINT;
		updateVelocitySetpoint(left_inches_per_sec, right_inches_per_sec);
	}

	/**
	 * Adjust Velocity setpoint (if already in velocity mode)
	 * 
	 * @param left_inches_per_sec
	 * @param right_inches_per_sec
	 */
	private synchronized void updateVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
		if (usesTalonVelocityControl(driveControlState)) {
			final double max_desired = Math.max(Math.abs(left_inches_per_sec), Math.abs(right_inches_per_sec));
			final double scale = max_desired > Constants.kDriveHighGearMaxSetpoint
					? Constants.kDriveHighGearMaxSetpoint / max_desired
					: 1.0;
			leftMaster.set(inchesPerSecondToRpm(left_inches_per_sec * scale));
			rightMaster.set(inchesPerSecondToRpm(right_inches_per_sec * scale));
		} else {
			System.out.println("Hit a bad velocity control state");
			leftMaster.set(0);
			rightMaster.set(0);
		}
	}

	private static double inchesToRotations(double inches) {
        return inches / (Constants.kDriveWheelDiameterInches * Math.PI);
    }
	
	private static double inchesPerSecondToRpm(double inches_per_second) {
		return inchesToRotations(inches_per_second) * 60;
	}

	/**
	 * Configures the drivebase to drive a path. Used for autonomous driving
	 * 
	 * @see Path
	 */
	public synchronized void setWantDrivePath(Path path, boolean reversed) {
		if (currentPath != path || driveControlState != DriveControlState.PATH_FOLLOWING) {
			configureTalonsForSpeedControl();
			RobotState.getInstance().resetDistanceDriven();
			pathFollower = new PathFollower(path, reversed, new PathFollower.Parameters(
					new Lookahead(Constants.kMinLookAhead, Constants.kMaxLookAhead, Constants.kMinLookAheadSpeed,
							Constants.kMaxLookAheadSpeed),
					Constants.kInertiaSteeringGain, Constants.kPathFollowingProfileKp,
					Constants.kPathFollowingProfileKi, Constants.kPathFollowingProfileKv,
					Constants.kPathFollowingProfileKffv, Constants.kPathFollowingProfileKffa,
					Constants.kPathFollowingMaxVel, Constants.kPathFollowingMaxAccel,
					Constants.kPathFollowingGoalPosTolerance, Constants.kPathFollowingGoalVelTolerance,
					Constants.kPathStopSteeringDistance));
			driveControlState = DriveControlState.PATH_FOLLOWING;
			currentPath = path;
		} else {
			setVelocitySetpoint(0, 0);
		}
	}

	/**
	 * Configures talons for velocity control
	 */
	private void configureTalonsForSpeedControl() {
		if (!usesTalonVelocityControl(driveControlState)) {
			// We entered a velocity control state.
			leftMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
			leftMaster.setNominalClosedLoopVoltage(12.0);
			leftMaster.setProfile(VELOCITY_CONTROL_SLOT);
			leftMaster.configNominalOutputVoltage(Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT,
					-Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT);
			rightMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
			rightMaster.setNominalClosedLoopVoltage(12.0);
			rightMaster.setProfile(VELOCITY_CONTROL_SLOT);
			rightMaster.configNominalOutputVoltage(Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT,
					-Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT);
			setBrakeMode(true);
		}
	}

	/**
	 * Check if the drive talons are configured for velocity control
	 */
	protected static boolean usesTalonVelocityControl(DriveControlState state) {
		if (state == DriveControlState.VELOCITY_SETPOINT || state == DriveControlState.PATH_FOLLOWING) {
			return true;
		}
		return false;
	}

	public synchronized void setBrakeMode(boolean on) {
		if (isBrakeMode != on) {
			isBrakeMode = on;
			rightMaster.enableBrakeMode(on);
			rightSlave.enableBrakeMode(on);
			leftMaster.enableBrakeMode(on);
			leftSlave.enableBrakeMode(on);
		}
	}

}

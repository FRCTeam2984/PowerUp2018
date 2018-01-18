package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.JoystickDrive;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.frc2018.state.Kinematics;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.lib.util.DriveSignal;
import org.ljrobotics.lib.util.control.Lookahead;
import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathFollower;
import org.ljrobotics.lib.util.control.SynchronousPIDF;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;
import org.ljrobotics.lib.util.drivers.LazyCANTalon;
import org.ljrobotics.lib.util.drivers.LazyGyroscope;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Twist2d;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Drive subsystem. This subsystem is responsible for everything regarding
 * driving. It controls the four drive motors.
 *
 * @author Max
 *
 */
public class Drive extends Subsystem implements LoopingSubsystem {

	private static Drive instance;

	public static Drive getInstance() {
		if (instance == null) {
			TalonSRX frontLeft = new LazyCANTalon(Constants.FRONT_LEFT_MOTOR_ID);
			TalonSRX frontRight = new LazyCANTalon(Constants.FRONT_RIGHT_MOTOR_ID);
			TalonSRX rearLeft = new LazyCANTalon(Constants.REAR_LEFT_MOTOR_ID);
			TalonSRX rearRight = new LazyCANTalon(Constants.REAR_RIGHT_MOTOR_ID);

			RobotState robotState = RobotState.getInstance();
			LazyGyroscope gyro = LazyGyroscope.getInstance();

			instance = new Drive(frontLeft, frontRight, rearLeft, rearRight, robotState, gyro);
		}
		return instance;
	}

	// The robot drivetrain's various states.
	public enum DriveControlState {
		VELOCITY_SETPOINT, // Under PID velocity control
		PATH_FOLLOWING, // Following a path
		OPEN_LOOP // Used to drive control
	}

	public static final int VELOCITY_CONTROL_SLOT = 0;
	
	//Sensors
	private Gyro gyro;
	
	private Rotation2d gyroZero;

	// The drive loop definition
	private class DriveLoop implements Loop {

		public void onStart( double timestamp ) {

		}

		public void onLoop( double timestamp ) {
			switch( driveControlState  ) {
			case VELOCITY_SETPOINT:
				break;
			case PATH_FOLLOWING:
				//TODO add a write to CVS file function
				updatePathFollower( timestamp );
				updateTalonOutputs( timestamp );
				break;
			default:

			}
		}
		
		public void onStop( double timestamp ) {

		}

	}
	// The local drive loop
	private DriveLoop driveLoop = new DriveLoop();

	// Talons
	private TalonSRX leftMaster;
	private TalonSRX rightMaster;
	private TalonSRX leftSlave;
	private TalonSRX rightSlave;

	// Control States
	private DriveControlState driveControlState;
	private double lastTimeStamp;

	// Controllers
	private PathFollower pathFollower;
	private RobotState robotState;
	
	private SynchronousPIDF leftPID;
	private SynchronousPIDF rightPID;

	// Hardware States
	private NeutralMode isBrakeMode;

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
	public Drive(TalonSRX frontLeft, TalonSRX frontRight, TalonSRX backLeft, TalonSRX backRight, RobotState robotState,
			Gyro gyro) {

		this.robotState = robotState;
		this.gyro = gyro;

		this.leftMaster = frontLeft;
		this.rightMaster = frontRight;
		this.leftSlave = backLeft;
		this.rightSlave = backRight;

		CANTalonFactory.updateCANTalonToDefault(this.leftMaster);

		CANTalonFactory.updatePermanentSlaveTalon(this.leftSlave, this.leftMaster.getDeviceID());
		leftMaster.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5);

		CANTalonFactory.updateCANTalonToDefault(this.rightMaster);

		CANTalonFactory.updatePermanentSlaveTalon(this.rightSlave, this.rightMaster.getDeviceID());
		rightMaster.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5);
		rightMaster.setInverted(true);
		
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		leftPID = new SynchronousPIDF(Constants.DRIVE_Kp, Constants.DRIVE_Ki, Constants.DRIVE_Kd,
				Constants.DRIVE_Kf);
		
		rightPID = new SynchronousPIDF(Constants.DRIVE_Kp, Constants.DRIVE_Ki, Constants.DRIVE_Kd,
				Constants.DRIVE_Kf);
		
//		leftPID.setOutputRange(-0.5, 0.5);
//		rightPID.setOutputRange(-0.5, 0.5);
		
		this.driveControlState = DriveControlState.OPEN_LOOP;

		this.isBrakeMode = NeutralMode.Coast;
		this.setNeutralMode(NeutralMode.Brake);
		
		this.gyroZero = Rotation2d.fromDegrees(0);
	}
	
	@Override
	public void stop() {
		this.leftMaster.set(ControlMode.PercentOutput, 0);
		this.rightMaster.set(ControlMode.PercentOutput, 0);
	}

	@Override
	public void reset() {

	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(driveLoop);
	}

	public void setOpenLoop(DriveSignal driveSignal) {
		if (this.driveControlState != DriveControlState.OPEN_LOOP) {
            this.leftMaster.configNominalOutputForward(0, 0);
            this.rightMaster.configNominalOutputForward(0, 0);

            this.leftMaster.configNominalOutputReverse(0, 0);
            this.rightMaster.configNominalOutputReverse(0, 0);
            this.driveControlState = DriveControlState.OPEN_LOOP;
            setNeutralMode(NeutralMode.Coast);
        }
		double left = driveSignal.getLeft();
		double right = driveSignal.getRight();
		left = Math.min(Math.max(left, -1), 1);
		right = Math.min(Math.max(right, -1), 1);
		this.leftMaster.set(ControlMode.PercentOutput, left);
		this.rightMaster.set(ControlMode.PercentOutput, right);
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
	
	public synchronized void updateTalonOutputs(double timestamp) {
		double dt = timestamp - this.lastTimeStamp;
		this.lastTimeStamp = timestamp;
		
		double leftVel = this.getLeftVelocityInchesPerSec();
		double rightVel = this.getRightVelocityInchesPerSec();
		double left = this.leftPID.calculate(leftVel, dt);
		double right = this.rightPID.calculate(rightVel, dt);
		
		SmartDashboard.putNumber("left output", left);
		SmartDashboard.putNumber("right output", right);
		
		this.leftMaster.set(ControlMode.PercentOutput, left);
		this.rightMaster.set(ControlMode.PercentOutput, right);
	}

	/**
	 * Called periodically when the robot is in path following mode. Updates the
	 * path follower with the robots latest pose, distance driven, and velocity, the
	 * updates the wheel velocity setpoints.
	 */
	public void updatePathFollower(double timestamp) {
		RigidTransform2d robot_pose = robotState.getLatestFieldToVehicle().getValue();
		Twist2d command = pathFollower.update(timestamp, robot_pose, robotState.getDistanceDriven(),
				robotState.getPredictedVelocity().dx);
		if (!pathFollower.isFinished()) {
//			command = new Twist2d(command.dx, command.dy, command.dtheta*1.5);
			System.out.println(command);
			SmartDashboard.putNumber("Turn Command", command.dtheta);
			SmartDashboard.putNumber("Move Command", command.dx);
			Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
			updateVelocitySetpoint(setpoint.left, setpoint.right);
		} else {
			updateVelocitySetpoint(0, 0);
		}
	}

	/**
	 * Adjust Velocity setpoint (if already in velocity mode)
	 *
	 * @param left_inches_per_sec
	 * @param right_inches_per_sec
	 */
	private synchronized void updateVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
		if (usesTalonVelocityControl(driveControlState)) {
			double max_desired = Math.max(Math.abs(left_inches_per_sec), Math.abs(right_inches_per_sec));
			double scale = max_desired > Constants.DRIVE_MAX_SETPOINT ? Constants.DRIVE_MAX_SETPOINT / max_desired
					: 1.0;
			SmartDashboard.putNumber("Left Wanted Vel", left_inches_per_sec * scale);
			SmartDashboard.putNumber("Right Wanted Vel", right_inches_per_sec * scale);
//			System.out.println(left_inches_per_sec + ", " + right_inches_per_sec + " : " + scale);
			leftPID.setSetpoint(left_inches_per_sec * scale);
			rightPID.setSetpoint(right_inches_per_sec * scale);
		} else {
			System.out.println("Hit a bad velocity control state");
			leftPID.setSetpoint(0);
			rightPID.setSetpoint(0);
		}
	}

	public synchronized boolean isDoneWithPath() {
		if (driveControlState == DriveControlState.PATH_FOLLOWING && pathFollower != null) {
			return pathFollower.isFinished();
		} else {
			System.out.println("Robot is not in path following mode");
			return true;
		}
	}

	/**
	 * Configures the drivebase to drive a path. Used for autonomous driving
	 *
	 * @see Path
	 */
	public synchronized void setWantDrivePath(Path path, boolean reversed) {
		if (currentPath != path || driveControlState != DriveControlState.PATH_FOLLOWING) {
			configureTalonsForSpeedControl();
			robotState.resetDistanceDriven();
			pathFollower = new PathFollower(path, reversed, new PathFollower.Parameters(
					new Lookahead(Constants.MIN_LOOK_AHEAD, Constants.MAX_LOOK_AHEAD, Constants.MIN_LOOK_AHEAD_SPEED,
							Constants.MAX_LOOK_AHEAD_SPEED),
					Constants.INERTIA_STEERING_GAIN, Constants.PATH_FOLLOWING_PROFILE_Kp,
					Constants.PATH_FOLLOWING_PROFILE_Ki, Constants.PATH_FOLLOWING_PROFILE_Kv,
					Constants.PATH_FOLLOWING_PROFILE_Kffv, Constants.PATH_FOLLOWING_PROFILE_Kffa,
					Constants.PATH_FOLLOWING_MAX_VEL, Constants.PATH_FOLLOWING_MAX_ACCEL,
					Constants.PATH_FOLLOWING_GOAL_POS_TOLERANCE, Constants.PATH_FOLLOWING_GOAL_VEL_TOLERANCE,
					Constants.PATH_STOP_STEERING_DISTANCE));
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
//			leftMaster.configNominalOutputForward(1, 0);
//			leftMaster.configNominalOutputReverse(-1, 0);
//			leftMaster.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
//			rightMaster.configNominalOutputForward(1, 0);
//			rightMaster.configNominalOutputReverse(-1, 0);
//			rightMaster.selectProfileSlot(VELOCITY_CONTROL_SLOT, 0);
			setNeutralMode(NeutralMode.Brake);
		}
	}

	/**
	 * Check if the drive talons are configured for velocity control
	 */
	protected static boolean usesTalonVelocityControl(DriveControlState state) {
		return state == DriveControlState.VELOCITY_SETPOINT || state == DriveControlState.PATH_FOLLOWING;
	}

	/**
	 * Sets the driveTrain into either break or coast mode.
	 *
	 * @param neutralMode
	 *            true if break mode false if coast mode.
	 */
	public synchronized void setNeutralMode(NeutralMode neutralMode) {
		if (isBrakeMode != neutralMode) {
			isBrakeMode = neutralMode;
			rightMaster.setNeutralMode(neutralMode);
			rightSlave.setNeutralMode(neutralMode);
			leftMaster.setNeutralMode(neutralMode);
			leftSlave.setNeutralMode(neutralMode);
		}
	}

	public double encoderTicksToInchesRight(double ticksPerSecond) {
		double rotationsPerSecond = ticksPerSecond / Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT;
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		return rotationsPerSecond * wheelCircumference;
	}
	
	public double encoderTicksToInchesLeft(double ticksPerSecond) {
		double rotationsPerSecond = ticksPerSecond / Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT;
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		return rotationsPerSecond * wheelCircumference;
	}
	
	public double inchesToEncoderTicksRight(double inchesPerSecond) {
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		double rotationsPerSecond = inchesPerSecond / wheelCircumference;
		return rotationsPerSecond * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_RIGHT;
	}
	
	public double inchesToEncoderTicksLeft(double inchesPerSecond) {
		double wheelCircumference = Constants.DRIVE_WHEEL_DIAMETER_INCHES * Math.PI;
		double rotationsPerSecond = inchesPerSecond / wheelCircumference;
		return rotationsPerSecond * Constants.DRIVE_ENCODER_TICKS_PER_ROTATION_LEFT;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Left Velocity", this.getLeftVelocityInchesPerSec());
		SmartDashboard.putNumber("Right Velocity", this.getRightVelocityInchesPerSec());

		SmartDashboard.putNumber("Left Position", this.getLeftDistanceInches());
		SmartDashboard.putNumber("Right Position", this.getRightDistanceInches());

		SmartDashboard.putNumber("Gyro Angle", this.getGyroAngle().getDegrees());
	}

	@Override
	public void writeToLog() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zeroSensors() {
		this.leftMaster.setSelectedSensorPosition(0, 0, 0);
		this.rightMaster.setSelectedSensorPosition(0, 0, 0);
		this.gyro.calibrate();
	}

	@Override
	protected void initDefaultCommand() {
		this.setDefaultCommand(new JoystickDrive());
	}

	public Rotation2d getGyroAngle() {
		double gyroAngle = Constants.GYRO_MODIFIER*this.gyro.getAngle();
		return Rotation2d.fromDegrees(gyroAngle).rotateBy(this.gyroZero.inverse());
	}

	public double getLeftVelocityInchesPerSec() {
		return encoderTicksToInchesLeft(this.leftMaster.getSelectedSensorVelocity(0) * 10);
	}

	public double getRightVelocityInchesPerSec() {
		return encoderTicksToInchesRight(this.rightMaster.getSelectedSensorVelocity(0) * 10);
	}

	public double getLeftDistanceInches() {
		return encoderTicksToInchesLeft(this.leftMaster.getSelectedSensorPosition(0));
	}

	public double getRightDistanceInches() {
		return encoderTicksToInchesRight(this.rightMaster.getSelectedSensorPosition(0));
	}

	public void setGyroAngle(Rotation2d rotation) {
		this.gyroZero = rotation;
	}
}

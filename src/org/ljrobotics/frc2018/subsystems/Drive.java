package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.CheesyJoystickDrive;
import org.ljrobotics.frc2018.commands.TankJoystickDrive;
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
import org.ljrobotics.lib.util.Conversions;

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
			TalonSRX slaveLeft = new LazyCANTalon(Constants.SLAVE_LEFT_MOTOR_ID);
			TalonSRX slaveRight = new LazyCANTalon(Constants.SLAVE_RIGHT_MOTOR_ID);
			TalonSRX masterLeft = new LazyCANTalon(Constants.MASTER_LEFT_MOTOR_ID);
			TalonSRX masterRight = new LazyCANTalon(Constants.MASTER_RIGHT_MOTOR_ID);

			RobotState robotState = RobotState.getInstance();
			LazyGyroscope gyro = LazyGyroscope.getInstance();

			instance = new Drive(masterLeft, masterRight, slaveLeft, slaveRight, robotState, gyro);
		}
		return instance;
	}

	// The robot drivetrain's various states.
	public enum DriveControlState {
		VELOCITY_SETPOINT, // Under PID velocity control
		PATH_FOLLOWING, // Following a path
		TURNING, //turnToAngle
		OPEN_LOOP// Used to drive control
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
				updateTalonOutputs( timestamp );
				break;
			case PATH_FOLLOWING:
				//TODO add a write to CVS file function
				updatePathFollower( timestamp );
				updateTalonOutputs( timestamp );
				break;
			case TURNING:
				updateTurn(timestamp);
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
	private double speedLimit;

	// Controllers
	private PathFollower pathFollower;
	private RobotState robotState;
	
	private SynchronousPIDF leftPID;
	private SynchronousPIDF rightPID;
	private SynchronousPIDF speedPID;
	
	private boolean hasUpdatedPID;

	// Hardware States
	private NeutralMode isBrakeMode;

	private Path currentPath;
	
	private long overCurrentTime;
	private boolean isOverCurrent;
	
	/**
	 * Creates a new Drive Subsystem from that controls the given motor controllers.
	 *
	 * @param masterLeft
	 *            the font left talon motor controller
	 * @param masterRight
	 *            the font right talon motor controller
	 * @param slaveLeft
	 *            the back left talon motor controller
	 * @param slaveRight
	 *            the back right talon motor controller
	 */
	public Drive(TalonSRX masterLeft, TalonSRX masterRight, TalonSRX slaveLeft, TalonSRX slaveRight, RobotState robotState,
			Gyro gyro) {

		this.robotState = robotState;
		this.gyro = gyro;

		this.leftMaster = masterLeft;
		this.rightMaster = masterRight;
		this.leftSlave = slaveLeft;
		this.rightSlave = slaveRight;

		CANTalonFactory.updateCANTalonToDefault(this.leftMaster);

		CANTalonFactory.updatePermanentSlaveTalon(this.leftSlave, this.leftMaster.getDeviceID());
		leftMaster.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5);

		CANTalonFactory.updateCANTalonToDefault(this.rightMaster);

		CANTalonFactory.updatePermanentSlaveTalon(this.rightSlave, this.rightMaster.getDeviceID());
		rightMaster.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5);
		rightSlave.setInverted(true);
		leftMaster.setInverted(true);
		rightMaster.setInverted(true);
		
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		
		leftMaster.configOpenloopRamp(0.375, 0);
		rightMaster.configOpenloopRamp(0.375, 0);
		
		leftPID = new SynchronousPIDF(Constants.DRIVE_Kp, Constants.DRIVE_Ki, Constants.DRIVE_Kd,
				Constants.DRIVE_Kf);
		
		rightPID = new SynchronousPIDF(Constants.DRIVE_Kp, Constants.DRIVE_Ki, Constants.DRIVE_Kd,
				Constants.DRIVE_Kf);
		
//		leftPID.setOutputRange(-0.5, 0.5);
//		rightPID.setOutputRange(-0.5, 0.5);
		
		speedPID = new SynchronousPIDF(Constants.TURN_Kp, Constants.TURN_Ki, 
    			Constants.TURN_Kd, Constants.TURN_Kf);
		speedPID.setContinuous();
		speedPID.setInputRange(-180D, 180D);
		//TODO Add constant for output range
		speedPID.setOutputRange(-Constants.TURN_SPEED, Constants.TURN_SPEED);
		
		this.driveControlState = DriveControlState.OPEN_LOOP;

		this.isBrakeMode = NeutralMode.Coast;
		this.setNeutralMode(NeutralMode.Brake);
		
		this.gyroZero = Rotation2d.fromDegrees(0);
		this.speedLimit = 1;
		
		this.overCurrentTime = System.currentTimeMillis();
		this.isOverCurrent = false;
	}
	
	public boolean isOverCurrent() {
		if(this.isOverCurrent) {
			double leftCurrent = this.leftMaster.getOutputCurrent();
			double rightCurrent = this.leftMaster.getOutputCurrent();
			if(Math.max(leftCurrent, rightCurrent) > Constants.DRIVE_MAX_CURRENT) {
				if(System.currentTimeMillis() - this.overCurrentTime > Constants.DRIVE_MAX_CURRENT_TIME) {
					return true;
				}
			} else {
				this.isOverCurrent = false;
			}
		} else {
			double leftCurrent = this.leftMaster.getOutputCurrent();
			double rightCurrent = this.leftMaster.getOutputCurrent();
			if(Math.max(leftCurrent, rightCurrent) > Constants.DRIVE_MAX_CURRENT) {
				this.isOverCurrent = true;
				this.overCurrentTime = System.currentTimeMillis();
			}
		}
		return false;
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
		this.leftMaster.set(ControlMode.PercentOutput, left * this.speedLimit);
		this.rightMaster.set(ControlMode.PercentOutput, right * this.speedLimit);
	}
	
	public void setSpeedLimit(double limit) {
		this.speedLimit = limit;
	}

	public synchronized void setTurnAngle(double angle) {
		this.driveControlState = DriveControlState.TURNING;
		
		speedPID.reset();
		speedPID.setSetpoint(angle);
		this.hasUpdatedPID = false;
	}
	
	public SynchronousPIDF getSpeedPID() {
		return speedPID;
	}
	
	private void updateTurn(double timestamp) {
		double dt = timestamp - this.lastTimeStamp;
		this.lastTimeStamp = timestamp;
		double currentAngle = LazyGyroscope.getInstance().getAngle();
		double speed = speedPID.calculate(currentAngle, dt);
		
		leftMaster.set(ControlMode.PercentOutput, speed);
		rightMaster.set(ControlMode.PercentOutput, -speed);
		this.hasUpdatedPID = true;
		// setVelocitySetpoint(-speed, speed);
		
	}
	
	public boolean isDoneWithTurn() {
		// boolean toReturn = Math.abs(speedPID.getError()) <= Constants.TURN_DEGREE_TOLERANCE && LazyGyroscope.getInstance().getRate()<=Constants.LOW_VELOCITY_THRESHOLD;
		boolean lessThanSpeed = Math.abs(speedPID.getError()) <= Constants.TURN_DEGREE_TOLERANCE ;
//		boolean lessThanAngle = Math.abs(speedPID.getSetpoint() - this.getGyroAngle().getDegrees()) <= Constants.TURN_DEGREE_TOLERANCE;
		boolean toReturn = lessThanSpeed && this.hasUpdatedPID;
	    System.out.println("Turning with error " + speedPID.getError());
		return (toReturn);
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
		try {
			RigidTransform2d robot_pose = robotState.getLatestFieldToVehicle().getValue();
			Twist2d command = pathFollower.update(timestamp, robot_pose, robotState.getDistanceDriven(),
					robotState.getPredictedVelocity().dx);
			if (!pathFollower.isFinished()) {
//				command = new Twist2d(command.dx, command.dy, command.dtheta*1.5);
//				System.out.println(command);
				SmartDashboard.putNumber("Turn Command", command.dtheta);
				SmartDashboard.putNumber("Move Command", command.dx);
				Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
				updateVelocitySetpoint(setpoint.left, setpoint.right);
			} else {
				updateVelocitySetpoint(0, 0);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
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
		if(isOverCurrent()) {
			return true;
		}
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

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Left Velocity", this.getLeftVelocityInchesPerSec());
		SmartDashboard.putNumber("Right Velocity", this.getRightVelocityInchesPerSec());

		SmartDashboard.putNumber("Left Position", this.getLeftDistanceInches());
		SmartDashboard.putNumber("Right Position", this.getRightDistanceInches());
		
		SmartDashboard.putNumber("Current", this.leftMaster.getOutputCurrent());

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
		this.gyro.reset();
	}

	@Override
	protected void initDefaultCommand() {
		if(Constants.USE_TANK_DRIVE) {
			this.setDefaultCommand(new TankJoystickDrive());
		} else {
			this.setDefaultCommand(new CheesyJoystickDrive());
		}
	}

	public Rotation2d getGyroAngle() {
		double gyroAngle = Constants.GYRO_MODIFIER*this.gyro.getAngle();
		return Rotation2d.fromDegrees(gyroAngle).rotateBy(this.gyroZero.inverse());
	}

	public double getLeftVelocityInchesPerSec() {
		return Conversions.encoderTicksToInchesLeft(this.leftMaster.getSelectedSensorVelocity(0) * 10);
	}

	public double getRightVelocityInchesPerSec() {
		return Conversions.encoderTicksToInchesRight(this.rightMaster.getSelectedSensorVelocity(0) * 10);
	}

	public double getLeftDistanceInches() {
		return Conversions.encoderTicksToInchesLeft(this.leftMaster.getSelectedSensorPosition(0));
	}

	public double getRightDistanceInches() {
		return Conversions.encoderTicksToInchesRight(this.rightMaster.getSelectedSensorPosition(0));
	}

	public void setGyroAngle(Rotation2d rotation) {
		this.gyroZero = rotation;
	}
}

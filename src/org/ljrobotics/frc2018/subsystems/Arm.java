package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.ArmIdle;
import org.ljrobotics.frc2018.commands.ArmSetpoint;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.control.SynchronousPIDF;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Arm subsystem. This subsystem is responsible for everything regarding
 * arm. It controls the two arm motors.
 *
 * @author Grant
 *
 */
public class Arm extends Subsystem implements LoopingSubsystem {

	private static Arm instance;

	public static Arm getInstance() {
		if (instance == null) {
			// TalonSRX slave = new TalonSRX(Constants.SLAVE_ARM_MOTOR_ID);
			TalonSRX slave = null;
			TalonSRX master = new TalonSRX(Constants.MASTER_ARM_MOTOR_ID);

			instance = new Arm(slave, master);
		}
		return instance;
	}

	private TalonSRX slave;
	private TalonSRX master;

	private SynchronousPIDF armPID;

	private ArmControlState controlState;
	private ArmPosition wantedPosition;
	private double wantedSpeed;

	private double lastTimeStamp;

	public static enum ArmControlState {
		Moving, // Move at joystick speed
		Angle, // Under PID angle control
		Position, // Set arm to position (see enum below)
		Idle // Do Nothing
	}

	public static enum ArmPosition {
		STOWED, // Move to stowed height
		INTAKE, // Move to intake height
		SWITCH, // Move to switch height
		SCALE // Move to scale height
	}

	private class IntakeLoop implements Loop {

		@Override
		public void onStart(double timestamp) {

		}

		@Override
		public void onLoop(double timestamp) {

			switch (controlState) {
			case Moving:
				setRestrictedSpeed(wantedSpeed);
				break;
			case Angle:
				updateTalonOutputs(timestamp);
				break;
			case Position:
				setPosition(wantedPosition);
				break;
			case Idle:
				setRestrictedSpeed(0);
			default:
				break;
			}
		}

		@Override
		public void onStop(double timestamp) {
		}

	}

	public Arm(TalonSRX slave, TalonSRX master) {
		this.armPID = new SynchronousPIDF(Constants.ARM_Kp, Constants.ARM_Ki, Constants.ARM_Kd, Constants.ARM_Kf);
		this.slave = slave;
		this.master = master;

		CANTalonFactory.updateCANTalonToDefault(this.master);

		// CANTalonFactory.updatePermanentSlaveTalon(this.slave,
		// this.master.getDeviceID());

		setCurrentLimit(master, Constants.MAX_ARM_CURRENT, Constants.NOMINAL_ARM_CURRENT,
				Constants.MAX_ARM_CURRENT_TIME);

		// this.slave.setInverted(true);
		this.master.setInverted(false);

		this.master.configOpenloopRamp(0.125, 0);

		this.controlState = ArmControlState.Idle;

		this.master.setSelectedSensorPosition(0, 0, 0);

		this.wantedSpeed = 0;
	}

	private void setCurrentLimit(TalonSRX talon, int max, int nominal, int time) {
		talon.configPeakCurrentDuration(time, 0);
		talon.enableCurrentLimit(true);
		talon.configContinuousCurrentLimit(nominal, 0);
		talon.configPeakCurrentLimit(max, 0);
	}

	@Override
	public void stop() {
		this.setRestrictedSpeed(0);
	}

	@Override
	public void reset() {

	}

	private void setRestrictedSpeed(double speed) {
		updateEncoder();
		this.master.set(ControlMode.PercentOutput, speed);
		SmartDashboard.putNumber("Arm Output", speed);
	}

	private void setPosition(ArmPosition position) {
		switch (position) {
		case STOWED:
			this.setAngleSetpoint(Constants.ARM_STOWED_DEGREES);
			break;
		case INTAKE:
			this.setAngleSetpoint(Constants.ARM_INTAKE_DEGREES);
			break;
		case SWITCH:
			this.setAngleSetpoint(Constants.ARM_SWITCH_DEGREES);
			break;
		case SCALE:
			this.setAngleSetpoint(Constants.ARM_STOWED_DEGREES);
			break;
		default:
			break;
		}
	}

	public void setWantedSpeed(double power) {
		this.wantedSpeed = power;
		this.controlState = ArmControlState.Moving;
	}

	public void setWantedPosition(ArmPosition position) {
		this.wantedPosition = position;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Arm PID Error", this.armPID.getError());
		SmartDashboard.putNumber("Arm Motor Encoder Master", this.getArmDegrees());
		SmartDashboard.putNumber("Arm Motor Current Master", this.master.getOutputCurrent());
	}

	@Override
	public void writeToLog() {

	}

	@Override
	public void zeroSensors() {
		this.master.setSelectedSensorPosition(0, 0, 0);
	}

	private void updateEncoder() {
		int minTicks = degreesToEncoderTicks(Constants.MIN_ARM_ENCODER_DEGREES);
		int maxTicks = degreesToEncoderTicks(Constants.MAX_ARM_ENCODER_DEGREES);
		if (this.master.getSensorCollection().isFwdLimitSwitchClosed()) {
			this.master.setSelectedSensorPosition(minTicks, 0, 0);
		} else if (this.master.getSensorCollection().isRevLimitSwitchClosed()) {
			this.master.setSelectedSensorPosition(maxTicks, 0, 0);
		}

	}

	public void setWantedState(ArmControlState state) {
		this.controlState = state;
	}

	public synchronized void setAngleSetpoint(double degrees) {
		setWantedState(ArmControlState.Angle);
		this.updateAngleSetpoint(degrees);
	}

	private double encoderTicksToDegrees(int ticks) {

		return ((double) ticks / Constants.ARM_TICKS_PER_REVOLUTION) * Constants.ARM_GEAR_RATIO * 360D;
	}

	private int degreesToEncoderTicks(double degrees) {
		return (int) ((degrees * Constants.ARM_TICKS_PER_REVOLUTION) / (Constants.ARM_GEAR_RATIO * 360D));
	}

	private synchronized void updateAngleSetpoint(double degrees) {
		SmartDashboard.putNumber("Arm Wanted Degrees", degrees);
		armPID.setSetpoint(degrees);
	}

	public double getArmDegrees() {
		return encoderTicksToDegrees(this.master.getSelectedSensorPosition(0));
	}

	public double getArmSlaveDegrees() {
		return encoderTicksToDegrees(this.slave.getSelectedSensorPosition(0));
	}

	public synchronized void updateTalonOutputs(double timestamp) {
		double dt = timestamp - this.lastTimeStamp;
		this.lastTimeStamp = timestamp;
		double degrees = this.getArmDegrees();
		double power = this.armPID.calculate(degrees, dt);
		this.setRestrictedSpeed(power);
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(new IntakeLoop());
	}

	@Override
	protected void initDefaultCommand() {
		// this.setDefaultCommand(new ArmIdle());
	}

}

package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.ArmJoystick;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.control.SynchronousPIDF;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
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
			TalonSRX slave = new TalonSRX(Constants.SLAVE_ARM_MOTOR_ID);
			TalonSRX master = new TalonSRX(Constants.MASTER_ARM_MOTOR_ID);

			instance = new Arm(slave, master);
		}
		return instance;
	}

	private TalonSRX slave;
	private TalonSRX master;

	private SynchronousPIDF armPID;

	private ArmControlState controlState;
	private double wantedSpeed;

	private double lastTimeStamp;

	private int fakeEncoderValue;
	
	public static enum ArmControlState {
		Moving, // Move at joystick speed
		Position, // Under PID control
		Idle // Do Nothing
	}

	public static enum ArmPosition {
		STOWED(Constants.ARM_STOWED_DEGREES), // Move to stowed height
		INTAKE(Constants.ARM_INTAKE_DEGREES), // Move to intake height
		SWITCH(Constants.ARM_SWITCH_DEGREES), // Move to switch height
		SCALE(Constants.ARM_SCALE_DEGREES); // Move to scale height
		
		private double angle;
		
		private ArmPosition(double angle) {
			this.angle = angle;
		}
		
		public double getAngle() {
			return angle;
		}
	}

	private class ArmLoop implements Loop {

		@Override
		public void onStart(double timestamp) {

		}

		@Override
		public void onLoop(double timestamp) {

			switch (controlState) {
			case Moving:
				setRestrictedSpeed(wantedSpeed);
				break;
			case Position:
				updateTalonOutputs(timestamp);
				break;
			case Idle:
				setRestrictedSpeed(0);
			default:
				break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			setWantedState(ArmControlState.Idle);
		}

	}

	public Arm(TalonSRX slave, TalonSRX master) {
		this.armPID = new SynchronousPIDF(Constants.ARM_Kp, Constants.ARM_Ki, Constants.ARM_Kd, Constants.ARM_Kf);
		
		this.armPID.setOutputRange(-0.1, 0.5);
		
		this.slave = slave;
		this.master = master;

		CANTalonFactory.updateCANTalonToDefault(this.master);
		this.master.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);
		this.master.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 0);

		 CANTalonFactory.updatePermanentSlaveTalon(this.slave, this.master.getDeviceID());

		setCurrentLimit(master, Constants.MAX_ARM_CURRENT, Constants.NOMINAL_ARM_CURRENT, Constants.MAX_ARM_CURRENT_TIME);

		 this.slave.setInverted(true);
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

	public void setWantedSpeed(double power) {
		this.wantedSpeed = power;
		this.controlState = ArmControlState.Moving;
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

	public void updateEncoder() {
		int minTicks = degreesToEncoderTicks(Constants.MIN_ARM_ENCODER_DEGREES);
		int maxTicks = degreesToEncoderTicks(Constants.MAX_ARM_ENCODER_DEGREES);
		if (this.master.getSensorCollection().isFwdLimitSwitchClosed()) {
			this.fakeEncoderValue = minTicks;
			this.master.setSelectedSensorPosition(minTicks, 0, 0);
		} else if (this.master.getSensorCollection().isRevLimitSwitchClosed()) {
			this.fakeEncoderValue = maxTicks;
			this.master.setSelectedSensorPosition(maxTicks, 0, 0);
		}
	}
	
	public int getFakeEncoderValue() {
		return this.fakeEncoderValue;
	}

	public void setWantedState(ArmControlState state) {
		this.controlState = state;
	}

	public synchronized void setPosition(ArmPosition pos) {
		setWantedState(ArmControlState.Position);
		this.updateAngleSetpoint(pos);
	}

	public double encoderTicksToDegrees(int ticks) {

		return ((double) ticks / Constants.ARM_TICKS_PER_REVOLUTION) * Constants.ARM_GEAR_RATIO * 360D;
	}

	public int degreesToEncoderTicks(double degrees) {
		return (int) ((degrees * Constants.ARM_TICKS_PER_REVOLUTION) / (Constants.ARM_GEAR_RATIO * 360D));
	}

	private synchronized void updateAngleSetpoint(ArmPosition pos) {
		SmartDashboard.putNumber("Arm Wanted Degrees", pos.getAngle());
		armPID.setSetpoint(pos.getAngle());
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
		double power = -this.armPID.calculate(degrees, dt);
		this.setRestrictedSpeed(power);
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(new ArmLoop());
	}

	@Override
	protected void initDefaultCommand() {
		// this.setDefaultCommand(new ArmIdle());
//		this.setDefaultCommand(new ArmJoystick());
	}

	public boolean atLowerLimit() {
		return this.master.getSensorCollection().isRevLimitSwitchClosed();
		
	}
	
}

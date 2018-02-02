package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.ArmIdle;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.control.SynchronousPIDF;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
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
	private DigitalInput frontLimitSwitch;
	private DigitalInput backLimitSwitch;

	private SynchronousPIDF armPID;
	private double PIDKp;
	private double PIDKi;
	private double PIDKd;
	private double PIDKf;

	private ArmControlState controlState;
	private double wantedSpeed;
	private double lastTimeStamp;

	public static enum ArmControlState {
		Moving, // Move at joystick speed
		Angle, // Under PID angle control
		Idle // Do Nothing
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
		int tempFrontPin = (int) Math.random() * 19;
		this.frontLimitSwitch = new DigitalInput(tempFrontPin);
		this.backLimitSwitch = new DigitalInput(tempFrontPin + 1);

		this.PIDKp = 0.00005;
		;
		this.PIDKi = 0.0000001;
		this.PIDKd = 0;
		this.PIDKf = 0.035;
		this.armPID = new SynchronousPIDF(this.PIDKp, this.PIDKp, this.PIDKd, this.PIDKd);

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
		// if ((frontLimitSwitch.get() && speed > 0) || (backLimitSwitch.get() && speed
		// < 0)) {
		// // One or both limit switch are broken and the speed will move in a legal
		// // direction
		// this.master.set(ControlMode.PercentOutput, speed);
		// updateEncoder();
		// } else if (!(backLimitSwitch.get() || !frontLimitSwitch.get())) {
		// // No limit switch is broken
		// this.master.set(ControlMode.PercentOutput, speed);
		// } else {
		// // One or both limit switch are broken and the speed will move in an illegal
		// // direction
		// updateEncoder();
		// this.master.set(ControlMode.PercentOutput, 0);
		// }
		SmartDashboard.putNumber("Arm Output", speed);

		this.master.set(ControlMode.PercentOutput, speed);

	}

	public void setWantedSpeed(double power) {
		this.wantedSpeed = power;
		this.controlState = ArmControlState.Moving;
	}

	@Override
	public void outputToSmartDashboard() {
//		SmartDashboard.putNumber("Arm Motor Current Slave", this.getArmSlaveDegrees());
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
		if (this.frontLimitSwitch.get()) {
			this.master.setSelectedSensorPosition(Constants.MIN_ARM_ENCODER_VALUE, 0, 0);
		} else if (this.backLimitSwitch.get()) {
			this.master.setSelectedSensorPosition(Constants.MAX_ARM_ENCODER_VALUE, 0, 0);
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
		return (double) ticks / Constants.ARM_TICKS_PER_REVOLUTION * Constants.ARM_GEAR_RATIO * 360D;
	}

	private double degreesToEncoderTicks(double degrees) {
		return (double) degrees * Constants.ARM_TICKS_PER_REVOLUTION * Constants.ARM_GEAR_RATIO * 360D;
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

		double masterDegrees = this.getArmDegrees();
		double power = this.armPID.calculate(masterDegrees, dt);

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

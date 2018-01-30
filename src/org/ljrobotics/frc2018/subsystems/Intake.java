package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements LoopingSubsystem {

	private static Intake instance;
	
	public static Intake getInstance() {
		if(instance == null) {
			TalonSRX left = new TalonSRX(Constants.LEFT_INTAKE_MOTOR_ID);
			TalonSRX right = new TalonSRX(Constants.RIGHT_INTAKE_MOTOR_ID);
			instance = new Intake(left, right);
		}
		return instance;
	}
	
	private TalonSRX left;
	private TalonSRX right;
	
	private IntakeControlState controlState;
	
	private double overCurrentProtectionTimeStart;
	
	private double wantedTensionPower;
	
	public static enum IntakeControlState {
		Suck, //Pull in the Cube
		Spit, //Spit out the Cube
		Idle //Do Nothing
	}
	
	private class IntakeLoop implements Loop {

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			switch (controlState) {
				case Suck:
					setSpeed(Constants.SUCK_SPEED);
					break;
				case Spit:
					setSpeed(Constants.SPIT_SPEED);
					break;
				case Idle:
					setSpeed( 0);
					break;
				default:
					break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	}
	
	public Intake(TalonSRX left, TalonSRX right) {
		this.left = left;
		this.right = right;
		
		setCurrentLimit(left, Constants.MAX_SUCK_CURRENT, Constants.NOMINAL_SUCK_CURRENT, Constants.MAX_SUCK_CURRENT_TIME);
		setCurrentLimit(right, Constants.MAX_SUCK_CURRENT, Constants.NOMINAL_SUCK_CURRENT, Constants.MAX_SUCK_CURRENT_TIME);
		
		this.left.setInverted(false);
		this.right.setInverted(true);
		
		this.left.configOpenloopRamp(0.125, 0);
		this.right.configOpenloopRamp(0.125, 0);
		
		this.controlState = IntakeControlState.Idle;
		
		this.wantedTensionPower = 0;
	}
	
	private void setCurrentLimit(TalonSRX talon, int max, int nominal, int time) {
		talon.configPeakCurrentDuration(time, 0);
		talon.enableCurrentLimit(true);
		talon.configContinuousCurrentLimit(nominal, 0);
		talon.configPeakCurrentLimit(max, 0);
	}
	
	@Override
	public void stop() {
		this.setSpeed(0);
	}

	@Override
	public void reset() {

	}
	
//	public void setSpeedCurrentChecked(double timestamp, double speed) {
//		double leftCurrent = this.left.getOutputCurrent();
//		double rightCurrent = this.right.getOutputCurrent();
//		if(this.overCurrentProtectionTimeStart + Constants.INTAKE_OVERCURRENT_PROTECTION_TIME > timestamp) {
//			this.left.set(ControlMode.PercentOutput, 0);
//			this.right.set(ControlMode.PercentOutput, 0);
//			return;
//		}
//		if(Math.max(leftCurrent, rightCurrent) < Constants.MAX_SUCK_CURRENT) {
//			this.left.set(ControlMode.PercentOutput, speed);
//			this.right.set(ControlMode.PercentOutput, speed);
//		} else {
//			if(this.overCurrentProtectionTimeStart > timestamp - 1) {
//				this.left.set(ControlMode.PercentOutput, speed);
//				this.right.set(ControlMode.PercentOutput, speed);
//			}
//			this.overCurrentProtectionTimeStart = timestamp;
//			this.left.set(ControlMode.PercentOutput, 0);
//			this.right.set(ControlMode.PercentOutput, 0);
//		}
//		
//	}
	
	private void setSpeed(double speed) {
		this.left.set(ControlMode.PercentOutput, speed);
		this.right.set(ControlMode.PercentOutput, speed);
	}
	
	public void setTensionPower(double power) {
		this.wantedTensionPower = power;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Motor Current Right", this.right.getOutputCurrent());
		SmartDashboard.putNumber("Intake Motor Current Left", this.left.getOutputCurrent());
	}

	@Override
	public void writeToLog() {

	}

	@Override
	public void zeroSensors() {

	}
	
	public void setWantedState(IntakeControlState state) {
		this.controlState = state;
		System.out.println(state);
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(new IntakeLoop());
	}

	@Override
	protected void initDefaultCommand() {
	}

}

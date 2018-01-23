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
	
	/**
	 * The current over current time. If it is negative not in over current mode.
	 */
	private double overCurrentTimeStartTime;
	
	private boolean overCurrentProtection;
	private double overCurrentProtectionTimeStart;
	
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
					setSpeed(0);
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
		
		this.left.setInverted(false);
		this.right.setInverted(true);
		
		this.controlState = IntakeControlState.Idle;
		
		this.overCurrentTimeStartTime = -Constants.INTAKE_OVERCURRENT_PROTECTION_TIME*2;
		
		this.overCurrentProtection = false;
	}
	
	@Override
	public void stop() {
		this.setSpeed(0);
	}

	@Override
	public void reset() {

	}
	
	public void setSpeedCurrentChecked(double timestamp, double speed) {
		double leftCurrent = this.left.getOutputCurrent();
		double rightCurrent = this.right.getOutputCurrent();
		if(this.overCurrentProtectionTimeStart + Constants.INTAKE_OVERCURRENT_PROTECTION_TIME > timestamp) {
			this.left.set(ControlMode.PercentOutput, 0);
			this.left.set(ControlMode.PercentOutput, 0);
			return;
		}
		if(Math.max(leftCurrent, rightCurrent) < Constants.MAX_SUCK_CURRENT) {
			this.left.set(ControlMode.PercentOutput, speed);
			this.right.set(ControlMode.PercentOutput, speed);
		} else {
			this.overCurrentProtectionTimeStart = timestamp;
			this.left.set(ControlMode.PercentOutput, 0);
			this.right.set(ControlMode.PercentOutput, 0);
		}
		
//		if(this.overCurrentProtection && timestamp > this.overCurrentTimeStartTime + Constants.INTAKE_OVERCURRENT_PROTECTION_TIME) {
//			
//		}
//		if(Math.max(leftCurrent, rightCurrent) > Constants.MAX_SUCK_CURRENT) {
//			if(this.overCurrentTimeStartTime < 0) {
//				this.overCurrentTimeStartTime = timestamp;
//			} else if(this.overCurrentTimeStartTime + Constants.MAX_SUCK_CURRENT_TIME < timestamp) {
//				this.overCurrentProtection = true;
//				this.overCurrentTimeStartTime = timestamp;
//			}
//		} else {
//			this.overCurrentTimeStartTime = -1;
//		}
	}
	
	private void setSpeed(double speed) {
		this.left.set(ControlMode.PercentOutput, speed);
		this.right.set(ControlMode.PercentOutput, speed);
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

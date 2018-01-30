package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.commands.MoveArmWithJoystick;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm extends Subsystem implements LoopingSubsystem {

	private static Arm instance;
	
	public static Arm getInstance() {
		if(instance == null) {
			TalonSRX left = new TalonSRX(Constants.LEFT_ARM_MOTOR_ID);
			TalonSRX right = new TalonSRX(Constants.RIGHT_ARM_MOTOR_ID);
			instance = new Arm(left, right);
		}
		return instance;
	}
	
	private TalonSRX left;
	private TalonSRX right;
	
	private ArmControlState controlState;
	
	private double wantedSpeed;
	
	public static enum ArmControlState {
		Moving, //Move at joystick speed
		Idle //Do Nothing
	}
	
	private class IntakeLoop implements Loop {

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			switch (controlState) {
				case Moving:
					setSpeed(wantedSpeed);
					break;
				case Idle:
					setSpeed(0);
				default:
					break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	}
	
	public Arm(TalonSRX left, TalonSRX right) {
		this.left = left;
		this.right = right;
		
		setCurrentLimit(left, Constants.MAX_ARM_CURRENT, Constants.NOMINAL_ARM_CURRENT, Constants.MAX_ARM_CURRENT_TIME);
		setCurrentLimit(right, Constants.MAX_ARM_CURRENT, Constants.NOMINAL_ARM_CURRENT, Constants.MAX_ARM_CURRENT_TIME);
		
		this.left.setInverted(false);
		this.right.setInverted(true);
		
		this.left.configOpenloopRamp(0.125, 0);
		this.right.configOpenloopRamp(0.125, 0);
		
		this.controlState = ArmControlState.Idle;
		
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
		this.setSpeed(0);
	}

	@Override
	public void reset() {

	}
	
	private void setSpeed(double speed) {
		this.left.set(ControlMode.PercentOutput, speed);
		this.right.set(ControlMode.PercentOutput, speed);
	}
	
	public void setWantedSpeed(double power) {
		this.wantedSpeed = power;
		this.controlState = ArmControlState.Moving;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Arm Motor Current Right", this.right.getOutputCurrent());
		SmartDashboard.putNumber("Arm Motor Current Left", this.left.getOutputCurrent());
	}

	@Override
	public void writeToLog() {

	}

	@Override
	public void zeroSensors() {

	}
	
	public void setWantedState(ArmControlState state) {
		this.controlState = state;
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(new IntakeLoop());
	}

	@Override
	protected void initDefaultCommand() {
		this.setDefaultCommand(new MoveArmWithJoystick());
	}

}

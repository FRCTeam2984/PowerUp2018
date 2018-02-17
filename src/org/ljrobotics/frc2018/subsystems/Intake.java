package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem implements LoopingSubsystem {

	private static Intake instance;
	
	public static Intake getInstance() {
		if(instance == null) {
			TalonSRX left = new TalonSRX(Constants.LEFT_INTAKE_MOTOR_ID);
			TalonSRX right = new TalonSRX(Constants.RIGHT_INTAKE_MOTOR_ID);
			AnalogInput leftDistance = new AnalogInput(Constants.INTAKE_LEFT_DISTANCE_PORT);
			AnalogInput rightDistance = new AnalogInput(Constants.INTAKE_RIGHT_DISTANCE_PORT);
			instance = new Intake(left, right, leftDistance, rightDistance);
		}
		return instance;
	}
	
	private TalonSRX left;
	private TalonSRX right;
	
	private AnalogInput leftDistance;
	private AnalogInput rightDistance;
	
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
			updateLEDs();
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
	
	public Intake(TalonSRX left, TalonSRX right, AnalogInput leftDistance, AnalogInput rightDistance) {
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
		
		this.leftDistance = leftDistance;
		this.rightDistance = rightDistance;
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
	
	public void setTensionPower(double power) {
		this.wantedTensionPower = power;
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Intake Motor Current Right", this.right.getOutputCurrent());
		SmartDashboard.putNumber("Intake Motor Current Left", this.left.getOutputCurrent());
		SmartDashboard.putNumber("Intake Distance Left", this.leftDistance.getVoltage());
		SmartDashboard.putNumber("Intake Distance Right", this.rightDistance.getVoltage());
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
	
	public void updateLEDs() {
		double minVoltage = Math.min(this.leftDistance.getVoltage(), this.rightDistance.getVoltage());
		if(minVoltage < 2.0) {
			LEDControl.getInstance().setWantedState(LEDControl.LEDState.OFF);
		} else {
			LEDControl.getInstance().setWantedState(LEDControl.LEDState.ON);
		}
	}

	@Override
	protected void initDefaultCommand() {
	}

}

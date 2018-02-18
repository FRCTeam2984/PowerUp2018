package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.events.Triggerer;
import org.ljrobotics.lib.util.events.Triggers;

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
	
	private boolean in;
	
	public static enum IntakeControlState {
		Suck, //Pull in the Cube
		Spit, //Spit out the Cube
		SpitSlow, //Spit at 25% power
		Idle //Do Nothing
	}
	
	private class IntakeLoop implements Loop {

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			updateEvents();
			switch (controlState) {
				case Suck:
					setSpeed(Constants.SUCK_SPEED);
					break;
				case Spit:
					setSpeed(Constants.SPIT_SPEED);
					break;
				case SpitSlow:
					setSpeed(Constants.SPIT_SPEED/2);
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
		
		this.leftDistance = leftDistance;
		this.rightDistance = rightDistance;
		
		this.in = false;
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
	
	private void updateEvents() {
		this.updateEvents(this.leftDistance.getVoltage(), this.rightDistance.getAverageVoltage());
	}
	
	protected void updateEvents(double left, double right) {
		double minVoltage = Math.min(left, right);
		if(minVoltage < Constants.IN_VOLTAGE_THRESH) {
			if(this.in) {
				Triggerer.getInstance().trigger(Triggers.CubeOut);
				this.in = false;
			}
		} else {
			if(!this.in) {
				Triggerer.getInstance().trigger(Triggers.CubeIn);
				this.in = true;
			}
		}
	}

	@Override
	protected void initDefaultCommand() {
	}

}

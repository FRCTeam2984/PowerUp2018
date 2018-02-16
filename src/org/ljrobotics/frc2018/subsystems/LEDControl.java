package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LEDControl extends Subsystem implements LoopingSubsystem {
	
	private static LEDControl instance;
	private Spark output;
	private double brightness;
	private double blink_time;
	private double past_time;
	private LEDState state;
	
	// The local LED loop
	private LEDLoop ledLoop = new LEDLoop();
	
	/**
	 * 
	 * 
	 * @param pinToControl
	 *            The pin that the led subsystem outputs to
	 * @param d
	 *            The brightness of the led output, from 0 to 255
	 * 
	 * @author creikey
	 */
	public LEDControl(int pinToControl, int brightness, double blink_time ) {
		this.output = new Spark(pinToControl);
		this.brightness = brightness;
		this.blink_time = blink_time;
		this.state = LEDState.OFF;
	}
	
	public LEDControl(Spark pwmControl, int brightness, double blink_time ) {
		this.output = pwmControl;
		this.brightness = brightness;
		this.blink_time = blink_time;
		this.state = LEDState.OFF;
	}
	
	public static LEDControl getInstance() {
		if(instance == null) {
			instance = new LEDControl(0, 1, 1D);
		}
		return instance;
	}
	
	public enum LEDState {
		BLINKING,
		ON,
		OFF
	}
	
	private class LEDLoop implements Loop {
		public void onStart( double timestamp ) {
			output.setSpeed(0);
			past_time = System.currentTimeMillis();
		}
		
		public void onLoop( double timestamp ) {
			switch(state) {
			case BLINKING:
				if(System.currentTimeMillis() - past_time >= blink_time*1000) {
					if( output.get() > 0.0 ) {
						output.set(brightness);
					} else {
						output.set(0);
					}
					past_time = System.currentTimeMillis();
				}
				break;
			case ON:
				output.set(brightness);
				break;
			case OFF:
			default:
				output.set(0);
				break;
			}
		}
		
		public void onStop( double timestamp ) {
			output.set(0);
		}
	}
	
	public double getLedBrightness() {
		return this.brightness;
	}
	
	public void setLEDBrightnes(double inBrightness) {
		this.brightness = inBrightness;
	}

	@Override
	public void stop() {
		this.output.set(0);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("LED Brightness", this.getLedBrightness());
		SmartDashboard.putNumber("blink time", this.getBlinkTime());
		
	}

	public double getBlinkTime() {
		return this.blink_time;
	}
	
	public void setBlinkTime(double blink_time) {
		this.blink_time = blink_time;
	}

	@Override
	public void writeToLog() {
		
	}

	@Override
	public void zeroSensors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(ledLoop);
		
	}

	@Override
	protected void initDefaultCommand() {
		
	}

	public void setWantedState(LEDState state) {
		this.state = state;
	}
	
}

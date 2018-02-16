package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.loops.Loop;
import org.ljrobotics.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LEDControl extends Subsystem implements LoopingSubsystem {
	
	private static LEDControl instance;
	private PWM pwmToUse;
	private int brightness;
	private double blink_time;
	private double past_time;
	
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
		this.pwmToUse = new PWM(pinToControl);
		this.brightness = brightness;
		this.blink_time = blink_time;
	}
	
	public LEDControl(PWM pwmControl, int brightness, double blink_time ) {
		this.pwmToUse = pwmControl;
		this.brightness = brightness;
		this.blink_time = blink_time;
	}
	
	public static LEDControl getInstance() {
		if(instance == null) {
			instance = new LEDControl(0, 255, 1D);
		}
		return instance;
	}
	
	private class LEDLoop implements Loop {
		public void onStart( double timestamp ) {
			pwmToUse.setSpeed(0);
			past_time = System.currentTimeMillis();
		}
		
		public void onLoop( double timestamp ) {
			if(System.currentTimeMillis() - past_time >= blink_time*1000) {
				if( pwmToUse.getRaw() > 0.0 ) {
					pwmToUse.setRaw(brightness);
				} else {
					pwmToUse.setRaw(0);
				}
				past_time = System.currentTimeMillis();
			}
		}
		
		public void onStop( double timestamp ) {
			pwmToUse.setRaw(0);
		}
	}
	
	public int getLedBrightness() {
		return this.brightness;
	}
	
	public void setLEDBrightnes(int inBrightness) {
		this.brightness = inBrightness;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		this.pwmToUse.setRaw(0);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}

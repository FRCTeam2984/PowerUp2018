package org.ljrobotics.frc2018.commands;

import org.ljrobotics.frc2018.subsystems.LEDControl;

import edu.wpi.first.wpilibj.command.Command;

public class BlinkLED extends Command {
	
	private int brightness;
	private double blinkTime;

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * @author creikey
	 * 
	 * @param brightness brightness value from 0 to 255
	 * @param pwm pwm the pwm controller to use to blink
	 * @param blinkTime blinkTime the time delay between each on and off
	 */
	public BlinkLED(int brightness, double blinkTime) {
		this.brightness = brightness;
		this.blinkTime = blinkTime;
		this.requires(LEDControl.getInstance());
	}
	
	@Override
	protected void initialize() {
		LEDControl.getInstance().setBlinkTime(blinkTime);
		LEDControl.getInstance().setLEDBrightnes(brightness);
	}

	
}

package org.ljrobotics.lib.util.drivers;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * This class is a thin wrapper around the AnalogGyro
 * @author Grant
 */
public class LazyGyroscope extends AnalogGyro {
	private static LazyGyroscope instance;
	public static LazyGyroscope getInstance() {
		if (instance == null) {
			instance = new LazyGyroscope();
		}
		return instance;
	}

	private LazyGyroscope() {
		super(new AnalogInput(0));
	}
}

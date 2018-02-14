package org.ljrobotics.lib.util.drivers;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogInput;

/**
 * This class is a thin wrapper around the ADXRS450_Gyro
 * @author Grant
 */
public class LazyGyroscope extends ADXRS450_Gyro {
	private static LazyGyroscope instance;
	public static LazyGyroscope getInstance() {
		if (instance == null) {
			instance = new LazyGyroscope();
		}
		return instance;
	}

	private LazyGyroscope() {
		super();
	}
}

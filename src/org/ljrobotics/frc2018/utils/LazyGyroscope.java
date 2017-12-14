package org.ljrobotics.frc2018.utils;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

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

	}
}

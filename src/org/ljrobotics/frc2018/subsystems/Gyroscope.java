package org.ljrobotics.frc2018.subsystems;


import org.ljrobotics.frc2018.loops.Looper;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;


/**
 * This is the super class for all subsystems. Every subsystem on the robot will extend this class.
 *
 * The most basic functionality is described in this class. Every subsystem can be stopped and reset.
 * {@link #registerEnabledLoops} method.
 * In addition, all of the subsystems will need to execute code periodically which handled by the
 *
 * @author Grant
 *
 */
public class Gyroscope extends Subsystem {
	public static Gyroscope instance;
	private ADXRS450_Gyro gyro;
	public static Gyroscope getInstance() {
		if (instance == null) {


			instance = new Gyroscope();
		}
		return instance;
	}
	public Gyroscope() {
		this.gyro = new ADXRS450_Gyro();
	}
	/**
	 * Stops all of the activity in the subsystem.
	 */
	public void stop(){
		return;
	}

	/**
	 * Returns the subsystem to original state. Ie resets sensors and clears accumulators.
	 */
	public void reset(){
		return;
	}
	public void calibrate(){
		this.gyro.calibrate();
	}
	public double getAngle(){
		return this.gyro.getAngle();
	}
	public double getRate(){
		return this.gyro.getRate();
	}

	/**
	 * Called once after subsystem is created. This allows the subsystem to register any loops.
	 *
	 * @param enabledLooper The looper that will be responsible for calling the subsystems loops.
	 */
	public void registerEnabledLoops(Looper enabledLooper){

	}

}

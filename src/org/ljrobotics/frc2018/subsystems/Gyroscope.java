package org.ljrobotics.frc2018.subsystems;


import org.ljrobotics.frc2018.loops.Looper;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * This is the gyroscope subystem. It deals with getting gyroscope data.
 * @author Grant
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
	public void stop(){
		return;
	}
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
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Gyro Angle", this.getAngle());
	}
	
	@Override
	public void writeToLog() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void zeroSensors() {
		this.calibrate();
	}

}

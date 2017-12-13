package org.ljrobotics.frc2018.utils;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is a thin wrapper around the ADXRS450_Gyro
 * @author Grant
 */
public class LazyGyroscope extends ADXRS450_Gyro {

    public LazyGyroscope() {
        super();
    }
    
    public void outputToSmartDashboard() {
      SmartDashboard.putNumber("Gyro Angle", this.getAngle());
    }
}

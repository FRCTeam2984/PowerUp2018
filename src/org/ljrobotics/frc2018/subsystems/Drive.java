package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.RobotMap;
import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.lib.util.control.PathFollower;
import org.ljrobotics.lib.util.drivers.CANTalonFactory;
import org.ljrobotics.lib.util.drivers.LazyCANTalon;

import com.ctre.CANTalon;
import com.ctre.CANTalon.StatusFrameRate;

/**
 * The Drive subsystem. This subsystem is responsible for everything
 * regarding driving. It controls the four drive motors.
 * 
 * @author Max
 *
 */
public class Drive extends Subsystem{

	private static Drive instance;

	public static Drive getInstance() {
		if (instance == null) {
			CANTalon frontLeft = new LazyCANTalon(RobotMap.FRONT_LEFT_MOTOR_ID);
			CANTalon frontRight = new LazyCANTalon(RobotMap.FRONT_RIGHT_MOTOR_ID);
			CANTalon rearLeft = new LazyCANTalon(RobotMap.REAR_LEFT_MOTOR_ID);
			CANTalon rearRight = new LazyCANTalon(RobotMap.REAR_RIGHT_MOTOR_ID);

			instance = new Drive(frontLeft, frontRight, rearLeft, rearRight);
		}
		return instance;
	}
	
	// The robot drivetrain's various states.
	public enum DriveControlState {
		VELOCITY_SETPOINT, //Under PID velocity control
		PATH_FOLLOWING //Following a path
	}
	
	public static final int VELOCITY_CONTROL_SLOT = 0;
	
	// Talons
	private CANTalon leftMaster;
	private CANTalon rightMaster;
	private CANTalon leftSlave;
	private CANTalon rightSlave;
	
	// Control States
	private DriveControlState driveControlState;
	
	// Controllers
	private PathFollower pathFollower;
	
	// Hardware States
	private boolean isBrakeMode;

	/**
	 * Creates a new Drive Subsystem from that controls the given motor controllers.
	 * 
	 * @param frontLeft the font left talon motor controller
	 * @param frontRight the font right talon motor controller
	 * @param backLeft the back left talon motor controller
	 * @param backRight the back right talon motor controller
	 */
	public Drive(CANTalon frontLeft, CANTalon frontRight, CANTalon backLeft, CANTalon backRight) {

		this.leftMaster = frontLeft;
		this.rightMaster = frontRight;
		this.leftSlave = backLeft;
		this.rightSlave = backRight;
		
		CANTalonFactory.updateCANTalonToDefault(this.leftMaster);
        leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        
        CANTalonFactory.updatePermanentSlaveTalon(this.leftSlave, this.leftMaster.getDeviceID());
        leftSlave.reverseOutput(false);
        leftMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);
        
		CANTalonFactory.updateCANTalonToDefault(this.rightMaster);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        
        CANTalonFactory.updatePermanentSlaveTalon(this.rightSlave, this.rightMaster.getDeviceID());
        rightSlave.reverseOutput(false);
        rightMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);
		
		this.driveControlState = DriveControlState.VELOCITY_SETPOINT;

		this.isBrakeMode = false;
		this.setBrakeMode(true);
	}
	

	@Override
	public void stop() {
		this.leftMaster.set(0);
		this.rightMaster.set(0);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}
	
	
	
	/**
     * Configures talons for velocity control
     */
    private void configureTalonsForSpeedControl() {
        if (!usesTalonVelocityControl(driveControlState)) {
            // We entered a velocity control state.
            leftMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            leftMaster.setNominalClosedLoopVoltage(12.0);
            leftMaster.setProfile(VELOCITY_CONTROL_SLOT);
            leftMaster.configNominalOutputVoltage(Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT,
                    -Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT);
            rightMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            rightMaster.setNominalClosedLoopVoltage(12.0);
            rightMaster.setProfile(VELOCITY_CONTROL_SLOT);
            rightMaster.configNominalOutputVoltage(Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT,
                    -Constants.DRIVE_HIGH_GEAR_NOMINAL_OUTPUT);
            setBrakeMode(true);
        }
    }
    
    /**
     * Check if the drive talons are configured for velocity control
     */
    protected static boolean usesTalonVelocityControl(DriveControlState state) {
        if (state == DriveControlState.VELOCITY_SETPOINT || state == DriveControlState.PATH_FOLLOWING) {
            return true;
        }
        return false;
    }
	
	public synchronized void setBrakeMode(boolean on) {
		if (isBrakeMode != on) {
            isBrakeMode = on;
            rightMaster.enableBrakeMode(on);
            rightSlave.enableBrakeMode(on);
            leftMaster.enableBrakeMode(on);
            leftSlave.enableBrakeMode(on);
        }
    }

}

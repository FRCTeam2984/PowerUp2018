package org.ljrobotics.lib.util.drivers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.MotorSafety;


/**
 * Creates CANTalon objects and configures all the parameters we care about to factory defaults. Closed-loop and sensor
 * parameters are not set, as these are expected to be set by the application.
 */
public class CANTalonFactory {

    public static class Configuration {
    	public LimitSwitchSource LIMIT_SWITCH_SOURCE = LimitSwitchSource.FeedbackConnector;
        public LimitSwitchNormal LIMIT_SWITCH_NORMALLY_OPEN = LimitSwitchNormal.NormallyOpen;
        public double NOMINAL_OUTPUT = 0;
        public double PEAK_OUTPUT = 1;
        public NeutralMode NEUTRAL_MODE = NeutralMode.Coast;
        public boolean ENABLE_CURRENT_LIMIT = false;
        public boolean ENABLE_SOFT_LIMIT = false;
        public boolean ENABLE_LIMIT_SWITCH = false;
        public int CURRENT_LIMIT = 0;
        public double EXPIRATION_TIMEOUT_SECONDS = MotorSafety.DEFAULT_SAFETY_EXPIRATION;
        public int FORWARD_SOFT_LIMIT = 0;
        public boolean INVERTED = false;
        public double NOMINAL_CLOSED_LOOP_VOLTAGE = 12;
        public int REVERSE_SOFT_LIMIT = 0;
        public boolean SAFETY_ENABLED = false;

        public int CONTROL_FRAME_PERIOD_MS = 5;
        public int MOTION_CONTROL_FRAME_PERIOD_MS = 100;

        public VelocityMeasPeriod VELOCITY_MEASUREMENT_PERIOD = VelocityMeasPeriod.Period_100Ms;
        public int VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW = 64;

        public double VOLTAGE_COMPENSATION_RAMP_RATE = 0;
        public double VOLTAGE_RAMP_RATE = 0;
    }

    private static final Configuration DEFAULT_CONFIGURATION = new Configuration();
    private static final Configuration SLAVE_CONFIGURATION = new Configuration();

    static {
        SLAVE_CONFIGURATION.CONTROL_FRAME_PERIOD_MS = 1000;
        SLAVE_CONFIGURATION.MOTION_CONTROL_FRAME_PERIOD_MS = 1000;
    }

    // Create a CANTalon with the default (out of the box) configuration.
    public static TalonSRX createDefaultTalon(int id) {
        return createTalon(id, DEFAULT_CONFIGURATION);
    }

    public static TalonSRX createPermanentSlaveTalon(int id, int master_id) {
        final TalonSRX talon = createTalon(id, SLAVE_CONFIGURATION);
        talon.set(ControlMode.Follower, master_id);
        return talon;
    }
    
    public static TalonSRX updatePermanentSlaveTalon(TalonSRX talon, int master_id) {
    	updateCANTalon(talon, SLAVE_CONFIGURATION);
        talon.set(ControlMode.Follower, master_id);
        return talon;
    }

    public static TalonSRX createTalon(int id, Configuration config) {
    	TalonSRX talon = new LazyCANTalon(id, config.CONTROL_FRAME_PERIOD_MS);
        updateCANTalon(talon, config);
        return talon;
    }
    
    public static void updateCANTalon(TalonSRX talon, Configuration config) {
    	talon.set(ControlMode.PercentOutput, 0);
        talon.changeMotionControlFramePeriod(config.MOTION_CONTROL_FRAME_PERIOD_MS);
        talon.setIntegralAccumulator(0, 0, 0);
        talon.clearMotionProfileHasUnderrun(0);
        talon.clearMotionProfileTrajectories();
        talon.clearStickyFaults(0);
        talon.configForwardLimitSwitchSource(config.LIMIT_SWITCH_SOURCE, config.LIMIT_SWITCH_NORMALLY_OPEN, 0);
        talon.configNominalOutputForward(config.NOMINAL_OUTPUT, 0);
        talon.configNominalOutputReverse(-config.NOMINAL_OUTPUT, 0);
        talon.configPeakOutputForward(config.PEAK_OUTPUT, 0);
        talon.configPeakOutputReverse(-config.PEAK_OUTPUT, 0);
        talon.configReverseLimitSwitchSource(config.LIMIT_SWITCH_SOURCE, config.LIMIT_SWITCH_NORMALLY_OPEN, 0);
        talon.setNeutralMode(config.NEUTRAL_MODE);
        talon.enableCurrentLimit(config.ENABLE_CURRENT_LIMIT);
        talon.configForwardSoftLimitEnable(config.ENABLE_SOFT_LIMIT, 0);
        talon.configReverseSoftLimitEnable(config.ENABLE_SOFT_LIMIT, 0);
        talon.setSelectedSensorPosition(0, 0, 0);
        talon.configPeakCurrentLimit(config.CURRENT_LIMIT, 0);
        if(config.ENABLE_CURRENT_LIMIT) {
        	talon.configContinuousCurrentLimit(config.CURRENT_LIMIT, 0);
        }
        talon.configForwardSoftLimitThreshold(config.FORWARD_SOFT_LIMIT, 0);
        talon.setInverted(config.INVERTED);
        talon.setSelectedSensorPosition(0, 0, 0);
        talon.selectProfileSlot(0, 0);
        talon.configReverseSoftLimitThreshold(config.REVERSE_SOFT_LIMIT, 0);
        talon.configVelocityMeasurementPeriod(config.VELOCITY_MEASUREMENT_PERIOD, 0);
        talon.configVelocityMeasurementWindow(config.VELOCITY_MEASUREMENT_ROLLING_AVERAGE_WINDOW, 0);
        talon.configOpenloopRamp(config.VOLTAGE_RAMP_RATE, 0);

    }
    
	public static void updateCANTalonToDefault(TalonSRX talon) {
		updateCANTalon(talon, DEFAULT_CONFIGURATION);
	}
}
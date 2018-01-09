package org.ljrobotics.lib.util.drivers;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * This class is a thin wrapper around the TalonSRX that reduces CAN bus / CPU overhead by skipping duplicate set
 * commands. (By default the Talon flushes the Tx buffer on every set call).
 */
public class LazyCANTalon extends TalonSRX {
    protected double lastSet = Double.NaN;
    protected ControlMode lastControlMode = null;

    public LazyCANTalon(int deviceNumber, int framePeriod) {
        super(deviceNumber);
        this.setControlFramePeriod(ControlFrame.Control_3_General, framePeriod);
    }
    
    public LazyCANTalon(int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public void set(ControlMode mode, double value) {
        if (value != lastSet || mode != lastControlMode) {
            lastSet = value;
            lastControlMode = mode;
            super.set(mode, value);
        }
    }
}
package org.ljrobotics.frc2018;

import java.util.List;

import org.ljrobotics.frc2018.loops.Looper;
import org.ljrobotics.frc2018.subsystems.Subsystem;

/**
 * Used to reset, start, stop, and update all subsystems at once
 */
public class SubsystemManager {

    private final List<Subsystem> subsystems;

    public SubsystemManager(List<Subsystem> subsystems) {
        this.subsystems = subsystems;
    }

    public void outputToSmartDashboard() {
        subsystems.forEach((s) -> s.outputToSmartDashboard());
    }

    public void writeToLog() {
        subsystems.forEach((s) -> s.writeToLog());
    }

    public void stop() {
        subsystems.forEach((s) -> s.stop());
    }

    public void zeroSensors() {
        subsystems.forEach((s) -> s.zeroSensors());
    }

    public void registerEnabledLoops(Looper enabledLooper) {
        subsystems.forEach((s) -> s.registerEnabledLoops(enabledLooper));
    }
}
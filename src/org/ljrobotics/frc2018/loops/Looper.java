package org.ljrobotics.frc2018.loops;

import java.util.ArrayList;
import java.util.List;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.lib.util.CrashTrackingRunnable;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This code runs all of the robot's loops. Loop objects are stored in a List object. They are started when the robot
 * powers up and stopped after the match.
 */
public class Looper {
    public final double kPeriod = Constants.LOOPER_Dt;

    private boolean running;

    private final Notifier notifier;
    private final List<Loop> loops;
    private final Object taskRunningLock = new Object();
    private double timestamp = 0;
    private double dt = 0;

    private final CrashTrackingRunnable runnable_ = new CrashTrackingRunnable() {
        @Override
        public void runCrashTracked() {
            synchronized (taskRunningLock) {
                if (running) {
                    double now = Timer.getFPGATimestamp();

                    for (Loop loop : loops) {
                        loop.onLoop(now);
                    }

                    dt = now - timestamp;
                    timestamp = now;
                }
            }
        }
    };

    public Looper() {
        notifier = new Notifier(runnable_);
        running = false;
        loops = new ArrayList<>();
    }

    /**
     * Registers a loop to be run periodically.
     * @param loop the loop to run
     */
    public synchronized void register(Loop loop) {
        synchronized (taskRunningLock) {
            loops.add(loop);
        }
    }

    public synchronized void start() {
        if (!running) {
            System.out.println("Starting loops");
            synchronized (taskRunningLock) {
                timestamp = Timer.getFPGATimestamp();
                for (Loop loop : loops) {
                    loop.onStart(timestamp);
                }
                running = true;
            }
            notifier.startPeriodic(kPeriod);
        }
    }

    public synchronized void stop() {
        if (running) {
            System.out.println("Stopping loops");
            notifier.stop();
            synchronized (taskRunningLock) {
                running = false;
                timestamp = Timer.getFPGATimestamp();
                for (Loop loop : loops) {
                    System.out.println("Stopping " + loop);
                    loop.onStop(timestamp);
                }
            }
        }
    }

    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("looper_dt", dt);
    }
}
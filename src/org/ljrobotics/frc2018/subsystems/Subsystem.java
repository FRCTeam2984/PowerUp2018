package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.loops.Looper;

/**
 * This is the super class for all subsystems. Every subsystem on the robot will extend this class.
 * 
 * The most basic functionality is described in this class. Every subsystem can be stopped and reset.
 * In addition, all of the subsystems will need to execute code periodically which handled by the
 * {@link #registerEnabledLoops} method.
 * 
 * @author Max
 *
 */
public abstract class Subsystem {

	/**
	 * Stops all of the activity in the subsystem.
	 */
	public abstract void stop();
	
	/**
	 * Returns the subsystem to original state. Ie resets sensors and clears accumulators.
	 */
	public abstract void reset();
	
	/**
	 * Called once after subsystem is created. This allows the subsystem to register any loops.
	 * 
	 * @param enabledLooper The looper that will be responsible for calling the subsystems loops.
	 */
	public abstract void registerEnabledLoops(Looper enabledLooper);
	
}

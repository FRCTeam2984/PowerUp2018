package org.ljrobotics.frc2018.loops;

import org.ljrobotics.frc2018.state.GoalTracker;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.vision.VisionUpdate;
import org.ljrobotics.frc2018.vision.VisionUpdateReceiver;

/**
 * This function adds vision updates (from the smartphone) to a list in
 * RobotState. This helps keep track of goals detected by the vision system. The
 * code to determine the best goal to shoot at and prune old Goal tracks is in
 * GoalTracker.java
 * 
 * @see GoalTracker.java
 */
public class VisionProcessor implements Loop, VisionUpdateReceiver {
	private static VisionProcessor instance;
	private VisionUpdate update = null;
	RobotState robotState = RobotState.getInstance();

	public static VisionProcessor getInstance() {
		if (instance == null) {
			instance = new VisionProcessor();
		}
		return instance;
	}

	private VisionProcessor() {

	}

	@Override
	public void onStart(double timestamp) {

	}

	@Override
	public void onLoop(double timestamp) {
		VisionUpdate update;
		synchronized (this) {
			if (this.update == null) {
				return;
			}
			update = this.update;
			this.update = null;
		}
		robotState.addVisionUpdate(update.getCapturedAtTimestamp(), update.getTargets());
	}

	@Override
	public void onStop(double timestamp) {

	}

	@Override
	public synchronized void gotUpdate(VisionUpdate update) {
		this.update = update;
	}

}
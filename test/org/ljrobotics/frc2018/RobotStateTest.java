package org.ljrobotics.frc2018;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.ljrobotics.frc2018.vision.TargetInfo;
import org.ljrobotics.lib.util.math.RigidTransform2d;

public class RobotStateTest {

	private RobotState robotState;
	
	@Before
	public void before() {
		robotState = RobotState.getInstance();
	}
	
	@BeforeEach
	public void beforeEach() {
		robotState.reset(0, RigidTransform2d.identity());
	}
	
	@Test
	public void firstAddVisionUpdateCreatesNewTrack() {
		TargetInfo target = new TargetInfo(100, 0);
		ArrayList<TargetInfo> targets = new ArrayList<TargetInfo>(1);
		targets.add(target);
		this.robotState.addVisionUpdate(1, targets);
	}
	
}

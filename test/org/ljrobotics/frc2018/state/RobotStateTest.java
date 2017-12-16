package org.ljrobotics.frc2018.state;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.state.RobotState;
import org.ljrobotics.frc2018.vision.TargetInfo;
import org.ljrobotics.lib.util.DummyFPGATimer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class RobotStateTest {

	private RobotState robotState;
	private DummyFPGATimer timer;
	
	@Before
	public void before() {
		robotState = RobotState.getInstance();
		this.timer = new DummyFPGATimer();
		Timer.SetImplementation(this.timer);
		
		robotState.reset(0, RigidTransform2d.identity());
	}
	
	@Test
	public void firstVisionUpdateSpecifiesLocation() {
		setCameraConstants(0,0,0,0);
		TargetInfo target = new TargetInfo(100, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		this.robotState.addVisionUpdate(1, targets);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		RigidTransform2d expectedTransform = createExpected(100, 0, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	@Test
	public void secondVisionUpdateUpdatesLocation() {
		setCameraConstants(0,0,0,0);
		TargetInfo target = new TargetInfo(100, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		
		TargetInfo target2 = new TargetInfo(101, 0);
		ArrayList<TargetInfo> targets2 = new ArrayList<>(1);
		targets2.add(target2);
		this.robotState.addVisionUpdate(1, targets);
		this.robotState.addVisionUpdate(1 + 1D/30D, targets2);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		RigidTransform2d expectedTransform = createExpected(100.5, 0, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	@Test
	public void nonsequeterSetOfTargetsIgnored() {
		setCameraConstants(0,0,0,0);
		TargetInfo target = new TargetInfo(10, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		
		TargetInfo target2 = new TargetInfo(101, 20);
		ArrayList<TargetInfo> targets2 = new ArrayList<>(1);
		targets2.add(target2);
		
		TargetInfo target3 = new TargetInfo(100, 20);
		ArrayList<TargetInfo> targets3 = new ArrayList<>(1);
		targets3.add(target3);
		this.robotState.addVisionUpdate(1, targets);
		this.robotState.addVisionUpdate(1 + 1D/30D, targets2);
		this.robotState.addVisionUpdate(1 + 2D/30D, targets3);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		RigidTransform2d expectedTransform = createExpected(10, 0, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	@Test
	public void robotRotationRotatesTarget() {
		setCameraConstants(0,0,0,0);
		this.robotState.reset(0, createExpected(0,0,90));
		TargetInfo target = new TargetInfo(100, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		
		this.robotState.addVisionUpdate(1, targets);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		RigidTransform2d expectedTransform = createExpected(0, 100, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	@Test
	public void robotTransformTransformsTarget() {
		setCameraConstants(0,0,0,0);
		this.robotState.reset(0, createExpected(100,-40,45));
		TargetInfo target = new TargetInfo(10, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		
		this.robotState.addVisionUpdate(1, targets);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		RigidTransform2d expectedTransform = createExpected(107.07106, -40+7.07106, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	private void setCameraConstants(double x, double y, double pitch, double yaw) {
		Constants.CAMERA_X_OFFSET = x;
		Constants.CAMEAR_Y_OFFSET = y;
		Constants.CAMERA_PITCH = pitch;
		Constants.CAMERA_YAW = yaw;
	}
	
	private RigidTransform2d createExpected(double x, double y, double rot) {
		Translation2d expectedPos = new Translation2d(x, y);
		Rotation2d expedtedRotation = Rotation2d.fromDegrees(rot);
		return new RigidTransform2d(expectedPos, expedtedRotation);
	}
	
}

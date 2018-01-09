package org.ljrobotics.frc2018.state;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.vision.TargetInfo;
import org.ljrobotics.lib.util.DummyFPGATimer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;
import org.ljrobotics.lib.util.math.Twist2d;

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
	
	@Test
	public void yawOffsetActsAsRotation() {
		setCameraConstants(0,0,0,10);
		this.robotState.reset(0, createExpected(0,0,0));
		TargetInfo target = new TargetInfo(10, 0);
		ArrayList<TargetInfo> targets = new ArrayList<>(1);
		targets.add(target);
		
		this.robotState.addVisionUpdate(1, targets);
		assertEquals(1, this.robotState.getCaptureTimeFieldToGoal().size());
		RigidTransform2d actualTransform = this.robotState.getCaptureTimeFieldToGoal().get(0);
		
		double radians = Math.toRadians(10);
		RigidTransform2d expectedTransform = createExpected(Math.cos(radians)*10, Math.sin(radians)*10, 0);
		assertEquals(expectedTransform, actualTransform);
	}
	
	@Test
	public void generateOdometryFromSensorsReturnsZeroTwistWhenNoMotion() {
		Twist2d expected = new Twist2d(0,0,0);
		Twist2d actual = this.robotState.generateOdometryFromSensors(0, 0, new Rotation2d());
		assertEquals(expected, actual);
	}
	
	@Test
	public void generateOdometryFromSensorsReturnsStaightForwardTwistWhenMoveingForward() {
		Twist2d expected = new Twist2d(1,0,0);
		Twist2d actual = this.robotState.generateOdometryFromSensors(1, 1, new Rotation2d());
		assertEquals(expected, actual);
		assertEquals(1, this.robotState.getDistanceDriven(), 0.00001);
	}
	
	@Test
	public void generateOdometryFromSensorsReturnsSpinTwistWhenMoveingInCircle() {
		Twist2d expected = new Twist2d(0,0,1);
		Twist2d actual = this.robotState.generateOdometryFromSensors(1, -1, Rotation2d.fromRadians(1));
		assertEquals(expected, actual);
		assertEquals(0, this.robotState.getDistanceDriven(), 0.00001);
	}
	
	@Test
	public void generateOdometryFromSensorsReturnsCurveAfterSecondCurvingMotion() {
		Twist2d expected = new Twist2d(0.5,0,1);
		this.robotState.addFieldToVehicleObservation(0, RigidTransform2d.fromRotation(Rotation2d.fromRadians(1)));
		Twist2d actual = this.robotState.generateOdometryFromSensors(1, 0, Rotation2d.fromRadians(2));
		assertEquals(expected, actual);
		assertEquals(0.5, this.robotState.getDistanceDriven(), 0.00001);
	}
	
	@Test
	public void getPredictedFieldToVehicleExtrapolatesLastMotion() {
		RigidTransform2d expected = RigidTransform2d.fromTranslation(new Translation2d(1,0));
		this.robotState.addObservations(0, new Twist2d(0,0,0), new Twist2d(1,0,0));
		RigidTransform2d actual = this.robotState.getPredictedFieldToVehicle(1);
		assertEquals(expected, actual);
	}
	
	private void setCameraConstants(double x, double y, double pitch, double yaw) {
		Constants.CAMERA_X_OFFSET = x;
		Constants.CAMERA_Y_OFFSET = y;
		Constants.CAMERA_YAW = yaw;
	}
	
	private RigidTransform2d createExpected(double x, double y, double rot) {
		Translation2d expectedPos = new Translation2d(x, y);
		Rotation2d expedtedRotation = Rotation2d.fromDegrees(rot);
		return new RigidTransform2d(expectedPos, expedtedRotation);
	}
	
}

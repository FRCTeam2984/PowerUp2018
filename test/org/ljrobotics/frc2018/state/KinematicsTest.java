package org.ljrobotics.frc2018.state;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.state.Kinematics.DriveVelocity;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;
import org.ljrobotics.lib.util.math.Twist2d;

/**
 * This is a tester for the kinematics solver. There are a few acronyms used to
 * make the names shorter:
 * <ul>
 * <li>ex/im = explicit / Implicit</li>
 * <li>Rel/Abs = relative / Absolute</li>
 * <li>FK = Forward Kinematics</li>
 * </ul>
 * 
 * @author Max
 *
 */
public class KinematicsTest {

	@Test
	public void exRelFKGivesZeroMotionWithAllZeroInputs() {
		Twist2d expected = new Twist2d(0, 0, 0);
		Twist2d calculated = Kinematics.forwardKinematics(0,0,0);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void exRelFKGivesStraightForwardMotionWhenNoGyroInput() {
		Twist2d expected = new Twist2d(1, 0, 0);
		Twist2d calculated = Kinematics.forwardKinematics(1,1,0);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void exRelFKGivesStraightForwardMotionWhenNoGyroInputAndUnequalLeftAndRight() {
		Twist2d expected = new Twist2d(1, 0, 0);
		Twist2d calculated = Kinematics.forwardKinematics(1.1,0.9,0);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void exRelFKGivesNoMotionForwardWhenTurningInPlace() {
		Twist2d expected = new Twist2d(0, 0, 1);
		Twist2d calculated = Kinematics.forwardKinematics(1, -1,1);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void exAbsFKGivesChangeInOnlyHeadingWhenStandingStill() {
		Twist2d expected = new Twist2d(0, 0, 1);
		Rotation2d start = Rotation2d.fromRadians(1);
		Rotation2d end = Rotation2d.fromRadians(2);
		Twist2d calculated = Kinematics.forwardKinematics(start, 1, -1, end);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void exAbsFKGivesChangeInOnlyDisplacementWhenDrivingStraight() {
		Twist2d expected = new Twist2d(-1, 0, 0);
		Rotation2d start = Rotation2d.fromRadians(-1);
		Rotation2d end = Rotation2d.fromRadians(-1);
		Twist2d calculated = Kinematics.forwardKinematics(start, -1, -1, end);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void imRelFKGivesChangeInOnlyDisplacementWhenDrivingStraight() {
		Twist2d expected = new Twist2d(2, 0, 0);
		Twist2d calculated = Kinematics.forwardKinematics(2, 2);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void imRelFKGivesChangeInBothDisplacementAndRotationWhenCircleing() {
		Constants.TRACK_SCRUB_FACTOR = 0.9;
		Constants.TRACK_WIDTH_INCHES = 1;
		Twist2d expected = new Twist2d(0.5, 0, -0.9);
		Twist2d calculated = Kinematics.forwardKinematics(1, 0);
		assertEquals(expected, calculated);
	}
	
	@Test
	public void intergratedFKGivesNewLocationStraightAheadWhenNotTurning() {
		RigidTransform2d start = RigidTransform2d.fromRotation(Rotation2d.fromDegrees(45));
		Translation2d endPos = new Translation2d(1,1);
		Rotation2d endRot = Rotation2d.fromDegrees(45);
		RigidTransform2d end = new RigidTransform2d(endPos, endRot);
		RigidTransform2d calculated = Kinematics.integrateForwardKinematics(start, 1.41421, 1.41421, endRot);
		assertEquals(end, calculated);
	}
	
	@Test
	public void inverseKinematicsGivesStaightDriveVelocityFromNoCurvatureTwist2d() {
		Twist2d vel = new Twist2d(1, 0, 0);
		DriveVelocity actual = Kinematics.inverseKinematics(vel);
		assertEquals(1, actual.left, 0.00001);
		assertEquals(1, actual.right, 0.00001);
	}
	
	@Test
	public void inverseKinematicsGivesCurvedDriveVelocityFromCurvedTwist2d() {
		Twist2d vel = new Twist2d(-1, 0, -1);
		DriveVelocity actual = Kinematics.inverseKinematics(vel);
		assertEquals(1, actual.left, 0.00001);
		assertEquals(1, actual.right, 0.00001);
	}

}

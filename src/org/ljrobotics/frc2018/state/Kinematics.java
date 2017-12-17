package org.ljrobotics.frc2018.state;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Twist2d;

/**
 * Provides forward and inverse kinematics equations for the robot modeling the
 * wheelbase as a differential drive (with a corrective factor to account for
 * skidding).
 */
public class Kinematics {
    private static final double EPSILON = 1E-9;

    /**
     * Forward kinematics using only encoders, rotation is implicit (less accurate than below, but useful for predicting
     * motion)
     */
    public static Twist2d forwardKinematics(double leftWheelDelta, double rightWheelDelta) {
        double deltaV = (rightWheelDelta - leftWheelDelta) / 2 * Constants.TRACK_SCRUB_FACTOR;
        double deltaRotation = deltaV * 2 / Constants.TRACK_WIDTH_INCHES;
        return forwardKinematics(leftWheelDelta, rightWheelDelta, deltaRotation);
    }

    /**
     * Forward kinematics using encoders and explicitly measured rotation (ex. from gyro)
     */
    public static Twist2d forwardKinematics(double leftWheelDelta, double rightWheelDelta,
            double deltaRotationRads) {
        final double dx = (leftWheelDelta + rightWheelDelta) / 2.0;
        return new Twist2d(dx, 0, deltaRotationRads);
    }

    /**
     * For convenience, forward kinematic with an absolute rotation and previous rotation.
     */
    public static Twist2d forwardKinematics(Rotation2d prevHeading, double leftWheelDelta, double rightWheelDelta,
            Rotation2d currentHeading) {
        return forwardKinematics(leftWheelDelta, rightWheelDelta,
                prevHeading.inverse().rotateBy(currentHeading).getRadians());
    }

    /** Append the result of forward kinematics to a previous pose. */
    public static RigidTransform2d integrateForwardKinematics(RigidTransform2d currentPose, double leftWheelDelta,
            double rightWheelDelta, Rotation2d currentHeading) {
        Twist2d withGyro = forwardKinematics(currentPose.getRotation(), leftWheelDelta, rightWheelDelta,
                currentHeading);
        return integrateForwardKinematics(currentPose, withGyro);
    }

    /**
     * For convenience, integrate forward kinematics with a Twist2d and previous rotation.
     */
    public static RigidTransform2d integrateForwardKinematics(RigidTransform2d currentPose,
            Twist2d forwardKinematics) {
        return currentPose.transformBy(RigidTransform2d.exp(forwardKinematics));
    }

    /**
     * Class that contains left and right wheel velocities
     */
    public static class DriveVelocity {
        public final double left;
        public final double right;

        public DriveVelocity(double left, double right) {
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Uses inverse kinematics to convert a Twist2d into left and right wheel velocities
     */
    public static DriveVelocity inverseKinematics(Twist2d velocity) {
        if (Math.abs(velocity.dtheta) < EPSILON) {
            return new DriveVelocity(velocity.dx, velocity.dx);
        }
        double deltaV = Constants.TRACK_WIDTH_INCHES * velocity.dtheta / (2 * Constants.TRACK_SCRUB_FACTOR);
        return new DriveVelocity(velocity.dx - deltaV, velocity.dx + deltaV);
    }
}
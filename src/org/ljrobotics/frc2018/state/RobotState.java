package org.ljrobotics.frc2018.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.vision.TargetInfo;
import org.ljrobotics.lib.util.InterpolatingDouble;
import org.ljrobotics.lib.util.InterpolatingTreeMap;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;
import org.ljrobotics.lib.util.math.Twist2d;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * RobotState keeps track of the poses of various coordinate frames throughout
 * the match. A coordinate frame is simply a point and direction in space that
 * defines an (x,y) coordinate system. Transforms (or poses) keep track of the
 * spatial relationship between different frames.
 *
 * Robot frames of interest (from parent to child):
 *
 * 1. Field frame: origin is where the robot is turned on
 *
 * 2. Vehicle frame: origin is the center of the robot wheelbase, facing
 * forwards
 *
 * 3. Camera frame: origin is the center of the camera imager relative to the
 * robot base.
 *
 * 4. Goal frame: origin is the center of the boiler (note that orientation in
 * this frame is arbitrary). Also note that there can be multiple goal frames.
 *
 * As a kinematic chain with 4 frames, there are 3 transforms of interest:
 *
 * 1. Field-to-vehicle: This is tracked over time by integrating encoder and
 * gyro measurements. It will inevitably drift, but is usually accurate over
 * short time periods.
 *
 * 2. Vehicle-to-camera: This is a constant.
 *
 * 3. Camera-to-goal: This is a pure translation, and is measured by the vision
 * system.
 */

public class RobotState {
    private static RobotState instance = new RobotState();

    public static RobotState getInstance() {
        return instance;
    }

    private static final int OBSERVATION_BUFFER_SIZE = 100;

    private static final RigidTransform2d VEHICLE_TO_CAMERA = new RigidTransform2d(
            new Translation2d(Constants.CAMERA_X_OFFSET, Constants.CAMERA_Y_OFFSET), new Rotation2d());

    // FPGATimestamp -> RigidTransform2d or Rotation2d
    private InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> fieldToVehicle;
    private Twist2d vehicleVelocityPredicted;
    private Twist2d vehicleVelocityMeasured;
    private double distanceDriven;
    private GoalTracker goalTracker;

    private RobotState() {
        reset(0, new RigidTransform2d());
    }

    /**
     * Resets the field to robot transform (robot's position on the field)
     */
    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle) {
        fieldToVehicle = new InterpolatingTreeMap<>(OBSERVATION_BUFFER_SIZE);
        fieldToVehicle.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
        vehicleVelocityPredicted = Twist2d.identity();
        vehicleVelocityMeasured = Twist2d.identity();
        distanceDriven = 0.0;
        goalTracker = new GoalTracker();
    }

    public synchronized void resetDistanceDriven() {
        distanceDriven = 0.0;
    }

    /**
     * Returns the robot's position on the field at a certain time. Linearly interpolates between stored robot positions
     * to fill in the gaps.
     */
    public synchronized RigidTransform2d getFieldToVehicle(double timestamp) {
        return fieldToVehicle.getInterpolated(new InterpolatingDouble(timestamp));
    }
    
    public synchronized RigidTransform2d getFieldToCamera(double timestamp) {
        return getFieldToVehicle(timestamp).transformBy(VEHICLE_TO_CAMERA);
    }
    
    public synchronized List<RigidTransform2d> getCaptureTimeFieldToGoal() {
        List<RigidTransform2d> rv = new ArrayList<>();
        for (TrackReport report : goalTracker.getTracks()) {
            rv.add(RigidTransform2d.fromTranslation(report.fieldToGoal));
        }
        return rv;
    }

    public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
        return fieldToVehicle.lastEntry();
    }

    public synchronized RigidTransform2d getPredictedFieldToVehicle(double lookahead_time) {
        return getLatestFieldToVehicle().getValue()
                .transformBy(RigidTransform2d.exp(vehicleVelocityPredicted.scaled(lookahead_time)));
    }

    public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
        fieldToVehicle.put(new InterpolatingDouble(timestamp), observation);
    }

    public synchronized void addObservations(double timestamp, Twist2d measured_velocity,
            Twist2d predicted_velocity) {
        addFieldToVehicleObservation(timestamp,
                Kinematics.integrateForwardKinematics(getLatestFieldToVehicle().getValue(), measured_velocity));
        vehicleVelocityMeasured = measured_velocity;
        vehicleVelocityPredicted = predicted_velocity;
    }

    public synchronized Twist2d generateOdometryFromSensors(double left_encoder_delta_distance,
            double right_encoder_delta_distance, Rotation2d current_gyro_angle) {
        final RigidTransform2d last_measurement = getLatestFieldToVehicle().getValue();
        final Twist2d delta = Kinematics.forwardKinematics(last_measurement.getRotation(),
                left_encoder_delta_distance, right_encoder_delta_distance, current_gyro_angle);
        distanceDriven += delta.dx;
        return delta;
    }

    public synchronized double getDistanceDriven() {
        return distanceDriven;
    }

    public synchronized Twist2d getPredictedVelocity() {
        return vehicleVelocityPredicted;
    }

    public synchronized Twist2d getMeasuredVelocity() {
        return vehicleVelocityMeasured;
    }

    public void addVisionUpdate(double timestamp, List<TargetInfo> vision_update) {
        List<Translation2d> field_to_goals = new ArrayList<>();
        RigidTransform2d field_to_camera = getFieldToCamera(timestamp);
        if (!(vision_update == null || vision_update.isEmpty())) {
            for (TargetInfo target : vision_update) {
            	
				double distance = target.getDistance();
				double yaw = target.getRotation();
				
				//Correct for camera yaw
				yaw += Constants.CAMERA_YAW;
				
				Rotation2d angle = Rotation2d.fromDegrees(yaw);
				field_to_goals.add(field_to_camera
						.transformBy(RigidTransform2d
								.fromTranslation(new Translation2d(distance * angle.cos(), distance * angle.sin())))
						.getTranslation());
            }
        }
        synchronized (this) {
            goalTracker.update(timestamp, field_to_goals);
        }
    }
    
    public void outputToSmartDashboard() {
        RigidTransform2d odometry = getLatestFieldToVehicle().getValue();
        SmartDashboard.putNumber("robot pose x", odometry.getTranslation().x());
        SmartDashboard.putNumber("robot pose y", odometry.getTranslation().y());
        SmartDashboard.putNumber("robot pose theta", odometry.getRotation().getDegrees());
        SmartDashboard.putNumber("robot velocity", vehicleVelocityMeasured.dx);
        List<RigidTransform2d> poses = getCaptureTimeFieldToGoal();
        for (RigidTransform2d pose : poses) {
            // Only output first goal
            SmartDashboard.putNumber("goal pose x", pose.getTranslation().x());
            SmartDashboard.putNumber("goal pose y", pose.getTranslation().y());
            break;
        }
    }
}
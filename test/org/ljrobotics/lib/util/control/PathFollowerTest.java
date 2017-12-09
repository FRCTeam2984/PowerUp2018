package org.ljrobotics.lib.util.control;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.lib.util.ReflectingCSVWriter;
import org.ljrobotics.lib.util.control.PathFollower;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;
import org.ljrobotics.lib.util.math.Twist2d;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;

public class PathFollowerTest {

    static final PathFollower.Parameters kParameters = new PathFollower.Parameters(
            new Lookahead(16.0, 16.0, 0.0, 120.0),
            0.0, // Inertia gain
            0.75, // Profile kp
            0.03, // Profile ki
            0.02, // Profile kv
            1.0, // Profile kffv
            0.0, // Profile kffa
            Constants.PATH_FOLLOWING_MAX_VEL, // Profile max abs vel
            Constants.PATH_FOLLOWING_MAX_ACCEL, // Profile max abs accel
            Constants.PATH_FOLLWOING_GOAL_POS_TOLERANCE,
            Constants.PATH_FOLLOWING_GOAL_VEL_TOLERANCE,
            Constants.PATH_STOP_STEERING_DISTANCE
    );

    private class StartToBoilerGearRed implements PathContainer {

    	@Override
        public Path buildPath() {
    		ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
            sWaypoints.add(new Waypoint(300,50,0,0));
            sWaypoints.add(new Waypoint(360,140,40,60));
            sWaypoints.add(new Waypoint(280,250,10,60));
            sWaypoints.add(new Waypoint(300,270,0,0));

            return PathBuilder.buildPathFromWaypoints(sWaypoints);
        }
        
        @Override
        public RigidTransform2d getStartPose() {
            return new RigidTransform2d(new Translation2d(300, 50), Rotation2d.fromDegrees(0)); 
        }

		@Override
		public boolean isReversed() {
			// TODO Auto-generated method stub
			return false;
		}
    	
    }
    
    @Test
    public void testStartToBoilerGearRed() {
        PathContainer container = new StartToBoilerGearRed();
        PathFollower controller = new PathFollower(container.buildPath(), container.isReversed(), kParameters);

        ReflectingCSVWriter<PathFollower.DebugOutput> writer = new ReflectingCSVWriter<PathFollower.DebugOutput>(
                "temp.csv", PathFollower.DebugOutput.class);

        final double dt = 0.01;

        RigidTransform2d robot_pose = container.getStartPose();
        double t = 0;
        double displacement = 0.0;
        double velocity = 0.0;
        while (!controller.isFinished() && t < 10.0) {
            // Follow the path
            Twist2d command = controller.update(t, robot_pose, displacement, velocity);
            writer.add(controller.getDebug());
            robot_pose = robot_pose.transformBy(RigidTransform2d.exp(command.scaled(dt)));

            t += dt;
            final double prev_vel = velocity;
            velocity = command.dx;
            displacement += velocity * dt;

            System.out.println("t = " + t + ", displacement " + displacement + ", lin vel " + command.dx + ", lin acc "
                    + (velocity - prev_vel) / dt + ", ang vel " + command.dtheta + ", pose " + robot_pose + ", CTE "
                    + controller.getCrossTrackError() + ", ATE " + controller.getAlongTrackError());
        }
        writer.flush();
        System.out.println(robot_pose);
        assertTrue(controller.isFinished());
        assertTrue(controller.getAlongTrackError() < 1.0);
        assertTrue(controller.getCrossTrackError() < 1.0);
    }

}
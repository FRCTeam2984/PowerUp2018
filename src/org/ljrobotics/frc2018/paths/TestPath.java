package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class TestPath implements PathContainer {

	@Override
	public Path buildPath() {
		ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
		sWaypoints.add(new Waypoint(0, 0, 0, 0));
		sWaypoints.add(new Waypoint(90, 0, 60, 60));
		sWaypoints.add(new Waypoint(155, 109, 60, 60));
		sWaypoints.add(new Waypoint(227, 141, 0, 60));
		sWaypoints.add(new Waypoint(245, 149, 0, 45));
		
//		sWaypoints.add(new Waypoint(0, 0, 0, 60));
//		sWaypoints.add(new Waypoint(80, 0, 0, 60));
		
//		sWaypoints.add(new Waypoint(0,0,0,60));
//        sWaypoints.add(new Waypoint(50,0,35,60));
//        sWaypoints.add(new Waypoint(100,50,35,60));
//        sWaypoints.add(new Waypoint(50,100,35,60));
//        sWaypoints.add(new Waypoint(0,50,35,60));
//        sWaypoints.add(new Waypoint(50,0,35,60));
//        sWaypoints.add(new Waypoint(100,0,0,60));

		return PathBuilder.buildPathFromWaypoints(sWaypoints);
	}

	@Override
	public RigidTransform2d getStartPose() {
		return new RigidTransform2d(new Translation2d(0, 0), Rotation2d.fromDegrees(0));
	}

	@Override
	public boolean isReversed() {
		// TODO Auto-generated method stub
		return false;
	}

}

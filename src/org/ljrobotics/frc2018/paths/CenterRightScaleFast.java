package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class CenterRightScaleFast implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,155,0,0));
        sWaypoints.add(new Waypoint(50,155,20,120));
        sWaypoints.add(new Waypoint(80,110,0,120));
        sWaypoints.add(new Waypoint(110,75,40,120));
        sWaypoints.add(new Waypoint(180,45,40,120));
        sWaypoints.add(new Waypoint(260,45,0,120));
        sWaypoints.add(new Waypoint(300,45,0,120));
        sWaypoints.add(new Waypoint(335,45,0,30));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(20, 155), Rotation2d.fromDegrees(0.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }
	
}
package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class CenterRightScale implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,155,0,0));
        sWaypoints.add(new Waypoint(50,155,20,60));
        sWaypoints.add(new Waypoint(80,110,0,60));
        sWaypoints.add(new Waypoint(110,75,40,60));
        sWaypoints.add(new Waypoint(180,25,40,60));
        sWaypoints.add(new Waypoint(260,25,0,60));
        sWaypoints.add(new Waypoint(300,25,0,60));
        sWaypoints.add(new Waypoint(325,25,15,30));
        sWaypoints.add(new Waypoint(325,55,0,20));

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
package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class LeftRightScale implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,240,0,0));
        sWaypoints.add(new Waypoint(50,240,20,60));
        sWaypoints.add(new Waypoint(100,260,20,60));
        sWaypoints.add(new Waypoint(190,260,0,60));
        sWaypoints.add(new Waypoint(240,260,40,60));
        sWaypoints.add(new Waypoint(240,190,0,60));
        sWaypoints.add(new Waypoint(240,160,0,60));
        sWaypoints.add(new Waypoint(240,100,0,60));
        sWaypoints.add(new Waypoint(240,50,40,60));
        sWaypoints.add(new Waypoint(290,50,0,60));
        sWaypoints.add(new Waypoint(335,50,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(20, 240), Rotation2d.fromDegrees(0.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }
}
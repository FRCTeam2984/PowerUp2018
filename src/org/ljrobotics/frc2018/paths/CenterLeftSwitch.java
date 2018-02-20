package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class CenterLeftSwitch implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,155,0,0));
        sWaypoints.add(new Waypoint(45,155,10,20));
        sWaypoints.add(new Waypoint(80,220,10,30));
        sWaypoints.add(new Waypoint(100,220,0,30));
        sWaypoints.add(new Waypoint(113,220,0,20));

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
	// WAYPOINT_DATA: [{"position":{"x":20,"y":250},"speed":0,"radius":0,"comment":""},{"position":{"x":35,"y":250},"speed":30,"radius":10,"comment":""},{"position":{"x":80,"y":220},"speed":40,"radius":20,"comment":""},{"position":{"x":105,"y":220},"speed":30,"radius":0,"comment":""},{"position":{"x":113,"y":220},"speed":203,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: UntitledPath
}
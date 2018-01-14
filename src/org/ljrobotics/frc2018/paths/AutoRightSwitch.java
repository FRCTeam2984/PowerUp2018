package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class AutoRightSwitch implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(0,155,0,0));
        sWaypoints.add(new Waypoint(40,155,40,60));
        sWaypoints.add(new Waypoint(80,20,40,60));
        sWaypoints.add(new Waypoint(160,30,40,60));
        sWaypoints.add(new Waypoint(170,80,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(0, 155), Rotation2d.fromDegrees(180.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }
	// WAYPOINT_DATA: [{"position":{"x":0,"y":155},"speed":0,"radius":0,"comment":""},{"position":{"x":40,"y":155},"speed":60,"radius":40,"comment":""},{"position":{"x":80,"y":20},"speed":60,"radius":40,"comment":""},{"position":{"x":160,"y":30},"speed":60,"radius":40,"comment":""},{"position":{"x":170,"y":80},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: AutoRightSwitch
}
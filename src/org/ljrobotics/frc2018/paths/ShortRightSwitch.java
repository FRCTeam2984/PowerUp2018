package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class ShortRightSwitch implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,155,0,0));
        sWaypoints.add(new Waypoint(35,155,10,60));
        sWaypoints.add(new Waypoint(80,110,20,60));
        sWaypoints.add(new Waypoint(105,110,0,60));
        sWaypoints.add(new Waypoint(110,110,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(0, 155), Rotation2d.fromDegrees(0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }
	// WAYPOINT_DATA: [{"position":{"x":0,"y":155},"speed":60,"radius":0,"comment":""},{"position":{"x":40,"y":155},"speed":60,"radius":30,"comment":""},{"position":{"x":80,"y":110},"speed":60,"radius":30,"comment":""},{"position":{"x":140,"y":110},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: ShortRightSwitch
}
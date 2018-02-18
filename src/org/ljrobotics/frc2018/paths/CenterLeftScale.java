package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class CenterLeftScale implements PathContainer {

    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,155,0,0));
        sWaypoints.add(new Waypoint(50,155,20,60));
        sWaypoints.add(new Waypoint(80,200,0,60));
        sWaypoints.add(new Waypoint(110,245,40,60));
        sWaypoints.add(new Waypoint(180,270,40,60));
        sWaypoints.add(new Waypoint(260,270,0,60));
        sWaypoints.add(new Waypoint(300,270,0,60));
        sWaypoints.add(new Waypoint(335,270,0,30));

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
	// WAYPOINT_DATA: [{"position":{"x":20,"y":155},"speed":0,"radius":0,"comment":""},{"position":{"x":50,"y":155},"speed":20,"radius":20,"comment":""},{"position":{"x":80,"y":110},"speed":40,"radius":0,"comment":""},{"position":{"x":110,"y":65},"speed":60,"radius":40,"comment":""},{"position":{"x":180,"y":40},"speed":60,"radius":40,"comment":""},{"position":{"x":260,"y":40},"speed":60,"radius":0,"comment":""},{"position":{"x":300,"y":40},"speed":60,"radius":0,"comment":""},{"position":{"x":325,"y":40},"speed":30,"radius":20,"comment":""},{"position":{"x":325,"y":60},"speed":20,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: ShortRightSwitch
}
package org.ljrobotics.frc2018.paths;

import java.util.ArrayList;

import org.ljrobotics.lib.util.control.Path;
import org.ljrobotics.lib.util.control.PathBuilder;
import org.ljrobotics.lib.util.control.PathBuilder.Waypoint;
import org.ljrobotics.lib.util.control.PathContainer;
import org.ljrobotics.lib.util.math.RigidTransform2d;
import org.ljrobotics.lib.util.math.Rotation2d;
import org.ljrobotics.lib.util.math.Translation2d;

public class RobotFlewAroundMyRoomBeforeYouCame implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(50,50,0,0));
        sWaypoints.add(new Waypoint(70,70,0,60));
        sWaypoints.add(new Waypoint(90,90,0,60));
        sWaypoints.add(new Waypoint(110,110,0,60));
        sWaypoints.add(new Waypoint(130,120,0,60));
        sWaypoints.add(new Waypoint(150,150,0,60));
        sWaypoints.add(new Waypoint(170,170,0,60));
        sWaypoints.add(new Waypoint(190,30,0,60));
        sWaypoints.add(new Waypoint(210,210,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(50, 50), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":50,"y":50},"speed":0,"radius":0,"comment":""},{"position":{"x":70,"y":70},"speed":60,"radius":0,"comment":""},{"position":{"x":90,"y":90},"speed":60,"radius":0,"comment":""},{"position":{"x":110,"y":110},"speed":60,"radius":0,"comment":""},{"position":{"x":130,"y":120},"speed":60,"radius":0,"comment":""},{"position":{"x":150,"y":150},"speed":60,"radius":0,"comment":""},{"position":{"x":170,"y":170},"speed":60,"radius":0,"comment":""},{"position":{"x":190,"y":30},"speed":60,"radius":0,"comment":""},{"position":{"x":210,"y":210},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: UntitledPath
}
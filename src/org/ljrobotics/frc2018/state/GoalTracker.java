package org.ljrobotics.frc2018.state;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ljrobotics.lib.util.math.Translation2d;


/**
 * This is used in the event that multiple goals are detected to judge all goals based on timestamp, stability, and
 * continuation of previous goals (i.e. if a goal was detected earlier and has changed locations). This allows the robot
 * to make consistent decisions about which goal to aim at and to smooth out jitter from vibration of the camera.
 * 
 * @see GoalTrack.java
 */
public class GoalTracker {
    List<GoalTrack> mCurrentTracks = new ArrayList<>();
    int mNextId = 0;

    public GoalTracker() {
    }

    public void reset() {
        mCurrentTracks.clear();
    }

    public void update(double timestamp, List<Translation2d> field_to_goals) {
        // Try to update existing tracks
        for (Translation2d target : field_to_goals) {
            boolean hasUpdatedTrack = false;
            for (GoalTrack track : mCurrentTracks) {
                if (!hasUpdatedTrack) {
                    if (track.tryUpdate(timestamp, target)) {
                        hasUpdatedTrack = true;
                    }
                } else {
                    track.emptyUpdate();
                }
            }
        }
        // Prune any tracks that have died
        for (Iterator<GoalTrack> it = mCurrentTracks.iterator(); it.hasNext();) {
            GoalTrack track = it.next();
            if (!track.isAlive()) {
                it.remove();
            }
        }
        // If all tracks are dead, start new tracks for any detections
        if (mCurrentTracks.isEmpty()) {
            for (Translation2d target : field_to_goals) {
                mCurrentTracks.add(GoalTrack.makeNewTrack(timestamp, target, mNextId));
                ++mNextId;
            }
        }
    }

    public boolean hasTracks() {
        return !mCurrentTracks.isEmpty();
    }

    public List<TrackReport> getTracks() {
        List<TrackReport> rv = new ArrayList<>();
        for (GoalTrack track : mCurrentTracks) {
            rv.add(new TrackReport(track));
        }
        return rv;
    }
}
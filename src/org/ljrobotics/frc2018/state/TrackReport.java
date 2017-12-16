package org.ljrobotics.frc2018.state;

import org.ljrobotics.lib.util.math.Translation2d;

/**
 * Track reports contain all of the relevant information about a given goal track.
 */
public class TrackReport {
    // Translation from the field frame to the goal
    public Translation2d field_to_goal;

    // The timestamp of the latest time that the goal has been observed
    public double latest_timestamp;

    // The percentage of the goal tracking time during which this goal has
    // been observed (0 to 1)
    public double stability;

    // The track id
    public int id;

    public TrackReport(GoalTrack track) {
        this.field_to_goal = track.getSmoothedPosition();
        this.latest_timestamp = track.getLatestTimestamp();
        this.stability = track.getStability();
        this.id = track.getId();
    }
}
package org.ljrobotics.frc2018.state;

import java.util.Comparator;

import org.ljrobotics.frc2018.Constants;

/**
 * TrackReportComparators are used in the case that multiple tracks are active
 * (e.g. we see or have recently seen multiple goals). They contain heuristics
 * used to pick which track we should aim at by calculating a score for each
 * track (highest score wins).
 */
public class TrackReportComparator implements Comparator<TrackReport> {
    // Reward tracks for being more stable (seen in more frames)
    double stabilityWeight;
    // Reward tracks for being recently observed
    double ageWeight;
    double currentTimestamp;
    // Reward tracks for being continuations of tracks that we are already
    // tracking
    double switchingWeight;
    int lastTrackId;

    public TrackReportComparator(double stabilityWeight, double ageWeight, double switchingWeight,
            int lastTrackid, double currentTimestamp) {
        this.stabilityWeight = stabilityWeight;
        this.ageWeight = ageWeight;
        this.switchingWeight = switchingWeight;
        this.lastTrackId = lastTrackid;
        this.currentTimestamp = currentTimestamp;
    }

    double score(TrackReport report) {
        double stabilityScore = stabilityWeight * report.stability;
        double ageScore = ageWeight
                * Math.max(0, (Constants.MAX_GOAL_TRACK_AGE - (currentTimestamp - report.latestTimestamp))
                        / Constants.MAX_GOAL_TRACK_AGE);
        double switchingScore = (report.id == lastTrackId ? switchingWeight : 0);
        return stabilityScore + ageScore + switchingScore;
    }

    @Override
    public int compare(TrackReport o1, TrackReport o2) {
        double diff = score(o1) - score(o2);
        // Greater than 0 if o1 is better than o2
        if (diff < 0) {
            return 1;
        } else if (diff > 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
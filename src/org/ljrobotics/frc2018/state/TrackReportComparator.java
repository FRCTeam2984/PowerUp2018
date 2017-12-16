package org.ljrobotics.frc2018.state;

import java.util.Comparator;

import org.ljrobotics.frc2018.Constants;

/**
 * TrackReportComparators are used in the case that multiple tracks are active (e.g. we see or have recently seen
 * multiple goals). They contain heuristics used to pick which track we should aim at by calculating a score for
 * each track (highest score wins).
 */
public class TrackReportComparator implements Comparator<TrackReport> {
    // Reward tracks for being more stable (seen in more frames)
    double mStabilityWeight;
    // Reward tracks for being recently observed
    double mAgeWeight;
    double mCurrentTimestamp;
    // Reward tracks for being continuations of tracks that we are already
    // tracking
    double mSwitchingWeight;
    int mLastTrackId;

    public TrackReportComparator(double stability_weight, double age_weight, double switching_weight,
            int last_track_id, double current_timestamp) {
        this.mStabilityWeight = stability_weight;
        this.mAgeWeight = age_weight;
        this.mSwitchingWeight = switching_weight;
        this.mLastTrackId = last_track_id;
        this.mCurrentTimestamp = current_timestamp;
    }

    double score(TrackReport report) {
        double stability_score = mStabilityWeight * report.stability;
        double age_score = mAgeWeight
                * Math.max(0, (Constants.kMaxGoalTrackAge - (mCurrentTimestamp - report.latest_timestamp))
                        / Constants.kMaxGoalTrackAge);
        double switching_score = (report.id == mLastTrackId ? mSwitchingWeight : 0);
        return stability_score + age_score + switching_score;
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
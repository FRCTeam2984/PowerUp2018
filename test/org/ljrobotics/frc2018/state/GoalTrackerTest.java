package org.ljrobotics.frc2018.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.frc2018.state.GoalTracker;
import org.ljrobotics.lib.util.DummyFPGATimer;
import org.ljrobotics.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.Timer;

public class GoalTrackerTest {

	private GoalTracker tracker;
	private DummyFPGATimer timer;
	
	@Before
	public void before() {
		this.tracker = new GoalTracker();
		this.timer = new DummyFPGATimer();
		Timer.SetImplementation(this.timer);
	}
	
	@BeforeEach
	public void beforeEach() {
		this.tracker.reset();
	}
	
	@Test
	public void newGoalTrackerHasNoTracks() {
		GoalTracker tracker = new GoalTracker();
		assertTrue(tracker.getTracks().isEmpty());
		assertFalse(tracker.hasTracks());
	}
	
	@Test
	public void resetClearsAllTrackingInformation() {
		GoalTracker tracker = new GoalTracker();
		tracker.update(0, Arrays.asList(
				new Translation2d[] {Translation2d.identity()}));
		assertTrue(tracker.hasTracks());
		tracker.reset();
		assertFalse(tracker.hasTracks());
	}
	
	@Test
	public void firstUpdateCreatesNewTrack() {
		Translation2d[] targets = new Translation2d[] {Translation2d.identity()};
		tracker.update(0, Arrays.asList(targets));
		assertTrue(tracker.hasTracks());
	}
	
	@Test
	public void firstTrackRunsThroughTargetPoint() {
		Translation2d[] targets = new Translation2d[] {Translation2d.identity()};
		tracker.update(0, Arrays.asList(targets));
		TrackReport report = tracker.getTracks().get(0);
		assertEquals(Translation2d.identity(), report.field_to_goal);
	}
	
	@Test
	public void secondApropriotTargetAddsToCurrentTrack() {
		Translation2d[] targets = new Translation2d[] {Translation2d.identity()};
		Translation2d[] targets2 = new Translation2d[] { new Translation2d(Constants.MAX_TRACKER_DISTANCE/2, 0)};
		timer.setFPGATimestamp(0);
		tracker.update(0, Arrays.asList(targets));
		timer.setFPGATimestamp(1D/30D + Constants.MAX_GOAL_TRACK_AGE/2);
		tracker.update(1D/30D, Arrays.asList(targets2));
		TrackReport report = tracker.getTracks().get(0);
		assertEquals(targets2[0].translateBy(targets[0]).scale(0.5), report.field_to_goal);
		assertEquals(1, tracker.getTracks().size());
	}
	
	@Test
	public void thirdInapropriotTargetIsIgnored() {
		Translation2d[] targets = new Translation2d[] {Translation2d.identity()};
		Translation2d[] targets2 = new Translation2d[] { new Translation2d(Constants.MAX_TRACKER_DISTANCE/2, 0)};
		Translation2d[] targets3 = new Translation2d[] { new Translation2d(-Constants.MAX_TRACKER_DISTANCE*3, 0)};
		timer.setFPGATimestamp(0);
		tracker.update(0, Arrays.asList(targets));
		timer.setFPGATimestamp(1D/30D + Constants.MAX_GOAL_TRACK_AGE/2);
		tracker.update(1D/30D, Arrays.asList(targets2));
		timer.setFPGATimestamp(2D/30D + Constants.MAX_GOAL_TRACK_AGE/2);
		tracker.update(2D/30D, Arrays.asList(targets3));
		TrackReport report = tracker.getTracks().get(0);
		assertEquals(targets2[0].translateBy(targets[0]).scale(0.5), report.field_to_goal);
		assertEquals(1, tracker.getTracks().size());
	}
	
	@Test
	public void thirdInapropriotTargetIsIgnoredWhileCorrectAccepted() {
		Translation2d[] targets = new Translation2d[] { new Translation2d(0,0)};
		Translation2d[] targets2 = new Translation2d[] { new Translation2d(Constants.MAX_TRACKER_DISTANCE/2, 0)};
		Translation2d[] targets3 = new Translation2d[] { new Translation2d(0, 0),
				new Translation2d(-Constants.MAX_TRACKER_DISTANCE*3, 0)};
		timer.setFPGATimestamp(0);
		tracker.update(0, Arrays.asList(targets));
		timer.setFPGATimestamp(1D/30D + Constants.MAX_GOAL_TRACK_AGE/2);
		tracker.update(1D/30D, Arrays.asList(targets2));
		timer.setFPGATimestamp(2D/30D + Constants.MAX_GOAL_TRACK_AGE/2);
		tracker.update(2D/30D, Arrays.asList(targets3));
		TrackReport report = tracker.getTracks().get(0);
		assertEquals(targets2[0].scale(1/3D), report.field_to_goal);
		assertEquals(1, tracker.getTracks().size());
	}
	
	@Test
	public void deadTrackIsPrunded() {
		Translation2d[] targets = new Translation2d[] { new Translation2d(0,0)};
		Translation2d[] targets2 = new Translation2d[] { new Translation2d(Constants.MAX_TRACKER_DISTANCE/2, 0)};
		Translation2d[] targets3 = new Translation2d[] { new Translation2d(100,0)};
		timer.setFPGATimestamp(0);
		tracker.update(0, Arrays.asList(targets));
		timer.setFPGATimestamp(1D/30D);
		tracker.update(1D/30D, Arrays.asList(targets2));
		timer.setFPGATimestamp(2D/30D + Constants.MAX_GOAL_TRACK_AGE);
		tracker.update(2D/30D + Constants.MAX_GOAL_TRACK_AGE, Arrays.asList(targets3));
		TrackReport report = tracker.getTracks().get(0);
		assertEquals(targets3[0], report.field_to_goal);
		assertEquals(1, tracker.getTracks().size());
	}
	
}

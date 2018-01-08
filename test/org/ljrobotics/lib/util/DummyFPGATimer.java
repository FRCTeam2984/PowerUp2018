package org.ljrobotics.lib.util;

import edu.wpi.first.wpilibj.Timer.Interface;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

/**
 * A Dummy FPGA Timer to satisfy the Timer class
 * @author max
 *
 */
public class DummyFPGATimer implements StaticInterface{

	private double fPGATimestamp;
	private double matchTimestamp;
	private boolean periodPassed;
	
	private class DummyInterface implements Interface {

		@Override
		public double get() {
			return fPGATimestamp;
		}

		@Override
		public void reset() {
			
		}

		@Override
		public void start() {
			
		}

		@Override
		public void stop() {
			
		}

		@Override
		public boolean hasPeriodPassed(double period) {
			return periodPassed;
		}
		
	}
	
	@Override
	public double getFPGATimestamp() {
		return this.fPGATimestamp;
	}

	@Override
	public double getMatchTime() {
		return this.matchTimestamp;
	}

	@Override
	public void delay(double seconds) {
		
	}

	@Override
	public Interface newTimer() {
		return new DummyInterface();
	}

	public void setFPGATimestamp(double fPGATimestamp) {
		this.fPGATimestamp = fPGATimestamp;
	}

	public void setMatchTimestamp(double matchTimestamp) {
		this.matchTimestamp = matchTimestamp;
	}

	public void setPeriodPassed(boolean periodPassed) {
		this.periodPassed = periodPassed;
	}
	
}

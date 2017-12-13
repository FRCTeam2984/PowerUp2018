package org.ljrobotics.lib.util;

import edu.wpi.first.wpilibj.HLUsageReporting;

/**
 * Implements the HLUsageReporting.Interface interface. Use in
 * unit tests to prevent errors:
 * 
 * HLUsageReporting.SetImplementation(new DummyReporter());
 * 
 * @author rich
 *
 */
public class DummyReporter implements HLUsageReporting.Interface {

	@Override
	public void reportPIDController(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportScheduler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportSmartDashboard() {
		// TODO Auto-generated method stub
		
	}

}
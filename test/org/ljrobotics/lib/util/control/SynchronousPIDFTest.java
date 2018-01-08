package org.ljrobotics.lib.util.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SynchronousPIDFTest {

	@Test
	public void zeroSetpointProduceZeroOutput() {
		SynchronousPIDF pid = new SynchronousPIDF();
		pid.setSetpoint(0);
		assertEquals(0, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pureProportinalPIDGivesProportionalResponseInPositiveRange() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0);
		pid.setSetpoint(1);
		assertEquals(1, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pureProportinalPIDGivesProportionalResponseInNegativeRange() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0);
		pid.setSetpoint(-1);
		assertEquals(-1, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void setOutputRangeLimitsPositiveOutput() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0);
		pid.setOutputRange(-1, 0.5);
		pid.setSetpoint(1);
		assertEquals(0.5, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void setOutputRangeLimitsNegativeOutput() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0);
		pid.setOutputRange(-0.5, 0);
		pid.setSetpoint(-1);
		assertEquals(-0.5, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pureIControllerOutputsErrorIndefinatly() {
		SynchronousPIDF pid = new SynchronousPIDF(0,1,0);
		pid.setSetpoint(1);
		pid.calculate(0, 1);
		pid.calculate(1, 1);
		assertEquals(1, pid.calculate(1, 1), 0.00001);
	}
	
	@Test
	public void pureIControllerDecreaseOutputWhileOvershooting() {
		SynchronousPIDF pid = new SynchronousPIDF(0,1,0);
		pid.setSetpoint(1);
		pid.calculate(0, 1);
		pid.calculate(1, 1);
		assertEquals(0.5, pid.calculate(1.5, 1), 0.00001);
	}
	
	@Test
	public void pIControllerIgnoreIWhenMaxPOutput() {
		SynchronousPIDF pid = new SynchronousPIDF(1,1,0);
		pid.setSetpoint(1);
		pid.calculate(0, 1);
		pid.calculate(0, 1);
		assertEquals(0.5, pid.calculate(0.75, 1), 0.00001);
	}
	
	@Test
	public void pureDControllerHasZeroOutputAtConstantError() {
		SynchronousPIDF pid = new SynchronousPIDF(0,0,1);
		pid.setSetpoint(1);
		pid.calculate(0, 1);
		assertEquals(0, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pureDControllerHasOutputAtChangeingError() {
		SynchronousPIDF pid = new SynchronousPIDF(0,0,1);
		pid.setSetpoint(1);
		pid.calculate(1, 1);
		assertEquals(1, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pIDIntegrationTest() {
		SynchronousPIDF pid = new SynchronousPIDF(1,1,1);
		pid.setSetpoint(1);
		assertEquals(1, 	pid.calculate(0, 1), 0.00001);
		assertEquals(0.5, 	pid.calculate(0.5, 1), 0.00001);
		assertEquals(0, 	pid.calculate(1, 1), 0.00001);
		assertEquals(-1, 	pid.calculate(1.5, 1), 0.00001);
		assertEquals(0.5, 	pid.calculate(1, 1), 0.00001);
		assertEquals(0, 	pid.calculate(1, 1), 0.00001);
	}
	
	@Test
	public void pureFControllerIgnoresFeedback() {
		SynchronousPIDF pid = new SynchronousPIDF(0,0,0, 1);
		pid.setSetpoint(1);
		assertEquals(1, pid.calculate(1, 1), 0.00001);
		assertEquals(1, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void pIDFIntegrationTest() {
		SynchronousPIDF pid = new SynchronousPIDF(1, 1, 1, 0.1);
		pid.setSetpoint(1);
		assertEquals(1, 	pid.calculate(0, 1), 0.00001);
		assertEquals(0.6, 	pid.calculate(0.5, 1), 0.00001);
		assertEquals(0.1, 	pid.calculate(1, 1), 0.00001);
		assertEquals(-0.9, 	pid.calculate(1.5, 1), 0.00001);
		assertEquals(0.6, 	pid.calculate(1, 1), 0.00001);
		assertEquals(0.1, 	pid.calculate(1, 1), 0.00001);
	}
	
	@Test
	public void continousPControllerWrapsPowerPostivily() {
		SynchronousPIDF pid = new SynchronousPIDF(1, 0, 0, 0);
		pid.setContinuous();
		pid.setSetpoint(0.2);
		pid.setInputRange(0, 1);
		assertEquals(0.3, pid.calculate(0.9, 1), 0.00001);
	}
	
	@Test
	public void continousPControllerWrapsPowerNegativly() {
		SynchronousPIDF pid = new SynchronousPIDF(1, 0, 0, 0);
		pid.setContinuous();
		pid.setSetpoint(0.9);
		pid.setInputRange(0, 1);
		assertEquals(-0.3, pid.calculate(0.2, 1), 0.00001);
	}
	
	@Test
	public void setPIDUpdatesPIDValues() {
		SynchronousPIDF pid = new SynchronousPIDF();
		pid.setSetpoint(1);
		pid.setPID(0.5, 0, 0);
		assertEquals(0.5, pid.calculate(0, 1), 0.00001);
		assertEquals(pid.getP(), 0.5, 0.00001);
		assertEquals(pid.getI(), 0, 0.00001);
		assertEquals(pid.getD(), 0, 0.00001);
	}
	
	@Test
	public void setPIDFUpdatesPIDValues() {
		SynchronousPIDF pid = new SynchronousPIDF();
		pid.setSetpoint(1);
		pid.setPID(0.5, 0, 0, 0.5);
		assertEquals(1, pid.calculate(0, 1), 0.00001);
		assertEquals(pid.getP(), 0.5, 0.00001);
		assertEquals(pid.getF(), 0.5, 0.00001);
	}
	
	@Test
	public void deadBandDisablesProportionalControl() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0,0);
		pid.setSetpoint(1);
		pid.setDeadband(0.5);
		assertEquals(0, pid.calculate(0.75, 1), 0.00001);
		assertEquals(1, pid.calculate(0.0, 1), 0.00001);
	}
	
	@Test
	public void onTargetControllerReturnsOnTarget() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0,0);
		pid.setSetpoint(1);
		pid.calculate(0.95, 1);
		assertTrue(pid.onTarget(0.1));
	}
	
	@Test
	public void resetControllerIsNotOnTarget() {
		SynchronousPIDF pid = new SynchronousPIDF(1,0,0,0);
		pid.setSetpoint(1);
		pid.calculate(1, 0);
		pid.reset();
		assertFalse(pid.onTarget(100));
	}
	
	@Test
	public void resetIntegratorStopsIFactor() {
		SynchronousPIDF pid = new SynchronousPIDF(0,0.5,0,0);
		pid.setSetpoint(1);
		pid.calculate(0, 1);
		pid.resetIntegrator();
		assertEquals(0.5, pid.calculate(0, 1), 0.00001);
	}
	
	@Test
	public void getStateReturnsStringRepresentationOfState() {
		SynchronousPIDF pid = new SynchronousPIDF(1,2,3);
		assertEquals("Kp: 1.0\nKi: 2.0\nKd: 3.0\n", pid.getState());
	}
	
}

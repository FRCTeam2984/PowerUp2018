package org.ljrobotics.lib.util;

/**
 * Helper class to implement "Cheesy Drive". "Cheesy Drive" simply means that the "turning" stick controls the curvature
 * of the robot's path rather than its rate of heading change. This helps make the robot more controllable at high
 * speeds. Also handles the robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
 * turn-in-place maneuvers.
 */
public class CheesyDriveHelper {

    private static final double THROTTLE_DEADBAND = 0.02;
    private static final double WHEEL_DEADBAND = 0.02;

    // These factor determine how fast the wheel traverses the "non linear" sine curve.
    private static final double HIGH_WHEEL_NON_LINEARITY = 0.65;
    private static final double LOW_WHEEL_NON_LINEARITY = 0.5;

    private static final double HIGH_NEG_INERTIA_SCALAR = 4.0;

    private static final double LOW_NEG_INERTIA_THRESHOLD = 0.65;
    private static final double LOW_NEG_INERTIA_TURN_SCALAR = 3.5;
    private static final double LOW_NEG_INERTIA_CLOSE_SCALAR = 4.0;
    private static final double ScalarLOW_NEG_INERTIA_FAR_SCALAR = 5.0;

    private static final double HIGH_SENSITIVITY = 0.95;
    private static final double LOW_SENSITIVITY = 1.3;

    private static final double QUICK_STOP_DEADBAND = 0.2;
    private static final double QUICK_STOP_WEIGHT = 0.1;
    private static final double QUICK_STOP_SCALAR = 5.0;

    private static CheesyDriveHelper instance;
    
    public static CheesyDriveHelper getInstance() {
    	if(instance == null) {
    		instance = new CheesyDriveHelper();
    	}
    	return instance;
    }
    
    private double oldWheel = 0.0;
    private double quickStopAccumulator = 0.0;
    private double negInertiaAccumulator = 0.0;

	/**
	 * gives the cheesy drive output for the desired inputs. Note that there is
	 * state attached to this method.
	 * 
	 * @param throttle
	 *            how fast to move forward [+1, -1]
	 * @param wheel
	 *            how fast to turn right [+1, -1]
	 * @param isQuickTurn
	 *            whether or not to spin in place
	 * @param isHighGear
	 *            switches constants to reduce sensitivity
	 * @return the control output that can be passed to the drive subsystem
	 */
    public DriveSignal cheesyDrive(double throttle, double wheel, boolean isQuickTurn,
            boolean isHighGear) {

        wheel = handleDeadband(wheel, WHEEL_DEADBAND);
        throttle = handleDeadband(throttle, THROTTLE_DEADBAND);

        double negInertia = wheel - oldWheel;
        oldWheel = wheel;

        double wheelNonLinearity;
        if (isHighGear) {
            wheelNonLinearity = HIGH_WHEEL_NON_LINEARITY;
            final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
        } else {
            wheelNonLinearity = LOW_WHEEL_NON_LINEARITY;
            final double denominator = Math.sin(Math.PI / 2.0 * wheelNonLinearity);
            // Apply a sin function that's scaled to make it feel better.
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
            wheel = Math.sin(Math.PI / 2.0 * wheelNonLinearity * wheel) / denominator;
        }

        double leftPwm, rightPwm, overPower;
        double sensitivity;

        double angularPower;
        double linearPower;

        // Negative inertia!
        double negInertiaScalar;
        if (isHighGear) {
            negInertiaScalar = HIGH_NEG_INERTIA_SCALAR;
            sensitivity = HIGH_SENSITIVITY;
        } else {
            if (wheel * negInertia > 0) {
                // If we are moving away from 0.0, aka, trying to get more wheel.
                negInertiaScalar = LOW_NEG_INERTIA_TURN_SCALAR;
            } else {
                // Otherwise, we are attempting to go back to 0.0.
                if (Math.abs(wheel) > LOW_NEG_INERTIA_THRESHOLD) {
                    negInertiaScalar = ScalarLOW_NEG_INERTIA_FAR_SCALAR;
                } else {
                    negInertiaScalar = LOW_NEG_INERTIA_CLOSE_SCALAR;
                }
            }
            sensitivity = LOW_SENSITIVITY;
        }
        double negInertiaPower = negInertia * negInertiaScalar;
        negInertiaAccumulator += negInertiaPower;

        wheel = wheel + negInertiaAccumulator;
        if (negInertiaAccumulator > 1) {
            negInertiaAccumulator -= 1;
        } else if (negInertiaAccumulator < -1) {
            negInertiaAccumulator += 1;
        } else {
            negInertiaAccumulator = 0;
        }
        linearPower = throttle;

        // Quickturn!
        if (isQuickTurn) {
            if (Math.abs(linearPower) < QUICK_STOP_DEADBAND) {
                double alpha = QUICK_STOP_WEIGHT;
                quickStopAccumulator = (1 - alpha) * quickStopAccumulator
                        + alpha * Util.limit(wheel, 1.0) * QUICK_STOP_SCALAR;
            }
            overPower = 1.0;
            angularPower = wheel;
        } else {
            overPower = 0.0;
            angularPower = Math.abs(throttle) * wheel * sensitivity - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        rightPwm = leftPwm = linearPower;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        if (leftPwm > 1.0) {
            rightPwm -= overPower * (leftPwm - 1.0);
            leftPwm = 1.0;
        } else if (rightPwm > 1.0) {
            leftPwm -= overPower * (rightPwm - 1.0);
            rightPwm = 1.0;
        } else if (leftPwm < -1.0) {
            rightPwm += overPower * (-1.0 - leftPwm);
            leftPwm = -1.0;
        } else if (rightPwm < -1.0) {
            leftPwm += overPower * (-1.0 - rightPwm);
            rightPwm = -1.0;
        }
        
        leftPwm = Math.min(Math.max(-1, leftPwm), 1);
        rightPwm = Math.min(Math.max(-1, rightPwm), 1);
        
        return new DriveSignal(leftPwm, rightPwm);
    }

    public double handleDeadband(double val, double deadband) {
        return (Math.abs(val) > Math.abs(deadband)) ? val : 0.0;
    }
}

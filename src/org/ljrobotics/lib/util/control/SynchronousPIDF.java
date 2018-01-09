package org.ljrobotics.lib.util.control;

import edu.wpi.first.wpilibj.util.BoundaryException;

/**
 * This class implements a PID Control Loop.
 * 
 * Does all computation synchronously (i.e. the calculate() function must be
 * called by the user from his own thread)
 */
public class SynchronousPIDF {
    private double pConstant; // factor for "proportional" control
    private double iConstant; // factor for "integral" control
    private double dConstant; // factor for "derivative" control
    private double fConstant; // factor for feed forward gain
    private double maximumOutput = 1.0; // |maximum output|
    private double minimumOutput = -1.0; // |minimum output|
    private double maximumInput = 0.0; // maximum input - limit setpoint to
                                         // this
    private double minimumInput = 0.0; // minimum input - limit setpoint to
                                         // this
    private boolean continuous = false; // do the endpoints wrap around? eg.
                                          // Absolute encoder
    private double prevError = 0.0; // the prior sensor input (used to compute
                                      // velocity)
    private double totalError = 0.0; // the sum of the errors for use in the
                                       // integral calc
    private double setpoint = 0.0;
    private double error = 0.0;
    private double result = 0.0;
    private double lastInput = Double.NaN;
    private double deadband = 0.0; // If the absolute error is less than
                                     // deadband
                                     // then treat error for the proportional
                                     // term as 0

    public SynchronousPIDF() {
    	this(0,0,0);
    }

    /**
     * Allocate a PID object with the given constants for P, I, D
     *
     * @param Kp
     *            the proportional coefficient
     * @param Ki
     *            the integral coefficient
     * @param Kd
     *            the derivative coefficient
     */
    public SynchronousPIDF(double Kp, double Ki, double Kd) {
        this(Kp, Ki, Kd, 0);
    }

    /**
     * Allocate a PID object with the given constants for P, I, D
     *
     * @param Kp
     *            the proportional coefficient
     * @param Ki
     *            the integral coefficient
     * @param Kd
     *            the derivative coefficient
     * @param Kf
     *            the feed forward gain coefficient
     */
    public SynchronousPIDF(double Kp, double Ki, double Kd, double Kf) {
        pConstant = Kp;
        iConstant = Ki;
        dConstant = Kd;
        fConstant = Kf;
    }

	/**
	 * Read the input, calculate the output accordingly, and write to the output.
	 * This should be called at a constant rate by the user (ex. in a timed thread)
	 *
	 * @param input
	 *            the input
	 * @param dt
	 *            time passed since previous call to calculate
	 */
    public double calculate(double input, double dt) {
        if (dt < 1E-6)
            dt = 1E-6;
        lastInput = input;
        error = setpoint - input;
        if (continuous) {
            if (Math.abs(error) > (maximumInput - minimumInput) / 2) {
                if (error > 0) {
                    error = error - maximumInput + minimumInput;
                } else {
                    error = error + maximumInput - minimumInput;
                }
            }
        }

        if ((error * pConstant < maximumOutput) && (error * pConstant > minimumOutput)) {
            totalError += error * dt;
        } else {
            totalError = 0;
        }

        // Don't blow away m_error so as to not break derivative
        double proportionalError = Math.abs(error) < deadband ? 0 : error;

        result = (pConstant * proportionalError + iConstant * totalError + dConstant * (error - prevError) / dt
                + fConstant * setpoint);
        prevError = error;

        if (result > maximumOutput) {
            result = maximumOutput;
        } else if (result < minimumOutput) {
            result = minimumOutput;
        }
        return result;
    }

	/**
	 * Set the PID controller gain parameters. Set the proportional, integral, and
	 * differential coefficients.
	 *
	 * @param p
	 *            Proportional coefficient
	 * @param i
	 *            Integral coefficient
	 * @param d
	 *            Differential coefficient
	 */
    public void setPID(double p, double i, double d) {
        this.setPID(p, i, d, this.fConstant);
    }

	/**
	 * Set the PID controller gain parameters. Set the proportional, integral, and
	 * differential coefficients.
	 *
	 * @param p
	 *            Proportional coefficient
	 * @param i
	 *            Integral coefficient
	 * @param d
	 *            Differential coefficient
	 * @param f
	 *            Feed forward coefficient
	 */
    public void setPID(double p, double i, double d, double f) {
        pConstant = p;
        iConstant = i;
        dConstant = d;
        fConstant = f;
    }

    /**
     * Get the Proportional coefficient
     *
     * @return proportional coefficient
     */
    public double getP() {
        return pConstant;
    }

    /**
     * Get the Integral coefficient
     *
     * @return integral coefficient
     */
    public double getI() {
        return iConstant;
    }

    /**
     * Get the Differential coefficient
     *
     * @return differential coefficient
     */
    public double getD() {
        return dConstant;
    }

    /**
     * Get the Feed forward coefficient
     *
     * @return feed forward coefficient
     */
    public double getF() {
        return fConstant;
    }

	/**
	 * Return the current PID result This is always centered on zero and constrained
	 * the the max and min outs
	 *
	 * @return the latest calculated output
	 */
    public double get() {
        return result;
    }

	/**
	 * Set the PID controller to consider the input to be continuous, Rather then
	 * using the max and min in as constraints, it considers them to be the same
	 * point and automatically calculates the shortest route to the setpoint.
	 * 
	 * This will cause the controller to wrap around as is the case with positioning
	 * a turret. Instead of going from 350 to 0 degrees the long way it will go the
	 * short way.
	 *
	 * @param continuous
	 *            Set to true turns on continuous, false turns off continuous
	 */
    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public void setDeadband(double deadband) {
        this.deadband = deadband;
    }

    /**
     * shorthand for {@link #setContinuous(boolean) setContinous(true)}
     */
    public void setContinuous() {
        this.setContinuous(true);
    }

    /**
     * Sets the maximum and minimum values expected from the input.
     *
     * @param minimumInput
     *            the minimum value expected from the input
     * @param maximumInput
     *            the maximum value expected from the output
     */
    public void setInputRange(double minimumInput, double maximumInput) {
        if (minimumInput > maximumInput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        this.minimumInput = minimumInput;
        this.maximumInput = maximumInput;
        setSetpoint(setpoint);
    }

    /**
     * Sets the minimum and maximum values to write.
     *
     * @param minimumOutput
     *            the minimum value to write to the output
     * @param maximumOutput
     *            the maximum value to write to the output
     */
    public void setOutputRange(double minimumOutput, double maximumOutput) {
        if (minimumOutput > maximumOutput) {
            throw new BoundaryException("Lower bound is greater than upper bound");
        }
        this.minimumOutput = minimumOutput;
        this.maximumOutput = maximumOutput;
    }

    /**
     * Set the setpoint for the PID controller
     *
     * @param setpoint
     *            the desired setpoint
     */
    public void setSetpoint(double setpoint) {
        if (maximumInput > minimumInput) {
            if (setpoint > maximumInput) {
                this.setpoint = maximumInput;
            } else if (setpoint < minimumInput) {
                this.setpoint = minimumInput;
            } else {
                this.setpoint = setpoint;
            }
        } else {
            this.setpoint = setpoint;
        }
    }

    /**
     * Returns the current setpoint of the PID controller
     *
     * @return the current setpoint
     */
    public double getSetpoint() {
        return setpoint;
    }

    /**
     * Returns the current difference of the input from the setpoint
     *
     * @return the current error
     */
    public double getError() {
        return error;
    }

    /**
     * Return true if the error is within the tolerance
     *
     * @return true if the error is less than the tolerance
     */
    public boolean onTarget(double tolerance) {
        return lastInput != Double.NaN && Math.abs(lastInput - setpoint) < tolerance;
    }

    /**
     * Reset all internal terms.
     */
    public void reset() {
        lastInput = Double.NaN;
        prevError = 0;
        totalError = 0;
        result = 0;
        setpoint = 0;
    }

    public void resetIntegrator() {
        totalError = 0;
    }

    public String getState() {
        String lState = "";

        lState += "Kp: " + pConstant + "\n";
        lState += "Ki: " + iConstant + "\n";
        lState += "Kd: " + dConstant + "\n";

        return lState;
    }

    public String getType() {
        return "PIDController";
    }
}

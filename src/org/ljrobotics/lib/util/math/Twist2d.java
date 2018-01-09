package org.ljrobotics.lib.util.math;

import org.ljrobotics.frc2018.Constants;
import org.ljrobotics.lib.util.Util;

/**
 * A movement along an arc at constant curvature and velocity. We can use ideas from "differential calculus" to create
 * new RigidTransform2d's from a Twist2d and visa versa.
 * 
 * A Twist can be used to represent a difference between two poses, a velocity, an acceleration, etc.
 */
public class Twist2d {
    protected static final Twist2d kIdentity = new Twist2d(0.0, 0.0, 0.0);

    public static final Twist2d identity() {
        return kIdentity;
    }

    public final double dx;
    public final double dy;
    public final double dtheta; // Radians!

    public Twist2d(double dx, double dy, double dtheta) {
        this.dx = dx;
        this.dy = dy;
        this.dtheta = dtheta;
    }

    public Twist2d scaled(double scale) {
        return new Twist2d(dx * scale, dy * scale, dtheta * scale);
    }
    
    @Override
    public boolean equals(Object o) {
    	if(this == o) {
    		return true;
    	}
    	if(o == null || o.getClass() != this.getClass()) {
    		return false;
    	}
    	Twist2d other = (Twist2d) o;
    	boolean same = Util.epsilonEquals(other.dx, this.dx, Constants.EQUALS_EPSILON);
    	same &= Util.epsilonEquals(this.dy, other.dy, Constants.EQUALS_EPSILON);
    	same &= Util.epsilonEquals(this.dtheta, other.dtheta, Constants.EQUALS_EPSILON);
    	return same;
    }
}
package org.ljrobotics.frc2018.vision;

/**
 * A container class for Targets detected by the vision system, containing the
 * location. This location is stored in polar coordinates from the phone's
 * perspective. Rotation is the rotation about the phone in degrees with zero
 * being head on and distance is the distance from the phone in inches.
 */
public class TargetInfo {
	protected double distance;
	protected double rotation;
	
	private double x;
	private double y;
	private double z;

	/**
	 * Creates a target info
	 * 
	 * @param distance
	 *            the distance from the phone in inches
	 * @param rotation
	 *            the rotation about the phone in degrees
	 */
	public TargetInfo(double distance, double rotation) {
		this.distance = distance;
		this.rotation = rotation;
		
		rotation = Math.toRadians(rotation);
		
		this.x = -this.distance * Math.sin(rotation);
		this.y = this.distance * Math.cos(rotation);
		this.z = 0;
	}

	public double getDistance() {
		return distance;
	}

	public double getRotation() {
		return rotation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
}
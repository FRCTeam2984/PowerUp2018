package org.ljrobotics.lib.util;

import java.util.LinkedList;

/**
 * Implements a simple circular buffer. You can get the average of the last n
 * values added where n is the window size of the buffer. Recompute average will
 * make sure that the value of get average is correct as floating point error
 * can occur while adding values.
 */
public class CircularBuffer {
	private int windowSize;
	private LinkedList<Double> samples;
	private double sum;

	/**
	 * Creates a new Circular buffer with the given window size
	 * 
	 * @param windowSize
	 *            the number of elements to keep track of
	 */
	public CircularBuffer(int windowSize) {
		this.windowSize = windowSize;
		this.samples = new LinkedList<Double>();
		this.sum = 0.0;
	}

	/**
	 * Clears the buffer
	 */
	public void clear() {
		samples.clear();
		sum = 0.0;
	}

	/**
	 * 
	 * @return the average of all the values in the current window
	 */
	public double getAverage() {
		if (samples.isEmpty())
			return 0.0;
		return sum / samples.size();
	}

	/**
	 * Removes any floating point error that may have been accrued.
	 */
	public void recomputeAverage() {
		sum = 0.0;
		if (samples.isEmpty())
			return;
		for (Double val : samples) {
			sum += val;
		}
	}

	/**
	 * Adds a new value to the buffer and if it is full removes the oldest value.
	 * 
	 * @param val
	 *            the new value to add
	 */
	public void addValue(double val) {
		samples.addLast(val);
		sum += val;
		if (samples.size() > windowSize) {
			sum -= samples.removeFirst();
		}
	}

	/**
	 * 
	 * @return the current number of values in the buffer window
	 */
	public int getNumValues() {
		return samples.size();
	}

	/**
	 * 
	 * @return whether or not the buffer window is full
	 */
	public boolean isFull() {
		return windowSize == samples.size();
	}
}

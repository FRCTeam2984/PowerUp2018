package org.ljrobotics.lib.util;

/**
 * An iterative boolean latch that delays the transition from false to true.
 *
 */
public class DelayedBoolean {
    private boolean lastValue;
    private double transitionTimestamp;
    private double delay;

    public DelayedBoolean(double timestamp, double delay) {
        transitionTimestamp = timestamp;
        lastValue = false;
        this.delay = delay;
    }

	/**
	 * Updates the boolean. If value is false it will return false and reset the
	 * boolean. If the value is true while the previous value was false it will lock
	 * the given timestamp. All subsequent calls with a true value will return false
	 * if the timestamp is at or before the set timestamp plus the delay.
	 * 
	 * @param timestamp
	 *            the current timestamp to check or latch
	 * @param value
	 *            false to reset true to set/read
	 * @return whether or not it has been delay time since the latching of the
	 *         boolean
	 */
    public boolean update(double timestamp, boolean value) {
        boolean result = false;

        if (value && !lastValue) {
            transitionTimestamp = timestamp;
        }

        // If we are still true and we have transitioned.
        if (value && (timestamp - transitionTimestamp > delay)) {
            result = true;
        }

        lastValue = value;
        return result;
    }
}

package org.ljrobotics.lib.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream that fakes returning only the string that was passed
 * 
 * @author max
 *
 */
public class DummyInputStream extends InputStream {

	private String returnString;
	private boolean onlyReturnOnce;
	private boolean returned;

	/**
	 * Create a new Dummy Input Stream that will return a string in a buffer
	 * @param returnString the string to return
	 * @param onlyReturnOnce if true all but the first invocation of read will return -1;
	 */
	public DummyInputStream(String returnString, boolean onlyReturnOnce) {
		this.returnString = returnString;
		this.onlyReturnOnce = onlyReturnOnce;
		this.returned = false;
	}

	@Override
	public int read() throws IOException {
		return -1;
	}

	@Override
	public int read(byte[] buffer) {
		if(this.onlyReturnOnce && this.returned) {
			return -1;
		}
		this.returned = true;
		byte[] stringBytes = this.returnString.getBytes();
		System.arraycopy(stringBytes, 0, buffer, 0, stringBytes.length);
		return stringBytes.length;
	}

}

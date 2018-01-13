package org.ljrobotics.lib.util;

public class IncorrectGameData extends Exception{

	/**
	 * 
	 */
	private String errorData;
	private static final long serialVersionUID = 1L;
	
	public IncorrectGameData(String errDesc) {
		this.errorData=errDesc;
	}
	
	public String getErrorData() {
		return this.errorData;
	}
}

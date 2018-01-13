package org.ljrobotics.lib.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;

// import edu.wpi.first.wpilibj.DriverStation;

/*
 * Util to get game data
 */

public class GameData {
	
	private PaddleSide[] PaddleLocations;
	private String gameString;
	
	public GameData(String gs) throws IncorrectGameData {
		this.gameString=gs;
		try {
			this.PaddleLocations=stringToPaddleSide(this.gameString);
		}
		catch(IncorrectGameData e) {
			throw e;
		}
	}
	
	public GameData() throws IncorrectGameData {
		this(DriverStation.getInstance().getGameSpecificMessage());
	}
	
	/*public void Poll() throws ErrorPollingGameData {
		String got = DriverStation.getInstance().getGameSpecificMessage();
		if( got.length() != 3 ) {
			throw new ErrorPollingGameData(got.length());
		} else {
			this.gameString = got;
		}
		try {
			this.PaddleLocations = stringToPaddleSide(this.gameString);
		}
		catch(IncorrectGameData e) {
			System.out.println(e.getErrorData());
		}
	}*/
	
	public PaddleSide[] GetPaddleSides() {
		return PaddleLocations;
	}
	
	public PaddleSide GetPaddleSide(int i) {
		return PaddleLocations[i];
	}
	
	public PaddleSide[] stringToPaddleSide(String ings) throws IncorrectGameData {
		if(ings.length() != 3) {
			throw new IncorrectGameData("Input string is not 3 characters");
		}
		ArrayList<PaddleSide> toReturn = new ArrayList<PaddleSide>();
		for(int i=0; i < ings.length(); i++) {
			switch(ings.charAt(i)) {
			case 'L':
				toReturn.add(PaddleSide.LEFT);
				break;
			case 'R':
				toReturn.add(PaddleSide.RIGHT);
				break;
			default:
				throw new IncorrectGameData("Unknown character in string to paddle side");
			}
		}
		return toReturn.toArray(new PaddleSide[toReturn.size()]);
		
	}

}

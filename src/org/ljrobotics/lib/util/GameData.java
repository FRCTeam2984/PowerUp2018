package org.ljrobotics.lib.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;

/*
 * Util to get game data
 */

enum PaddleSide {
	RIGHT, LEFT
}

public class GameData {
	
	private PaddleSide[] PaddleLocations;
	private String gameString;
	
	public GameData() {
		this.gameString = DriverStation.getInstance().getGameSpecificMessage();
		try {
			this.PaddleLocations = stringToPaddleSide(this.gameString);
		}
		catch(IncorrectGameData e) {
			System.out.println(e.getErrorData());
		}
	}
	
	public PaddleSide[] GetPaddleSides() {
		return PaddleLocations;
	}
	
	public PaddleSide GetPaddleSide(int i) {
		return PaddleLocations[i];
	}
	
	private PaddleSide[] stringToPaddleSide(String ings) throws IncorrectGameData {
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
		return (PaddleSide[]) toReturn.toArray();
		
	}

}

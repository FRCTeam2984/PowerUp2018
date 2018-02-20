package org.ljrobotics.frc2018.subsystems;

import org.ljrobotics.frc2018.commands.WaitSecond;
import org.ljrobotics.frc2018.commands.auto.ScaleCommand;
import org.ljrobotics.frc2018.commands.auto.SteightPathCommand;
import org.ljrobotics.frc2018.commands.auto.SwitchCommand;
import org.ljrobotics.frc2018.commands.auto.TwoCubeCenterLeft;
import org.ljrobotics.frc2018.commands.auto.TwoCubeCenterRight;
import org.ljrobotics.frc2018.paths.CenterLeftScale;
import org.ljrobotics.frc2018.paths.CenterLeftSwitch;
import org.ljrobotics.frc2018.paths.CenterRightScale;
import org.ljrobotics.frc2018.paths.CenterRightSwitch;
import org.ljrobotics.frc2018.paths.LeftLeftScale;
import org.ljrobotics.frc2018.paths.LeftLeftSwitch;
import org.ljrobotics.frc2018.paths.LeftRightScale;
import org.ljrobotics.frc2018.paths.LeftRightSwitch;
import org.ljrobotics.frc2018.paths.RightLeftScale;
import org.ljrobotics.frc2018.paths.RightLeftSwitch;
import org.ljrobotics.frc2018.paths.RightRightScale;
import org.ljrobotics.frc2018.paths.RightRightSwitch;
import org.ljrobotics.lib.util.CrashTracker;
import org.ljrobotics.lib.util.GameData;
import org.ljrobotics.lib.util.IncorrectGameData;
import org.ljrobotics.lib.util.PaddleSide;
import org.ljrobotics.lib.util.control.PathContainer;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A class to get the autonomous command from the switch on the robot. Below is
 * a list of the different commands.
 * <ul>
 * <li>0 - Left Switch</li>
 * <li>1 - Center Switch</li>
 * <li>2 - Right Switch</li>
 * <li>3 - Left Scale</li>
 * <li>4 - Center Scale</li>
 * <li>5 - Right Scale</li>
 * <li>6 - Drive past line forward</li>
 * <li>7 - Do Nothing</li>
 * </ul>
 * 
 * @author max
 *
 */
public class AutoSelectorSwitch {

	private static AutoSelectorSwitch instance;
	private GameData gameData;

	public static AutoSelectorSwitch getInstance() {
		if (instance == null) {
			DigitalInput ones = new DigitalInput(0);
			DigitalInput twos = new DigitalInput(1);
			DigitalInput fours = new DigitalInput(2);
			instance = new AutoSelectorSwitch(ones, twos, fours);
		}
		return instance;
	}

	private DigitalInput ones;
	private DigitalInput twos;
	private DigitalInput fours;

	public AutoSelectorSwitch(DigitalInput ones, DigitalInput twos, DigitalInput fours) {
		this.ones = ones;
		this.twos = twos;
		this.fours = fours;
		this.gameData = null;
	}

	public int getAutoMode() {
		boolean ones = this.ones.get();
		boolean twos = this.twos.get();
		boolean fours = this.fours.get();
		return this.getAutoMode(ones, twos, fours);
	}

	public int getAutoMode(boolean ones, boolean twos, boolean fours) {
		int result = 0;
		result += ones ? 0 : 1;
		result += twos ? 0 : 2;
		result += fours ? 0 : 4;
		return result;
	}

	public Command getAutoCommand() {
		if(this.gameData == null) {
			System.out.println("Could not read game data");
			CrashTracker.logMessage("Did not find game data!");
			return new WaitSecond(1);
		}
		System.out.println("Auto mode " + this.getAutoMode());
		PaddleSide paddleSide;
		PathContainer pathContainer;
		switch (getAutoMode()) {
		case 0:
			paddleSide = gameData.GetPaddleSide(0);
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new LeftLeftSwitch() : new LeftRightSwitch();
			return new SwitchCommand(pathContainer);
		case 1:
			paddleSide = gameData.GetPaddleSide(0);
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new CenterLeftSwitch() : new CenterRightSwitch();
			return new SwitchCommand(pathContainer);
		case 2:
			paddleSide = gameData.GetPaddleSide(0);
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new RightLeftSwitch() : new RightRightSwitch();
			return new SwitchCommand(pathContainer);
		case 3:
			paddleSide = gameData.GetPaddleSide(1);
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new LeftLeftScale() : new LeftRightScale();
			return new ScaleCommand(pathContainer, (paddleSide == PaddleSide.LEFT) ? -90 : 90);
		case 4:
			paddleSide = gameData.GetPaddleSide(1);
			if(gameData.GetPaddleSide(0) == paddleSide) {
				if(paddleSide == PaddleSide.LEFT) {
					return new TwoCubeCenterLeft();
				} else {
					return new TwoCubeCenterRight();
				}
			}
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new CenterLeftScale() : new CenterRightScale();
			return new ScaleCommand(pathContainer, (paddleSide == PaddleSide.LEFT) ? -90 : 90);
		case 5:
			paddleSide = gameData.GetPaddleSide(1);
			pathContainer = (paddleSide == PaddleSide.LEFT) ? new RightLeftScale() : new RightRightScale();
			return new ScaleCommand(pathContainer, (paddleSide == PaddleSide.LEFT) ? -90 : 90);
		case 6:
			return new SteightPathCommand();
		default:
			return new WaitSecond(0);
		}
	}

	public void querryGameData() {
		try {
			this.gameData = new GameData();
			System.out.println("Game Data Found! " + this.gameData);
		} catch (IncorrectGameData e) {
			System.out.println("No Game Data Found Yet");
		}
	}

	public boolean hasGameData() {
		return this.gameData != null;
	}

	public class PathChooser {
		public final PathContainer left;
		public final PathContainer right;
		public final boolean scale;

		public PathChooser(PathContainer left, PathContainer right, boolean scale) {
			this.left = left;
			this.right = right;
			this.scale = scale;
		}

		public PathContainer getCorrect() {
			GameData gameData = null;
			int id = this.scale ? 1 : 0;
			try {
				gameData = new GameData();
				if (gameData.GetPaddleSide(id) == PaddleSide.LEFT) {
					return this.left;
				} else if (gameData.GetPaddleSide(id) == PaddleSide.RIGHT) {
					return this.right;
				}
			} catch (IncorrectGameData e) {
				System.out.println(e.getErrorData());
			}
			return this.left;
		}
	}

}
